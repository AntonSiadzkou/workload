package com.leverx.workload.department.exception;

public class DepartmentNotExistException extends RuntimeException {

  public DepartmentNotExistException() {}

  public DepartmentNotExistException(String message) {
    super(message);
  }
}
