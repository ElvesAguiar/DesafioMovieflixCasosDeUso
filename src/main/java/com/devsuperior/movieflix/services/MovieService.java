package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repository;


    @Transactional(readOnly = true)
    public Page<MovieCardDTO> findAll(Pageable pageable) {
        List<MovieCardDTO> dtos = repository.findAll().
                stream().sorted((m ,a)-> m.getTitle().compareTo(a.getTitle())).map(x -> new MovieCardDTO(x))
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());
        List<MovieCardDTO> pageList = dtos.subList(start, end);

        Page<MovieCardDTO> page = new PageImpl<>(pageList, pageable, dtos.size());
        return page;
    }
}
