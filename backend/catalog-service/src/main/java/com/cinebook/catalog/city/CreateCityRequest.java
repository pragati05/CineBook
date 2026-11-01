package com.cinebook.catalog.city;

import jakarta.validation.constraints.NotBlank;

public record CreateCityRequest(@NotBlank String name) {}
