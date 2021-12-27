package com.leverx.workload.handler;

import com.leverx.workload.department.exception.DepartmentNotExistException;
import com.leverx.workload.department.exception.DepartmentNotValidException;
import com.leverx.workload.user.exception.DuplicatedEmailException;
import com.leverx.workload.user.exception.NotValidUserException;
import com.leverx.workload.user.exception.UserNotExistException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({UserNotExistException.class, DepartmentNotExistException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ExceptionMessage entityNotExistException(HttpServletRequest request, Exception e) {
    return new ExceptionMessage(HttpStatus.NOT_FOUND.value(), e.getMessage(),
        request.getRequestURL().toString());
  }

  @ExceptionHandler({DuplicatedEmailException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionMessage duplicatedFieldException(HttpServletRequest request, Exception e) {
    return new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(),
        request.getRequestURL().toString());
  }

  @ExceptionHandler({NotValidUserException.class, DepartmentNotValidException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionMessage notValidEntityException(HttpServletRequest request, Exception e) {
    return new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(),
        request.getRequestURL().toString());
  }
}
