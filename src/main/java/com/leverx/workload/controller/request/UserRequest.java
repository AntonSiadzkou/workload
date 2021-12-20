package com.leverx.workload.controller.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest extends AbstractRequest {

  @NotBlank(message = "First name is required")
  @Size(min = 3, max = 255,
      message = "First name must be at least 3 characters long, but less than 255 characters")
  private String firstName;

  @NotBlank(message = "Last name is required")
  @Size(min = 3, max = 255,
      message = "Last name must be at least 3 characters long, but less than 255 characters")
  private String lastName;

  @NotBlank(message = "Email is required")
  @Size(min = 4, max = 64,
      message = "Email must be at least 4 characters, but less than 64 characters")
  @Email(message = "Wrong email format")
  private String email;

  @NotBlank(message = "Password is required")
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,64}$",
      message = "Password must contain at least one lowercase character, one uppercase character, one digit, and a length between 6 to 64")
  private String password;

  @NotBlank(message = "Position is required")
  private String position;

  @NotBlank(message = "Department is required")
  private String department;

  @NotBlank(message = "Role is required")
  private String role;

  private boolean active;
}
