package com.leverx.workload.userproject.web.dto.response;

import lombok.Data;

@Data
public class ProjectWithoutUsers {

  private String projectName;
  private String assignDate;
  private String cancelDate;
}
