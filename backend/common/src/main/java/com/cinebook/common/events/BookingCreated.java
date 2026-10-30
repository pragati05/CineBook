package com.cinebook.common.events;

import java.math.BigDecimal;
import java.util.List;

/** Topic {@code booking.created}, keyed by {@code showId}. */
public record BookingCreated(
    String bookingRef, String showId, String userId, List<String> seats, BigDecimal totalAmount) {}
