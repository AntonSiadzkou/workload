package com.leverx.workload.userproject.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.leverx.workload.project.repository.entity.ProjectEntity;
import com.leverx.workload.user.repository.UserRepository;
import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.user.service.converter.UserConverter;
import com.leverx.workload.user.web.dto.response.UserResponse;
import com.leverx.workload.userproject.repository.UserProjectRepository;
import com.leverx.workload.userproject.repository.entity.UserProjectEntity;
import com.leverx.workload.userproject.repository.entity.UserProjectId;
import com.leverx.workload.userproject.service.UserProjectService;
import com.leverx.workload.userproject.service.converter.UserProjectConverter;
import com.leverx.workload.userproject.web.dto.response.ProjectWithoutUsers;
import com.leverx.workload.userproject.web.dto.response.UserWithAssignedProjectsResponse;
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
  private UserProjectConverter userProjectMapper;
  @Mock
  private UserConverter userMapper;

  @BeforeEach
  void setUp() {
    userProjectRepository = mock(UserProjectRepository.class);
    userRepository = mock(UserRepository.class);
    userProjectMapper = mock(UserProjectConverter.class);
    userMapper = mock(UserConverter.class);
    underTest = new UserProjectServiceImpl(userProjectRepository, userRepository, userProjectMapper,
        userMapper);
  }

  @Test
  void id_findAllUserProjectsByUserId_ListOfUserProjects() {
    long id = 4;
    UserEntity user = new UserEntity();
    user.setId(id);

    long id2 = 2;
    ProjectEntity project = new ProjectEntity();
    project.setId(id2);
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

    ProjectWithoutUsers assignedProject = new ProjectWithoutUsers();
    assignedProject.setProjectName("test");

    List<ProjectWithoutUsers> assignedProjects = new ArrayList<>();
    assignedProjects.add(assignedProject);

    UserWithAssignedProjectsResponse expected =
        new UserWithAssignedProjectsResponse(userResponse, assignedProjects);

    given(userRepository.findById(id)).willReturn(Optional.of(user));
    given(userProjectRepository.findAllByIdUserOrderByAssignDate(user)).willReturn(projects);
    given(userMapper.toResponse(user)).willReturn(userResponse);
    given(userProjectMapper.toProjectWithoutUsers(userProject)).willReturn(assignedProject);

    UserWithAssignedProjectsResponse actual = underTest.findAllUserProjectsByUserId(id);

    verify(userRepository).findById(id);
    verify(userProjectRepository).findAllByIdUserOrderByAssignDate(user);
    verify(userMapper).toResponse(user);
    verify(userProjectMapper).toProjectWithoutUsers(userProject);
    assertThat(actual).isEqualTo(expected);
  }
}
