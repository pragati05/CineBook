package com.cinebook.catalog.show;

import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** A screening. {@code cityId} is copied from the theatre so city queries need no join. */
@Document("shows")
public record Show(
    @Id String id,
    String movieId,
    String theatreId,
    String screenName,
    String cityId,
    Instant startsAt,
    BigDecimal price) {}
