package com.leverx.workload.userproject.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.leverx.workload.project.repository.ProjectRepository;
import com.leverx.workload.project.repository.entity.ProjectEntity;
import com.leverx.workload.project.service.converter.ProjectConverter;
import com.leverx.workload.project.web.dto.responce.ProjectResponse;
import com.leverx.workload.security.service.model.Role;
import com.leverx.workload.user.repository.UserRepository;
import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.user.service.converter.UserConverter;
import com.leverx.workload.user.web.dto.response.UserResponse;
import com.leverx.workload.userproject.repository.UserProjectRepository;
import com.leverx.workload.userproject.repository.entity.UserProjectEntity;
import com.leverx.workload.userproject.repository.entity.UserProjectId;
import com.leverx.workload.userproject.service.UserProjectService;
import com.leverx.workload.userproject.service.converter.UserProjectConverter;
import com.leverx.workload.userproject.web.dto.request.UserProjectBodyParams;
import com.leverx.workload.userproject.web.dto.response.AssignedProjects;
import com.leverx.workload.userproject.web.dto.response.AssignedUsers;
import com.leverx.workload.userproject.web.dto.response.ProjectWithAssignedUsersResponse;
import com.leverx.workload.userproject.web.dto.response.UserWithAssignedProjectsResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
class UserProjectServiceImplTest {

  private UserProjectService underTest;

  @Mock
  private UserProjectRepository userProjectRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ProjectRepository projectRepository;
  @Mock
  private UserProjectConverter userProjectMapper;
  @Mock
  private UserConverter userMapper;
  @Mock
  private ProjectConverter projectMapper;

  @BeforeEach
  void setUp() {
    userProjectRepository = mock(UserProjectRepository.class);
    userRepository = mock(UserRepository.class);
    projectRepository = mock(ProjectRepository.class);
    userProjectMapper = mock(UserProjectConverter.class);
    userMapper = mock(UserConverter.class);
    projectMapper = mock(ProjectConverter.class);
    underTest = new UserProjectServiceImpl(userProjectRepository, userRepository, projectRepository,
        userProjectMapper, userMapper, projectMapper);
  }

  @Test
  void id_findAllUserProjectsByUserId_ListOfUserProjects() {
    long id = 4;
    UserEntity user = new UserEntity();
    user.setId(id);

    long idPr = 2;
    ProjectEntity project = new ProjectEntity();
    project.setId(idPr);
    project.setName("test");

    UserProjectId upId = new UserProjectId();
    upId.setUser(user);
    upId.setProject(project);

    UserProjectEntity userProject = new UserProjectEntity();
    userProject.setId(upId);

    List<UserProjectEntity> projects = new ArrayList<>();
    projects.add(userProject);

    UserResponse userResponse = new UserResponse();
    userResponse.setId(id);

    AssignedProjects assignedProject = new AssignedProjects();
    assignedProject.setProjectName("test");

    List<AssignedProjects> assignedProjects = new ArrayList<>();
    assignedProjects.add(assignedProject);

    UserWithAssignedProjectsResponse expected =
        new UserWithAssignedProjectsResponse(userResponse, assignedProjects);

    given(userRepository.findById(id)).willReturn(Optional.of(user));
    given(userProjectRepository.findAllByIdUserOrderByAssignDate(user)).willReturn(projects);
    given(userMapper.toResponse(user)).willReturn(userResponse);
    given(userProjectMapper.toAssignedProjects(userProject)).willReturn(assignedProject);

    UserWithAssignedProjectsResponse actual = underTest.findAllUserProjectsByUserId(id);

    verify(userRepository).findById(id);
    verify(userProjectRepository).findAllByIdUserOrderByAssignDate(user);
    verify(userMapper).toResponse(user);
    verify(userProjectMapper).toAssignedProjects(userProject);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void id_findAllUserProjectsByProjectId_ListOfUserProjects() {
    long id = 4;
    ProjectEntity project = new ProjectEntity();
    project.setId(id);

    long idUs = 2;
    UserEntity user = new UserEntity();
    user.setId(idUs);
    user.setFirstName("Test");

    UserProjectId upId = new UserProjectId();
    upId.setProject(project);
    upId.setUser(user);

    UserProjectEntity userProject = new UserProjectEntity();
    userProject.setId(upId);

    List<UserProjectEntity> projects = new ArrayList<>();
    projects.add(userProject);

    ProjectResponse projectResponse = new ProjectResponse();
    projectResponse.setId(id);

    AssignedUsers assignedUser = new AssignedUsers();
    assignedUser.setFirstName("Test");

    List<AssignedUsers> assignedUsers = new ArrayList<>();
    assignedUsers.add(assignedUser);

    ProjectWithAssignedUsersResponse expected =
        new ProjectWithAssignedUsersResponse(projectResponse, assignedUsers);

    given(projectRepository.findById(id)).willReturn(Optional.of(project));
    given(userProjectRepository.findAllByIdProjectOrderByAssignDate(project)).willReturn(projects);
    given(projectMapper.toResponse(project)).willReturn(projectResponse);
    given(userProjectMapper.toAssignedUsers(userProject)).willReturn(assignedUser);

    ProjectWithAssignedUsersResponse actual = underTest.findAllUserProjectsByProjectId(id);

    verify(projectRepository).findById(id);
    verify(userProjectRepository).findAllByIdProjectOrderByAssignDate(project);
    verify(projectMapper).toResponse(project);
    verify(userProjectMapper).toAssignedUsers(userProject);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void daysAndDate_findAllAvailableUsers_ListOfUsers() {
    int days = 10;
    String date = "2021-01-01";

    long id = 4;
    ProjectEntity project = new ProjectEntity();
    project.setId(id);

    long idUs = 2;
    UserEntity user = new UserEntity();
    user.setId(idUs);

    long idUs2 = 55;
    UserEntity user2 = new UserEntity();
    user2.setId(idUs2);

    UserProjectId upId = new UserProjectId();
    upId.setProject(project);
    upId.setUser(user2);

    UserProjectEntity userProject = new UserProjectEntity();
    userProject.setId(upId);

    List<UserEntity> activeUsers = new ArrayList<>();
    activeUsers.add(user);
    activeUsers.add(user2);

    List<UserProjectEntity> userProjectsWithinPeriod = new ArrayList<>();
    userProjectsWithinPeriod.add(userProject);

    List<UserEntity> expected = new ArrayList<>();
    expected.add(user);

    given(userRepository.findAllByActiveAndRole(true, Role.USER)).willReturn(activeUsers);
    given(userProjectRepository.findAllActiveProjectWithinPeriod(LocalDate.of(2021, 1, 1),
        LocalDate.of(2021, 1, 11))).willReturn(userProjectsWithinPeriod);

    List<UserEntity> actual = underTest.findAllAvailableUsers(days, date);

    verify(userRepository).findAllByActiveAndRole(true, Role.USER);
    verify(userProjectRepository).findAllActiveProjectWithinPeriod(LocalDate.of(2021, 1, 1),
        LocalDate.of(2021, 1, 11));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void validUserProject_SaveUserProject_Created() {
    long id = 4;
    ProjectEntity project = new ProjectEntity();
    project.setId(id);
    project.setStartDate(LocalDate.of(2021, 4, 10));
    project.setEndDate(LocalDate.of(2023, 4, 10));

    long idUs = 2;
    UserEntity user = new UserEntity();
    user.setId(idUs);

    UserProjectId upId = new UserProjectId();
    upId.setProject(project);
    upId.setUser(user);

    UserProjectEntity userProject = new UserProjectEntity();
    userProject.setId(upId);
    userProject.setAssignDate(LocalDate.of(2022, 4, 10));
    userProject.setCancelDate(LocalDate.of(2022, 6, 10));

    UserProjectBodyParams params = new UserProjectBodyParams();
    params.setUserId(idUs);
    params.setProjectId(id);
    params.setAssignDate("2022-04-10");
    params.setCancelDate("2022-06-10");

    given(userRepository.findById(idUs)).willReturn(Optional.of(user));
    given(projectRepository.findById(id)).willReturn(Optional.of(project));

    underTest.saveUserProject(params);

    verify(userRepository).findById(idUs);
    verify(projectRepository).findById(id);
    verify(userProjectRepository).save(userProject);
  }

  @Test
  void validUserProjectId_DeleteUserProject_Deleted() {
    long id = 4;
    ProjectEntity project = new ProjectEntity();
    project.setId(id);

    long idUs = 2;
    UserEntity user = new UserEntity();
    user.setId(idUs);

    UserProjectId upId = new UserProjectId();
    upId.setProject(project);
    upId.setUser(user);

    UserProjectEntity userProject = new UserProjectEntity();
    userProject.setId(upId);

    given(projectRepository.findById(id)).willReturn(Optional.of(project));
    given(userRepository.findById(idUs)).willReturn(Optional.of(user));
    given(userProjectRepository.findById(upId)).willReturn(Optional.of(userProject));

    underTest.deleteUserProject(id, idUs);

    verify(userRepository).findById(idUs);
    verify(projectRepository).findById(id);
    verify(userProjectRepository).findById(upId);
    verify(userProjectRepository).deleteById(upId);
  }
}
