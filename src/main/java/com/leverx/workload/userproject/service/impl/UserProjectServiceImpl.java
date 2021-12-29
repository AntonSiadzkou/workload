package com.leverx.workload.userproject.service.impl;

import com.leverx.workload.user.repository.UserRepository;
import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.user.service.converter.UserConverter;
import com.leverx.workload.userproject.repository.UserProjectRepository;
import com.leverx.workload.userproject.repository.entity.UserProjectEntity;
import com.leverx.workload.userproject.service.UserProjectService;
import com.leverx.workload.userproject.service.converter.UserProjectConverter;
import com.leverx.workload.userproject.web.dto.response.UserWithAssignedProjectsResponse;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Validated
public class UserProjectServiceImpl implements UserProjectService {

  private final UserProjectRepository userProjectRepository;
  private final UserRepository userRepository;
  private final UserProjectConverter userProjectMapper;
  private final UserConverter userMapper;

  @Override
  @Transactional(readOnly = true)
  public UserWithAssignedProjectsResponse findAllUserProjectsByUserId(@NotNull Long id) {
    UserEntity user = checkAndGetUserById(id);
    List<UserProjectEntity> assignedProjects =
        userProjectRepository.findAllByIdUserOrderByAssignDate(user);
    return toUserResponse(user, assignedProjects);
  }

  @Override
  @Transactional(readOnly = true)
  public UserWithAssignedProjectsResponse findAllCurrentUserProjectsByUserId(@NotNull Long id) {
    UserEntity user = checkAndGetUserById(id);
    List<UserProjectEntity> currentAssignedProjects = userProjectRepository
        .findAllByIdUserAndCancelDateGreaterThanOrderByAssignDate(user, LocalDate.now());
    return toUserResponse(user, currentAssignedProjects);
  }

  private UserEntity checkAndGetUserById(Long id) {
    return userRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(String.format("User with id=%s not found", id)));
  }

  private UserWithAssignedProjectsResponse toUserResponse(UserEntity user,
      List<UserProjectEntity> projects) {
    return new UserWithAssignedProjectsResponse(userMapper.toResponse(user),
        projects.stream().map(userProjectMapper::toProjectWithoutUsers).toList());
  }
}
