package com.cinebook.catalog.show;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShowController {

  private final ShowService service;

  public ShowController(ShowService service) {
    this.service = service;
  }

  @PostMapping("/shows")
  ResponseEntity<Show> create(@RequestBody @Valid CreateShowRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
  }

  @GetMapping("/shows/{id}")
  Show get(@PathVariable String id) {
    return service.get(id);
  }

  @GetMapping("/cities/{cityId}/shows")
  List<MovieShows> showsForCity(
      @PathVariable String cityId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return service.showsForCity(cityId, date);
  }
}
