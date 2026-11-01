package com.cinebook.catalog.movie;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieController {

  private final MovieService service;

  public MovieController(MovieService service) {
    this.service = service;
  }

  @GetMapping
  List<Movie> getAllMovies() {
    return service.getAllMovies();
  }

  @GetMapping("/{id}")
  Movie getMovieById(@PathVariable String id) {
    return service.getMovieById(id);
  }

  @PostMapping
  ResponseEntity<Movie> createMovie(@RequestBody @Valid CreateMovieRequest request) {
    Movie movie = service.createMovie(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(movie);
  }
}
