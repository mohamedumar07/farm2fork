package com.grocery.productservice.exception;

public class EmptyFileException extends RuntimeException {
  public EmptyFileException(String message) {
    super(message);
  }
}
