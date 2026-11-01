package com.cinebook.catalog.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateMovieRequest(
    @NotBlank String title,
    @Positive int durationMins,
    @NotBlank String language,
    @NotBlank String posterUrl) {}
