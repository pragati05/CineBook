package com.cinebook.catalog.theatre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/** One row of seats in a screen's layout, e.g. label "A", 10 seats, type "STANDARD". */
public record SeatRow(@NotBlank String label, @Positive int seatCount, @NotBlank String seatType) {}
