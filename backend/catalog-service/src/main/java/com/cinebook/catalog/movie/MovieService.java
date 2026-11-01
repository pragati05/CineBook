package com.cinebook.catalog.movie;

import com.cinebook.catalog.api.NotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

  private final MovieRepository repo;

  public MovieService(MovieRepository repo) {
    this.repo = repo;
  }

  Movie createMovie(CreateMovieRequest request) {
    return repo.save(
        new Movie(
            request.title(), request.durationMins(), request.language(), request.posterUrl()));
  }

  List<Movie> getAllMovies() {
    return repo.findAll();
  }

  Movie getMovieById(String id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Movie not found: " + id));
  }
}
