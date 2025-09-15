package com.website_backend.errors;

/**
 * Any errors from accessing the database
 */
public class DatabaseException extends Exception {
  public DatabaseException(String message){
    super(message);
  }

}
