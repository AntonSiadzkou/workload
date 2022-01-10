package com.leverx.workload.userproject.web.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserProjectBodyParams {

  @NotNull
  private Long projectId;
  @NotNull
  private Long userId;
  @NotNull
  private String assignDate;
  @NotNull
  private String cancelDate;
}
