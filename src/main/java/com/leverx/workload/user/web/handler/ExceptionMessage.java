package com.leverx.workload.user.web.handler;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.leverx.workload.user.web.util.LocalDateTimeToStringConverter;
import com.leverx.workload.user.web.util.StringToLocalDatetimeConverter;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExceptionMessage {
  private int statusCode;

  @JsonSerialize(converter = LocalDateTimeToStringConverter.class)
  @JsonDeserialize(converter = StringToLocalDatetimeConverter.class)
  private LocalDateTime dateTime;

  private String message;
  private String description;
}
