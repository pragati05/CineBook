package com.cinebook.catalog.theatre;

import com.cinebook.catalog.api.NotFoundException;
import com.cinebook.catalog.city.CityRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TheatreService {

  private final TheatreRepository repo;
  private final CityRepository cities;

  public TheatreService(TheatreRepository repo, CityRepository cities) {
    this.repo = repo;
    this.cities = cities;
  }

  Theatre create(CreateTheatreRequest request) {
    if (!cities.existsById(request.cityId())) {
      throw new NotFoundException("City not found: " + request.cityId());
    }
    return repo.save(
        new Theatre(null, request.cityId(), request.name(), request.address(), request.screens()));
  }

  List<Theatre> list(String cityId) {
    return cityId == null ? repo.findAll() : repo.findByCityId(cityId);
  }

  Theatre get(String id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Theatre not found: " + id));
  }
}
