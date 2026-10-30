package com.cinebook.common.events;

import java.time.Instant;

/** Topic {@code show.updated}, keyed by {@code showId}. */
public record ShowUpdated(String showId, String change, Instant startsAt) {}
