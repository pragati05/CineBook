package com.cinebook.common.events;

/** Kafka topic names. Failed messages go to {@code <topic>.dlq}. */
public final class Topics {

  public static final String BOOKING_CREATED = "booking.created";
  public static final String BOOKING_CANCELLED = "booking.cancelled";
  public static final String BOOKING_EXPIRED = "booking.expired";
  public static final String TICKET_GENERATED = "ticket.generated";
  public static final String TICKET_CHECKED_IN = "ticket.checked-in";
  public static final String SHOW_UPDATED = "show.updated";

  private Topics() {}

  public static String dlq(String topic) {
    return topic + ".dlq";
  }
}
