package com.cinebook.common;

import static org.assertj.core.api.Assertions.assertThat;

import com.cinebook.common.error.ErrorCode;
import com.cinebook.common.error.ErrorResponse;
import com.cinebook.common.events.BookingExpired;
import com.cinebook.common.events.EventEnvelope;
import com.cinebook.common.events.Topics;
import java.util.List;
import org.junit.jupiter.api.Test;

class CommonModelTest {

  @Test
  void envelopeGetsUniqueIdAndTimestamp() {
    var payload = new BookingExpired("B1", "S1", List.of("A1"));
    var a = EventEnvelope.of(Topics.BOOKING_EXPIRED, payload);
    var b = EventEnvelope.of(Topics.BOOKING_EXPIRED, payload);

    assertThat(a.eventId()).isNotEqualTo(b.eventId());
    assertThat(a.occurredAt()).isNotNull();
    assertThat(a.payload()).isEqualTo(payload);
  }

  @Test
  void dlqTopicSuffix() {
    assertThat(Topics.dlq(Topics.BOOKING_CREATED)).isEqualTo("booking.created.dlq");
  }

  @Test
  void errorResponseDefaults() {
    var e = ErrorResponse.of(ErrorCode.NOT_FOUND, "no such show", "c-1");

    assertThat(e.code()).isEqualTo(ErrorCode.NOT_FOUND);
    assertThat(e.details()).isEmpty();
    assertThat(e.timestamp()).isNotNull();
  }
}
