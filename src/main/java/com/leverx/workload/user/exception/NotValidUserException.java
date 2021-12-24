package com.leverx.workload.user.exception;

public class NotValidUserException extends RuntimeException {

  public NotValidUserException() {}

  public NotValidUserException(String message) {
    super(message);
  }
}
