package com.cinebook.common.events;

/** Topic {@code ticket.generated}, keyed by {@code bookingRef}. */
public record TicketGenerated(String ticketId, String bookingRef, String showId, String userId) {}
