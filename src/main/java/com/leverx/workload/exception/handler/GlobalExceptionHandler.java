package com.leverx.workload.exception.handler;

import com.leverx.workload.exception.DuplicatedValueException;
import com.leverx.workload.exception.NotValidEntityException;
import java.time.format.DateTimeParseException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ExceptionMessage entityNotExistException(HttpServletRequest request, Exception e) {
    return new ExceptionMessage(HttpStatus.NOT_FOUND.value(), e.getMessage(),
        request.getRequestURL().toString());
  }

  @ExceptionHandler(DuplicatedValueException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionMessage duplicatedFieldException(HttpServletRequest request, Exception e) {
    return new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(),
        request.getRequestURL().toString());
  }

  @ExceptionHandler(NotValidEntityException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionMessage notValidEntityException(HttpServletRequest request, Exception e) {
    return new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(),
        request.getRequestURL().toString());
  }

  @ExceptionHandler(DateTimeParseException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionMessage wrongFormatException(HttpServletRequest request, Exception e) {
    return new ExceptionMessage(HttpStatus.BAD_REQUEST.value(),
        "Wrong number format, must be the same as 'YYYY-MM-DD'",
        request.getRequestURL().toString());
  }
}
