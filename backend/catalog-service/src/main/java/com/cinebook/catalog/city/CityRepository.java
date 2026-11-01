package com.cinebook.catalog.city;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CityRepository extends MongoRepository<City, String> {

  boolean existsByNameIgnoreCase(String name);

  java.util.Optional<City> findByNameIgnoreCase(String name);
}
