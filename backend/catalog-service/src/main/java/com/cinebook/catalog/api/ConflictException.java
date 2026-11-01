package com.cinebook.catalog.api;

public class ConflictException extends RuntimeException {

  public ConflictException(String message) {
    super(message);
  }
}
