package com.leverx.workload.userproject.service.converter;

import com.leverx.workload.userproject.repository.entity.UserProjectEntity;
import com.leverx.workload.userproject.web.dto.response.AssignedProjects;
import com.leverx.workload.userproject.web.dto.response.AssignedUsers;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserProjectConverter {

  private final ModelMapper mapper;

  public AssignedProjects toAssignedProjects(UserProjectEntity entity) {
    mapper.typeMap(UserProjectEntity.class, AssignedProjects.class).addMappings(mapper -> mapper
        .map(src -> src.getId().getProject().getName(), AssignedProjects::setProjectName));
    return Objects.isNull(entity) ? null : mapper.map(entity, AssignedProjects.class);
  }

  public AssignedUsers toAssignedUsers(UserProjectEntity entity) {
    mapper.typeMap(UserProjectEntity.class, AssignedUsers.class)
        .addMappings(mapper -> mapper.map(src -> src.getId().getUser().getFirstName(),
            AssignedUsers::setFirstName))
        .addMappings(mapper -> mapper.map(src -> src.getId().getUser().getLastName(),
            AssignedUsers::setLastName))
        .addMappings(mapper -> mapper.map(src -> src.getId().getUser().getPosition(),
            AssignedUsers::setPosition))
        .addMappings(mapper -> mapper.map(src -> src.getId().getUser().getDepartment().getTitle(),
            AssignedUsers::setDepartment));
    return Objects.isNull(entity) ? null : mapper.map(entity, AssignedUsers.class);
  }
}
