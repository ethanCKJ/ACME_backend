package com.website_backend.orders.errors;

public class CustomerIdNotExistException extends RuntimeException {

  public CustomerIdNotExistException(String message) {
    super(message);
  }
}
