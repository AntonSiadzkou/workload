package com.leverx.workload.user.web.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExceptionMessage {
  private int statusCode;
  private String message;
  private String description;
}
