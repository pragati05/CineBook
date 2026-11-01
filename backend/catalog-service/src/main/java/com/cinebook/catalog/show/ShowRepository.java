package com.cinebook.catalog.show;

import java.time.Instant;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShowRepository extends MongoRepository<Show, String> {

  List<Show> findByCityIdAndStartsAtBetweenOrderByStartsAtAsc(
      String cityId, Instant from, Instant to);
}
