package com.leverx.workload.exception;

public class DuplicatedValueException extends RuntimeException {

  public DuplicatedValueException() {}

  public DuplicatedValueException(String message) {
    super(message);
  }
}
