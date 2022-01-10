package com.leverx.workload.userproject.web.dto.response;

import lombok.Data;

@Data
public class AssignedUsers {

  private String firstName;
  private String lastName;
  private String position;
  private String department;
  private String assignDate;
  private String cancelDate;
}
