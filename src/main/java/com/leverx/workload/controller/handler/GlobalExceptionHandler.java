package com.leverx.workload.controller.handler;

import com.leverx.workload.exception.NotValidUser;
import com.leverx.workload.exception.UserNotExistException;
import com.leverx.workload.exception.UserWithSuchEmailExists;
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
    return new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now(),
        e.getMessage(), request.getRequestURL().toString());
  }

  @ExceptionHandler(UserNotExistException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ExceptionMessage userNotExistException(HttpServletRequest request, Exception e) {
    return new ExceptionMessage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), e.getMessage(),
        request.getRequestURL().toString());
  }

  @ExceptionHandler({UserWithSuchEmailExists.class, NotValidUser.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionMessage UserWithSuchEmailExistsException(HttpServletRequest request,
      Exception e) {
    return new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), e.getMessage(),
        request.getRequestURL().toString());
  }
}
