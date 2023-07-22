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

@Service
public class MovieService {

    @Autowired
    private MovieRepository repository;


    @Transactional(readOnly = true)
    public Page<MovieCardDTO> findAll(Long genreId, Pageable pageable) {
        List<MovieCardDTO> dtos;
        if (genreId.equals(0L)) {
            List<Movie> toSort = new ArrayList<>();
            for (Movie x : repository.findAll()) {
                toSort.add(x);
            }
            toSort.sort((m, a) -> m.getTitle().compareTo(a.getTitle()));
            List<MovieCardDTO> list = new ArrayList<>();
            for (Movie x : toSort) {
                MovieCardDTO movieCardDTO = new MovieCardDTO(x);
                list.add(movieCardDTO);
            }
            dtos = list;
        } else {
            dtos = repository.getMoviesByGenre_Id(genreId)
                    .stream().sorted((m, a) -> m.getTitle().compareTo(a.getTitle()))
                    .map(MovieCardDTO::new)
                    .toList();
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());
        List<MovieCardDTO> pageList = dtos.subList(start, end);

        return new PageImpl<>(pageList, pageable, dtos.size());
    }

    @Transactional(readOnly = true)
    public MovieDetailsDTO findById(Long id) {
        return repository.findById(id).map(MovieDetailsDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Resource no found"));
    }

}
