package com.website_backend.orders.errors;

/**
 * Exception raised when ordering a larger quantity of a product than existent stock of the product.
 */
public class InsufficientStockException extends Exception{
  public InsufficientStockException(String message){
    super(message);
  }
}

