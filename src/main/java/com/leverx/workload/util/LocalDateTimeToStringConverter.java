package com.leverx.workload.util;

import com.fasterxml.jackson.databind.util.StdConverter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeToStringConverter extends StdConverter<LocalDateTime, String> {
  public static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

  @Override
  public String convert(LocalDateTime value) {
    return value.format(DATE_FORMATTER);
  }
}
