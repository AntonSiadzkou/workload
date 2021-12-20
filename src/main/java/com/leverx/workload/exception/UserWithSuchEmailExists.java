package com.leverx.workload.exception;

public class UserWithSuchEmailExists extends RuntimeException {

  public UserWithSuchEmailExists() {}

  public UserWithSuchEmailExists(String message) {
    super(message);
  }
}
