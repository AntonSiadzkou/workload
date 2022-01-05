package com.leverx.workload.department.exception;

public class DuplicatedTitleException extends RuntimeException {

  public DuplicatedTitleException() {}

  public DuplicatedTitleException(String message) {
    super(message);
  }
}
