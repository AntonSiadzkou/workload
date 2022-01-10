package com.leverx.workload.csv.service.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class CsvUser {

  @CsvBindByName(required = true)
  private String firstName;

  @CsvBindByName(required = true)
  private String lastName;

  @CsvBindByName(required = true)
  private String email;

  @CsvBindByName(required = true)
  private String password;

  @CsvBindByName(required = true)
  private String position;

  @CsvBindByName(required = true)
  private long department;

  @CsvBindByName(required = true)
  private String role;

  @CsvBindByName(required = true)
  private Boolean active;
}
