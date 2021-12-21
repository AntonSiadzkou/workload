package com.leverx.workload.user.web.dto.request;

import lombok.Data;

@Data
public class UserBodyParams {
  private long id;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String position;
  private String department;
  private String role;
  private boolean active;
}
