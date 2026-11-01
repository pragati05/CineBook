package com.cinebook.catalog.api;

import com.cinebook.common.error.ErrorCode;
import com.cinebook.common.error.ErrorResponse;
import java.time.Instant;
import java.util.List;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CatalogException {

  @ExceptionHandler(ResourceNotFoundException.class)
  ResponseEntity<ErrorResponse> NotFoundException(ResourceNotFoundException ex) {
    ErrorResponse er =
        new ErrorResponse(ErrorCode.NOT_FOUND, ex.getMessage(), "", Instant.now(), List.of());
    return new ResponseEntity<>(er, HttpStatus.NOT_FOUND);
  }
}
