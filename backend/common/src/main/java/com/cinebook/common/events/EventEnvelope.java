package com.cinebook.common.events;

import java.time.Instant;
import java.util.UUID;

/** Wrapper carried by every Kafka event; consumers dedupe on {@code eventId}. */
public record EventEnvelope<T>(UUID eventId, String type, Instant occurredAt, T payload) {

  public static <T> EventEnvelope<T> of(String type, T payload) {
    return new EventEnvelope<>(UUID.randomUUID(), type, Instant.now(), payload);
  }
}
