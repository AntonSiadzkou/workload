package com.leverx.workload.project.web.dto.responce;

import lombok.Data;

@Data
public class ProjectResponse {

  private long id;
  private String name;
  private String startDate;
  private String endDate;
}
