package com.leverx.workload.user.web.dto.response;

import lombok.Data;

@Data
public class UserResponse {
  private long id;
  private String firstName;
  private String lastName;
  private String email;
  private String position;
  private String department;
  private String role;
  private boolean active;
}
