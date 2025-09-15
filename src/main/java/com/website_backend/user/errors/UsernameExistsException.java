package com.website_backend.user.errors;

public class UsernameExistsException extends RuntimeException {

  /**
   * Attempting to create user with same username (email) as existing user
   * @param message
   */
  public UsernameExistsException(String message) {
    super(message);
  }
}
