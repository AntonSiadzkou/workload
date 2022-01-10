package com.leverx.workload.csv.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CsvResponse {

  private int savedUsers;
  private int invalidUsers;
  private int allLines;

  @Override
  public String toString() {
    return "CsvResponse{" + "saved users from file to database = " + savedUsers
        + "; parsed invalid users from file = " + invalidUsers + "; parsed valid lines from file = "
        + allLines + '}';
  }
}
