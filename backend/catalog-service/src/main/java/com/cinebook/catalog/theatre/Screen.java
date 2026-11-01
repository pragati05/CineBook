package com.cinebook.catalog.theatre;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/** A screen inside a theatre, identified by its name within that theatre. */
public record Screen(@NotBlank String name, @NotEmpty @Valid List<SeatRow> rows) {}
