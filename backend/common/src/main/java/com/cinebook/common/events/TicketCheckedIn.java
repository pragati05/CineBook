package com.cinebook.common.events;

import java.time.Instant;

/** Topic {@code ticket.checked-in}, keyed by {@code showId}. */
public record TicketCheckedIn(
    String ticketId, String bookingRef, String showId, Instant checkedInAt) {}
