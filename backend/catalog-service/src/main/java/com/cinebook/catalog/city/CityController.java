package com.cinebook.catalog.city;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cities")
public class CityController {
  @Autowired CityService service;

  @PostMapping()
  ResponseEntity createCityRequest(@RequestBody @Valid CreateCityRequest cityRequest) {
    service.create(cityRequest);
    return new ResponseEntity<City>(HttpStatus.CREATED);
  }

  @GetMapping()
  ResponseEntity<List<City>> getAllCities() {
    List<City> cities = service.getAllCities();
    return new ResponseEntity<>(cities, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  ResponseEntity<City> getCity(@PathVariable String id) {
    City city = service.getCityById(id);
    return new ResponseEntity<>(city, HttpStatus.OK);
  }
}
