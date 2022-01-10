package com.leverx.workload.department.web.dto.request;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepartmentBodyParams {
  private long id;

  @Column(name = "title", nullable = false, unique = true)
  @NotBlank(message = "Title is required")
  private String title;
}
