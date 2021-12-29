package com.leverx.workload.userproject.service.converter;

import com.leverx.workload.userproject.repository.entity.UserProjectEntity;
import com.leverx.workload.userproject.web.dto.response.ProjectWithoutUsers;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserProjectConverter {

  private final ModelMapper mapper;

  public ProjectWithoutUsers toProjectWithoutUsers(UserProjectEntity entity) {
    mapper.typeMap(UserProjectEntity.class, ProjectWithoutUsers.class).addMappings(mapper -> mapper
        .map(src -> src.getId().getProject().getName(), ProjectWithoutUsers::setProjectName));
    return Objects.isNull(entity) ? null : mapper.map(entity, ProjectWithoutUsers.class);
  }
}
