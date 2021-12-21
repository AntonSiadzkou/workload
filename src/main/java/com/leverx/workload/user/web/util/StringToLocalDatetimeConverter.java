package com.leverx.workload.user.web.util;

import com.fasterxml.jackson.databind.util.StdConverter;
import java.time.LocalDateTime;

public class StringToLocalDatetimeConverter extends StdConverter<String, LocalDateTime> {

  @Override
  public LocalDateTime convert(String value) {
    return LocalDateTime.parse(value, LocalDateTimeToStringConverter.DATE_FORMATTER);
  }
}
