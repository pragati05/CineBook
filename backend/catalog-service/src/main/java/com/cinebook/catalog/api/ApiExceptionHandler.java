package com.cinebook.catalog.api;

import com.cinebook.common.error.ErrorCode;
import com.cinebook.common.error.ErrorResponse;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Maps exceptions to the shared {@link ErrorResponse} format from the common module. */
@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  ResponseEntity<ErrorResponse> notFound(NotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ErrorResponse.of(ErrorCode.NOT_FOUND, e.getMessage(), null));
  }

  @ExceptionHandler(ConflictException.class)
  ResponseEntity<ErrorResponse> conflict(ConflictException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(ErrorResponse.of(ErrorCode.CONFLICT, e.getMessage(), null));
  }

  @ExceptionHandler(BadRequestException.class)
  ResponseEntity<ErrorResponse> badRequest(BadRequestException e) {
    return ResponseEntity.badRequest()
        .body(ErrorResponse.of(ErrorCode.VALIDATION_FAILED, e.getMessage(), null));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<ErrorResponse> invalid(MethodArgumentNotValidException e) {
    List<ErrorResponse.FieldError> details =
        e.getBindingResult().getFieldErrors().stream()
            .map(f -> new ErrorResponse.FieldError(f.getField(), f.getDefaultMessage()))
            .toList();
    return ResponseEntity.badRequest()
        .body(
            new ErrorResponse(
                ErrorCode.VALIDATION_FAILED, "Validation failed", null, Instant.now(), details));
  }
}
