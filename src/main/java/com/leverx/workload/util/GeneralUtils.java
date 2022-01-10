package com.leverx.workload.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.workload.exception.NotValidEntityException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class GeneralUtils {

  private GeneralUtils() {
    throw new UnsupportedOperationException("Forbidden to create an instance");
  }

  public static void checkViolations(BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      StringBuilder errors = new StringBuilder();
      for (FieldError violation : bindingResult.getFieldErrors()) {
        errors.append("['").append(violation.getField()).append("': '")
            .append(violation.getRejectedValue()).append("': '")
            .append(violation.getDefaultMessage()).append("']; ");
      }
      throw new NotValidEntityException("Request has not valid fields. " + errors);
    }
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
