package com.cinebook.catalog.theatre;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/theatres")
public class TheatreController {

  private final TheatreService service;

  public TheatreController(TheatreService service) {
    this.service = service;
  }

  @PostMapping
  ResponseEntity<Theatre> create(@RequestBody @Valid CreateTheatreRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
  }

  @GetMapping
  List<Theatre> list(@RequestParam(required = false) String cityId) {
    return service.list(cityId);
  }

  @GetMapping("/{id}")
  Theatre get(@PathVariable String id) {
    return service.get(id);
  }
}
