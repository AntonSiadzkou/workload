package com.leverx.workload.user.web.handler;

import com.leverx.workload.user.exception.DuplicatedEmailException;
import com.leverx.workload.user.exception.NotValidUserException;
import com.leverx.workload.user.exception.UserNotExistException;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserNotExistException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ExceptionMessage userNotExistException(HttpServletRequest request, Exception e) {
    return new ExceptionMessage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), e.getMessage(),
        request.getRequestURL().toString());
  }

  @ExceptionHandler({DuplicatedEmailException.class,})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionMessage userWithSuchEmailExistsException(HttpServletRequest request,
      Exception e) {
    return new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), e.getMessage(),
        request.getRequestURL().toString());
  }

  @ExceptionHandler(NotValidUserException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionMessage userNotValidException(HttpServletRequest request, Exception e) {
    return new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), e.getMessage(),
        request.getRequestURL().toString());
  }
}
