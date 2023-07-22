package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repository;


    @Transactional(readOnly = true)
    public Page<MovieCardDTO> findAll(Long genreId,Pageable pageable) {
        List<MovieCardDTO> dtos= new ArrayList<>();
        if(genreId.equals(0L)) {
             dtos = repository.findAll().
                    stream().sorted((m, a) -> m.getTitle().compareTo(a.getTitle())).map(x -> new MovieCardDTO(x))
                    .toList();
        }else {
            dtos=repository.getMoviesByGenre_Id(genreId)
                    .stream().sorted((m, a) -> m.getTitle().compareTo(a.getTitle()))
                    .map(x -> new MovieCardDTO(x))
                    .toList();
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());
        List<MovieCardDTO> pageList = dtos.subList(start, end);

        Page<MovieCardDTO> page = new PageImpl<>(pageList, pageable, dtos.size());
        return page;
    }

    @Transactional(readOnly = true)
    public MovieDetailsDTO findById(Long id) {
        Optional<Movie > entity= repository.findById(id);
        if (entity.isEmpty()) throw new ResourceNotFoundException("Resource not found");
        return new MovieDetailsDTO(entity.get());
    }

}
