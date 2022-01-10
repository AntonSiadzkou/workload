package com.leverx.workload.project.web.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectBodyParams {

  private long id;

  @NotBlank(message = "Name is required")
  private String name;

  @NotNull
  private String startDate;

  @NotNull
  private String endDate;
}
