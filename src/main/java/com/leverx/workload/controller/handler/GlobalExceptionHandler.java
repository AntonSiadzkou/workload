package com.leverx.workload.controller.handler;

import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ExceptionMessage unexpectedException(HttpServletRequest request, Exception e) {
    return new ExceptionMessage(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        LocalDateTime.now(),
        e.getMessage(),
        request.getRequestURL().toString());
  }
}
