package com.website_backend.orders.errors;

public class ProductIdNotExistException extends RuntimeException {

  public ProductIdNotExistException(String message) {
    super(message);
  }
}
