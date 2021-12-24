package com.leverx.workload.department.exception;

public class DepartmentNotValidException extends RuntimeException {

  public DepartmentNotValidException() {}

  public DepartmentNotValidException(String message) {
    super(message);
  }
}
