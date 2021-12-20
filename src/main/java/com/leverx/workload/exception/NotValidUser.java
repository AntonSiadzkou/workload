package com.leverx.workload.exception;

public class NotValidUser extends RuntimeException {

  public NotValidUser() {}

  public NotValidUser(String message) {
    super(message);
  }
}
