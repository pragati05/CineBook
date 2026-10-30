package com.cinebook.common.events;

import java.util.List;

/** Topic {@code booking.expired}, keyed by {@code showId}; emitted when a seat hold lapses. */
public record BookingExpired(String bookingRef, String showId, List<String> seats) {}
