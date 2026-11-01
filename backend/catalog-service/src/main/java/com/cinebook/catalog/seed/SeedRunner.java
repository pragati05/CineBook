package com.cinebook.catalog.seed;

import com.cinebook.catalog.city.City;
import com.cinebook.catalog.city.CityRepository;
import com.cinebook.catalog.movie.Movie;
import com.cinebook.catalog.movie.MovieRepository;
import com.cinebook.catalog.show.Show;
import com.cinebook.catalog.show.ShowRepository;
import com.cinebook.catalog.theatre.Screen;
import com.cinebook.catalog.theatre.SeatRow;
import com.cinebook.catalog.theatre.Theatre;
import com.cinebook.catalog.theatre.TheatreRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Local-only demo data: 1 city, 1 theatre with 2 screens, 6 movies and a week of shows. Safe to run
 * on every start: anything that already exists is reused, and shows are only created together with
 * a new theatre.
 */
@Component
@Profile("local")
public class SeedRunner implements ApplicationRunner {

  private static final Logger log = LoggerFactory.getLogger(SeedRunner.class);
  private static final ZoneId ZONE = ZoneId.of("Asia/Kolkata");
  private static final List<LocalTime> SLOTS =
      List.of(LocalTime.of(10, 0), LocalTime.of(14, 0), LocalTime.of(18, 0));

  private final CityRepository cities;
  private final TheatreRepository theatres;
  private final MovieRepository movies;
  private final ShowRepository shows;

  public SeedRunner(
      CityRepository cities,
      TheatreRepository theatres,
      MovieRepository movies,
      ShowRepository shows) {
    this.cities = cities;
    this.theatres = theatres;
    this.movies = movies;
    this.shows = shows;
  }

  @Override
  public void run(ApplicationArguments args) {
    City city =
        cities
            .findByNameIgnoreCase("Hyderabad")
            .orElseGet(() -> cities.save(new City("Hyderabad")));
    List<Movie> allMovies = seedMovies();

    if (theatres.findByName("CineBook Central").isPresent()) {
      log.info("Seed: theatre already exists, nothing more to do");
      return;
    }
    Theatre theatre =
        theatres.save(
            new Theatre(
                null,
                city.getId(),
                "CineBook Central",
                "Banjara Hills, Hyderabad",
                List.of(
                    new Screen(
                        "Screen 1",
                        List.of(
                            new SeatRow("A", 8, "PREMIUM"),
                            new SeatRow("B", 8, "PREMIUM"),
                            new SeatRow("C", 10, "STANDARD"),
                            new SeatRow("D", 10, "STANDARD"),
                            new SeatRow("E", 10, "STANDARD"))),
                    new Screen(
                        "Screen 2",
                        List.of(
                            new SeatRow("A", 8, "STANDARD"),
                            new SeatRow("B", 8, "STANDARD"),
                            new SeatRow("C", 8, "STANDARD"),
                            new SeatRow("D", 8, "STANDARD"))))));

    List<Show> week = new ArrayList<>();
    LocalDate today = LocalDate.now(ZONE);
    for (int day = 0; day < 7; day++) {
      for (int screen = 0; screen < theatre.screens().size(); screen++) {
        for (int slot = 0; slot < SLOTS.size(); slot++) {
          Movie movie = allMovies.get((day + screen * 3 + slot) % allMovies.size());
          week.add(
              new Show(
                  null,
                  movie.getId(),
                  theatre.id(),
                  theatre.screens().get(screen).name(),
                  city.getId(),
                  ZonedDateTime.of(today.plusDays(day), SLOTS.get(slot), ZONE).toInstant(),
                  BigDecimal.valueOf(250)));
        }
      }
    }
    shows.saveAll(week);
    log.info("Seed: created theatre {} and {} shows", theatre.name(), week.size());
  }

  private List<Movie> seedMovies() {
    record Spec(String title, int mins, String language) {}
    List<Spec> specs =
        List.of(
            new Spec("Jawan", 169, "Hindi"),
            new Spec("Pushpa 2", 200, "Telugu"),
            new Spec("Kalki 2898 AD", 181, "Telugu"),
            new Spec("RRR", 187, "Telugu"),
            new Spec("Animal", 201, "Hindi"),
            new Spec("Leo", 164, "Tamil"));
    return specs.stream()
        .map(
            s ->
                movies
                    .findByTitle(s.title())
                    .orElseGet(
                        () ->
                            movies.save(
                                new Movie(
                                    s.title(),
                                    s.mins(),
                                    s.language(),
                                    "https://placehold.co/300x450?text="
                                        + s.title().replace(' ', '+')))))
        .toList();
  }
}
