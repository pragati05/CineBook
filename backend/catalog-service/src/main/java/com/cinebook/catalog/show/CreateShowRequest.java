package com.cinebook.catalog.show;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Instant;

public record CreateShowRequest(
    @NotBlank String movieId,
    @NotBlank String theatreId,
    @NotBlank String screenName,
    @NotNull Instant startsAt,
    @NotNull @Positive BigDecimal price) {}
