package com.leverx.workload.user.exception;

public class DuplicatedEmailException extends RuntimeException {

  public DuplicatedEmailException() {}

  public DuplicatedEmailException(String message) {
    super(message);
  }
}
