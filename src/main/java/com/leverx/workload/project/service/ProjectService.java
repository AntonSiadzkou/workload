package com.leverx.workload.project.service;

import com.leverx.workload.project.repository.entity.ProjectEntity;
import com.leverx.workload.project.web.dto.request.ProjectBodyParams;
import com.leverx.workload.project.web.dto.request.ProjectRequestParams;
import java.util.List;
import javax.validation.constraints.NotNull;

public interface ProjectService {

  List<ProjectEntity> findAllProjects(@NotNull ProjectRequestParams params);

  ProjectEntity findById(@NotNull Long id);

  long createProject(@NotNull ProjectBodyParams params);
}
