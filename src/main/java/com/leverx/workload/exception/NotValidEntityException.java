package com.leverx.workload.exception;

public class NotValidEntityException extends RuntimeException {

  public NotValidEntityException() {}

  public NotValidEntityException(String message) {
    super(message);
  }
}
