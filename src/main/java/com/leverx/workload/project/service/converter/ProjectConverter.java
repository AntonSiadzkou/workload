package com.leverx.workload.project.service.converter;

import com.leverx.workload.project.repository.entity.ProjectEntity;
import com.leverx.workload.project.web.dto.request.ProjectBodyParams;
import com.leverx.workload.project.web.dto.responce.ProjectResponse;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProjectConverter {

  private final ModelMapper mapper;

  public ProjectEntity toEntity(ProjectBodyParams request) {
    mapper.typeMap(ProjectBodyParams.class, ProjectEntity.class)
        .addMappings(mapper -> mapper.skip(ProjectEntity::setStartDate))
        .addMappings(mapper -> mapper.skip(ProjectEntity::setEndDate))
        .setPostConverter(toEntityConverter());
    return Objects.isNull(request) ? null : mapper.map(request, ProjectEntity.class);
  }

  public ProjectResponse toResponse(ProjectEntity entity) {
    return Objects.isNull(entity) ? null : mapper.map(entity, ProjectResponse.class);
  }

  public Converter<ProjectBodyParams, ProjectEntity> toEntityConverter() {
    return context -> {
      ProjectBodyParams source = context.getSource();
      ProjectEntity destination = context.getDestination();
      destination
          .setStartDate((Objects.isNull(source) || Objects.isNull(source.getStartDate())) ? null
              : LocalDate.parse(source.getStartDate()));
      destination.setEndDate((Objects.isNull(source) || Objects.isNull(source.getEndDate())) ? null
          : LocalDate.parse(source.getEndDate()));
      return context.getDestination();
    };
  }
}
