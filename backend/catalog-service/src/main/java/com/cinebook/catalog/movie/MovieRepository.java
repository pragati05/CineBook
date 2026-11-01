package com.cinebook.catalog.movie;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<Movie, String> {

  java.util.Optional<Movie> findByTitle(String title);
}
