package com.cinebook.catalog.show;

import com.cinebook.catalog.movie.Movie;
import com.cinebook.catalog.theatre.Theatre;
import java.util.List;

/** Response for {@code GET /cities/{id}/shows}: shows grouped by movie, then by theatre. */
public record MovieShows(Movie movie, List<TheatreShows> theatres) {

  public record TheatreShows(Theatre theatre, List<Show> shows) {}
}
