package com.cinebook.catalog.city;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityService {
  @Autowired private final CityRepository repo;

  public CityService(CityRepository repo) {
    this.repo = repo;
  }

  List<City> getAllCities() {
    return repo.findAll();
  }

  City getCityById(String id) {
    return repo.findById(id).get();
  }

  City create(CreateCityRequest request) {
    if (!repo.existsByNameIgnoreCase(request.name())) {
      City c = new City(request.name());
      repo.save(c);
      return c;
    } else {

      throw new RuntimeException("City Already Exists");
    }
  }
}
