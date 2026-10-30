package com.cinebook.common.events;

import java.util.List;

/** Topic {@code booking.cancelled}, keyed by {@code showId}. */
public record BookingCancelled(
    String bookingRef, String showId, String userId, List<String> seats, String reason) {}
