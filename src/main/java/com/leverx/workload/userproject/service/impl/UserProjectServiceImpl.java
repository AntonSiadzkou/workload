package com.leverx.workload.userproject.service.impl;

import com.leverx.workload.exception.NotValidEntityException;
import com.leverx.workload.project.repository.ProjectRepository;
import com.leverx.workload.project.repository.entity.ProjectEntity;
import com.leverx.workload.project.service.converter.ProjectConverter;
import com.leverx.workload.security.service.model.Role;
import com.leverx.workload.user.repository.UserRepository;
import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.user.service.converter.UserConverter;
import com.leverx.workload.userproject.repository.UserProjectRepository;
import com.leverx.workload.userproject.repository.entity.UserProjectEntity;
import com.leverx.workload.userproject.repository.entity.UserProjectId;
import com.leverx.workload.userproject.service.UserProjectService;
import com.leverx.workload.userproject.service.converter.UserProjectConverter;
import com.leverx.workload.userproject.web.dto.request.UserProjectBodyParams;
import com.leverx.workload.userproject.web.dto.response.ProjectWithAssignedUsersResponse;
import com.leverx.workload.userproject.web.dto.response.UserWithAssignedProjectsResponse;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Validated
@Slf4j
public class UserProjectServiceImpl implements UserProjectService {

  private final UserProjectRepository userProjectRepository;
  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;
  private final UserProjectConverter userProjectMapper;
  private final UserConverter userMapper;
  private final ProjectConverter projectMapper;

  @Override
  @Transactional(readOnly = true)
  public UserWithAssignedProjectsResponse findAllUserProjectsByUserId(@NotNull Long id) {
    log.info("Searching all userProjects by user with id: " + id);
    UserEntity user = checkAndGetUserById(id);
    List<UserProjectEntity> assignedProjects =
        userProjectRepository.findAllByIdUserOrderByAssignDate(user);
    log.info("Successfully found all userProjects by user with id: " + id);
    return toUserResponse(user, assignedProjects);
  }

  @Override
  @Transactional(readOnly = true)
  public UserWithAssignedProjectsResponse findAllCurrentUserProjectsByUserId(@NotNull Long id) {
    log.info("Searching all current userProjects by user with id: " + id);
    UserEntity user = checkAndGetUserById(id);
    List<UserProjectEntity> currentAssignedProjects = userProjectRepository
        .findAllByIdUserAndCancelDateGreaterThanOrderByAssignDate(user, LocalDate.now());
    log.info("Successfully found all current userProjects by user with id: " + id);
    return toUserResponse(user, currentAssignedProjects);
  }

  @Override
  @Transactional(readOnly = true)
  public ProjectWithAssignedUsersResponse findAllUserProjectsByProjectId(Long id) {
    log.info("Searching all userProjects by project with id: " + id);
    ProjectEntity project = checkAndGetProjectById(id);
    List<UserProjectEntity> assignedUsers =
        userProjectRepository.findAllByIdProjectOrderByAssignDate(project);
    log.info("Successfully found all userProjects by project with id: " + id);
    return toProjectResponse(project, assignedUsers);
  }

  @Override
  @Transactional(readOnly = true)
  public ProjectWithAssignedUsersResponse findAllCurrentUserProjectsByProjectId(Long id) {
    log.info("Searching all current userProjects by project with id: " + id);
    ProjectEntity project = checkAndGetProjectById(id);
    List<UserProjectEntity> currentAssignedUsers = userProjectRepository
        .findAllByIdProjectAndCancelDateGreaterThanOrderByAssignDate(project, LocalDate.now());
    log.info("Successfully found all current userProjects by project with id: " + id);
    return toProjectResponse(project, currentAssignedUsers);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserEntity> findAllAvailableUsers(@NotNull Integer days, String date) {
    final LocalDate fromDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
    final LocalDate tillDate = fromDate.plusDays(days);
    log.info("Searching all available users from " + fromDate + " till " + tillDate);
    List<UserEntity> activeUsers = userRepository.findAllByActiveAndRole(true, Role.USER);
    List<UserProjectEntity> userProjectsWithinPeriod =
        userProjectRepository.findAllActiveProjectWithinPeriod(fromDate, tillDate);
    List<UserEntity> usersWithActiveProject = userProjectsWithinPeriod.stream()
        .map(userProject -> userProject.getId().getUser()).distinct().toList();
    activeUsers.removeAll(usersWithActiveProject);
    log.info("Successfully found all available users from " + fromDate + " till " + tillDate);
    return activeUsers;
  }

  @Override
  @Transactional
  public void saveUserProject(UserProjectBodyParams userProject) {
    log.info("Adding or updating userProject)");
    UserEntity user = checkAndGetUserById(userProject.getUserId());
    ProjectEntity project = checkAndGetProjectById(userProject.getProjectId());
    LocalDate assignDateFromRequest = LocalDate.parse(userProject.getAssignDate());
    LocalDate cancelDateFromRequest = LocalDate.parse(userProject.getCancelDate());
    if (assignDateFromRequest.isBefore(project.getStartDate())
        || cancelDateFromRequest.isAfter(project.getEndDate())
        || assignDateFromRequest.isAfter(cancelDateFromRequest)) {
      throw new NotValidEntityException(
          "Wrong dates: assign date must not be before start date of the project, cancel date must not "
              + "be after end date of the project, and assign date must be before cancel date.");
    }
    UserProjectEntity entity = new UserProjectEntity(new UserProjectId(user, project),
        assignDateFromRequest, cancelDateFromRequest);
    userProjectRepository.save(entity);
    log.info("Successfully added or updated userProject for a user with id: " + user.getId()
        + " and a project with id: " + project.getId());
  }

  @Override
  @Transactional
  public void deleteUserProject(Long projectId, Long userId) {
    log.info(
        "Deleting userProject for user with id " + userId + " and project with id " + projectId);
    ProjectEntity project = checkAndGetProjectById(projectId);
    UserEntity user = checkAndGetUserById(userId);
    UserProjectId id = new UserProjectId(user, project);
    userProjectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
        "Unable to delete user with project.There is no such pair of identifiers."));
    userProjectRepository.deleteById(id);
    log.info("Successfully deleting userProject for user with id " + userId
        + " and project with id " + projectId);
  }

  private UserEntity checkAndGetUserById(Long id) {
    return userRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(String.format("User with id=%s not found", id)));
  }

  private UserWithAssignedProjectsResponse toUserResponse(UserEntity user,
      List<UserProjectEntity> projects) {
    log.info("Converting user entity and assigned projects to response");
    return new UserWithAssignedProjectsResponse(userMapper.toResponse(user),
        projects.stream().map(userProjectMapper::toAssignedProjects).toList());
  }

  private ProjectEntity checkAndGetProjectById(Long id) {
    return projectRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(String.format("Project with id=%s not found", id)));
  }

  private ProjectWithAssignedUsersResponse toProjectResponse(ProjectEntity project,
      List<UserProjectEntity> users) {
    log.info("Converting project entity and assigned users to response");
    return new ProjectWithAssignedUsersResponse(projectMapper.toResponse(project),
        users.stream().map(userProjectMapper::toAssignedUsers).toList());
  }
}
