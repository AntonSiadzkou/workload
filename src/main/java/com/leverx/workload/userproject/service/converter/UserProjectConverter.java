package com.leverx.workload.userproject.service.converter;

import com.leverx.workload.userproject.repository.entity.UserProjectEntity;
import com.leverx.workload.userproject.web.dto.response.AssignedProjects;
import com.leverx.workload.userproject.web.dto.response.AssignedUsers;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class UserProjectConverter {

  private final ModelMapper mapper;

  public AssignedProjects toAssignedProjects(UserProjectEntity entity) {
    log.info("Converting UserProject: from entity to assigned projects");
    mapper.typeMap(UserProjectEntity.class, AssignedProjects.class).addMappings(mapper -> mapper
        .map(src -> src.getId().getProject().getName(), AssignedProjects::setProjectName));
    return Objects.isNull(entity) ? null : mapper.map(entity, AssignedProjects.class);
  }

  public AssignedUsers toAssignedUsers(UserProjectEntity entity) {
    log.info("Converting UserProject: from entity to assigned users");
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
