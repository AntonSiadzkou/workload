package com.leverx.workload.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class BindingResultErrorsParser {
  private BindingResultErrorsParser() {
    throw new UnsupportedOperationException("Forbidden to create an instance.");
  }

  public static String parseErrors(BindingResult result) {
    StringBuilder builder = new StringBuilder();
    for (FieldError error : result.getFieldErrors()) {
      builder.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
    }
    return builder.toString();
  }
}
