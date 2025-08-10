package com.grocery.cartservice.exception;

public class ProductNotFoundInCartException extends RuntimeException {
  public ProductNotFoundInCartException(String message) {
    super(message);
  }
}
