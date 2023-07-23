package com.devsuperior.movieflix.controllers;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/movies")
public class MovieController {

    @Autowired
    private MovieService service;

    @PreAuthorize("hasAnyRole('VISITOR', 'MEMBER')")
    @GetMapping
    public ResponseEntity<Page<MovieCardDTO>> findAll(
            @RequestParam(defaultValue = "0") Long genreId, Pageable pageable) {
        Page<MovieCardDTO> dtos = service.findAll(genreId, pageable);
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasAnyRole('VISITOR', 'MEMBER')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<MovieDetailsDTO> findById(@PathVariable Long id) {
        MovieDetailsDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{id}/reviews")
    public ResponseEntity<List<ReviewDTO>> getReviews(@PathVariable Long id) {
       List<ReviewDTO> dto = service.findReview(id);
        return ResponseEntity.ok(dto);
    }
}
