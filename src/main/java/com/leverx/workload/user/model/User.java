package com.leverx.workload.user.model;

import lombok.Data;

@Data
public class User {
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
