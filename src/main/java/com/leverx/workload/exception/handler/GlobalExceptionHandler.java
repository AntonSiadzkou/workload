package com.leverx.workload.exception.handler;

import com.leverx.workload.exception.DuplicatedValueException;
import com.leverx.workload.exception.JwtAuthenticationException;
import com.leverx.workload.exception.NotValidEntityException;
import com.opencsv.exceptions.CsvRuntimeException;
import java.time.format.DateTimeParseException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler({EntityNotFoundException.class, UsernameNotFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ExceptionMessage entityNotExistException(HttpServletRequest request, Exception e) {
    log.warn(request.getRequestURL().toString() + " : " + HttpStatus.NOT_FOUND.value() + " : "
        + e.getMessage());
    return new ExceptionMessage(HttpStatus.NOT_FOUND.value(), e.getMessage(),
        request.getRequestURL().toString());
  }

  @ExceptionHandler(DuplicatedValueException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionMessage duplicatedFieldException(HttpServletRequest request, Exception e) {
    log.warn(request.getRequestURL().toString() + " : " + HttpStatus.BAD_REQUEST.value() + " : "
        + e.getMessage());
    return new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(),
        request.getRequestURL().toString());
  }

  @ExceptionHandler(NotValidEntityException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionMessage notValidEntityException(HttpServletRequest request, Exception e) {
    log.warn(request.getRequestURL().toString() + " : " + HttpStatus.BAD_REQUEST.value() + " : "
        + e.getMessage());
    return new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(),
        request.getRequestURL().toString());
  }

  @ExceptionHandler(DateTimeParseException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionMessage wrongFormatException(HttpServletRequest request, Exception e) {
    log.warn(request.getRequestURL().toString() + " : " + HttpStatus.BAD_REQUEST.value()
        + " : Wrong number format, must be the same as 'YYYY-MM-DD'");
    return new ExceptionMessage(HttpStatus.BAD_REQUEST.value(),
        "Wrong number format, must be the same as 'YYYY-MM-DD'",
        request.getRequestURL().toString());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionMessage illegalArgumentException(HttpServletRequest request, Exception e) {
    log.warn(request.getRequestURL().toString() + " : " + HttpStatus.BAD_REQUEST.value()
        + e.getMessage());
    return new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(),
        request.getRequestURL().toString());
  }

  @ExceptionHandler(JwtAuthenticationException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ExceptionMessage authenticationException(HttpServletRequest request, Exception e) {
    log.warn(request.getRequestURL().toString() + " : " + HttpStatus.FORBIDDEN.value() + " : "
        + e.getMessage());
    return new ExceptionMessage(HttpStatus.FORBIDDEN.value(), e.getMessage(),
        request.getRequestURL().toString());
  }

  @ExceptionHandler(CsvRuntimeException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionMessage csvException(HttpServletRequest request, Exception e) {
    log.warn(request.getRequestURL().toString() + " : " + HttpStatus.BAD_REQUEST.value() + " : "
        + e.getMessage());
    return new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(),
        request.getRequestURL().toString());
  }
}
