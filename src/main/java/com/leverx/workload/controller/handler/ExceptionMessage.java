package com.leverx.workload.controller.handler;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExceptionMessage {
  private int statusCode;
  private LocalDateTime dateTime;
  private String message;
  private String description;
}
