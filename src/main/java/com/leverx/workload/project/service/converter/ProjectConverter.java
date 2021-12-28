package com.leverx.workload.project.service.converter;

import com.leverx.workload.project.repository.entity.ProjectEntity;
import com.leverx.workload.project.web.dto.request.ProjectBodyParams;
import com.leverx.workload.project.web.dto.responce.ProjectResponse;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProjectConverter {

  private final ModelMapper mapper;

  public ProjectEntity toEntity(ProjectBodyParams request) {
    return Objects.isNull(request) ? null : mapper.map(request, ProjectEntity.class);
  }

  public ProjectResponse toResponse(ProjectEntity entity) {
    return Objects.isNull(entity) ? null : mapper.map(entity, ProjectResponse.class);
  }
}
