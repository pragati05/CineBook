package com.cinebook.common.error;

import java.time.Instant;
import java.util.List;

/** JSON body returned by every service on a non-2xx response. */
public record ErrorResponse(
    ErrorCode code,
    String message,
    String correlationId,
    Instant timestamp,
    List<FieldError> details) {

  public record FieldError(String field, String message) {}

  public static ErrorResponse of(ErrorCode code, String message, String correlationId) {
    return new ErrorResponse(code, message, correlationId, Instant.now(), List.of());
  }
}
