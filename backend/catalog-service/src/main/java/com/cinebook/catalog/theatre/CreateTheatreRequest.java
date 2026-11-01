package com.cinebook.catalog.theatre;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CreateTheatreRequest(
    @NotBlank String cityId,
    @NotBlank String name,
    String address,
    @NotEmpty @Valid List<Screen> screens) {}
