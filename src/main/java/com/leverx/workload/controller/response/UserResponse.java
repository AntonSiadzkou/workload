package com.leverx.workload.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse extends AbstractResponse {
  private String firstName;
  private String lastName;
  private String email;
  private String position;
  private String department;
  private String role;
  private boolean active;
}
