package com.cinebook.catalog.theatre;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TheatreRepository extends MongoRepository<Theatre, String> {

  List<Theatre> findByCityId(String cityId);

  Optional<Theatre> findByName(String name);
}
