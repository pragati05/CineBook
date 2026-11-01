package com.cinebook.catalog.show;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.cinebook.catalog.api.BadRequestException;
import com.cinebook.catalog.api.NotFoundException;
import com.cinebook.catalog.city.CityRepository;
import com.cinebook.catalog.movie.Movie;
import com.cinebook.catalog.movie.MovieRepository;
import com.cinebook.catalog.show.MovieShows.TheatreShows;
import com.cinebook.catalog.theatre.Theatre;
import com.cinebook.catalog.theatre.TheatreRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class ShowService {

  /** POC assumes every city is in this zone; add a timezone to City when that changes. */
  static final ZoneId ZONE = ZoneId.of("Asia/Kolkata");

  private final ShowRepository shows;
  private final MovieRepository movies;
  private final TheatreRepository theatres;
  private final CityRepository cities;

  public ShowService(
      ShowRepository shows,
      MovieRepository movies,
      TheatreRepository theatres,
      CityRepository cities) {
    this.shows = shows;
    this.movies = movies;
    this.theatres = theatres;
    this.cities = cities;
  }

  Show create(CreateShowRequest request) {
    if (!movies.existsById(request.movieId())) {
      throw new NotFoundException("Movie not found: " + request.movieId());
    }
    Theatre theatre =
        theatres
            .findById(request.theatreId())
            .orElseThrow(() -> new NotFoundException("Theatre not found: " + request.theatreId()));
    if (theatre.screens().stream().noneMatch(s -> s.name().equals(request.screenName()))) {
      throw new BadRequestException(
          "Theatre " + theatre.name() + " has no screen " + request.screenName());
    }
    return shows.save(
        new Show(
            null,
            request.movieId(),
            theatre.id(),
            request.screenName(),
            theatre.cityId(),
            request.startsAt(),
            request.price()));
  }

  Show get(String id) {
    return shows.findById(id).orElseThrow(() -> new NotFoundException("Show not found: " + id));
  }

  List<MovieShows> showsForCity(String cityId, LocalDate date) {
    if (!cities.existsById(cityId)) {
      throw new NotFoundException("City not found: " + cityId);
    }
    Instant from = date.atStartOfDay(ZONE).toInstant();
    Instant to = date.plusDays(1).atStartOfDay(ZONE).toInstant();
    List<Show> found = shows.findByCityIdAndStartsAtBetweenOrderByStartsAtAsc(cityId, from, to);

    Map<String, Movie> movieById =
        movies.findAllById(found.stream().map(Show::movieId).distinct().toList()).stream()
            .collect(toMap(Movie::getId, Function.identity()));
    Map<String, Theatre> theatreById =
        theatres.findAllById(found.stream().map(Show::theatreId).distinct().toList()).stream()
            .collect(toMap(Theatre::id, Function.identity()));

    Map<String, Map<String, List<Show>>> grouped =
        found.stream()
            .collect(
                groupingBy(
                    Show::movieId,
                    LinkedHashMap::new,
                    groupingBy(Show::theatreId, LinkedHashMap::new, toList())));

    return grouped.entrySet().stream()
        .map(
            byMovie ->
                new MovieShows(
                    movieById.get(byMovie.getKey()),
                    byMovie.getValue().entrySet().stream()
                        .map(
                            byTheatre ->
                                new TheatreShows(
                                    theatreById.get(byTheatre.getKey()), byTheatre.getValue()))
                        .toList()))
        .toList();
  }
}
