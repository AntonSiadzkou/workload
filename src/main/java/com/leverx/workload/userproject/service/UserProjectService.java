package com.leverx.workload.userproject.service;

import com.leverx.workload.userproject.web.dto.request.UserProjectBodyParams;
import com.leverx.workload.userproject.web.dto.response.ProjectWithAssignedUsersResponse;
import com.leverx.workload.userproject.web.dto.response.UserWithAssignedProjectsResponse;
import javax.validation.constraints.NotNull;

public interface UserProjectService {

  UserWithAssignedProjectsResponse findAllUserProjectsByUserId(@NotNull Long id);

  UserWithAssignedProjectsResponse findAllCurrentUserProjectsByUserId(@NotNull Long id);

  ProjectWithAssignedUsersResponse findAllUserProjectsByProjectId(@NotNull Long id);

  ProjectWithAssignedUsersResponse findAllCurrentUserProjectsByProjectId(@NotNull Long id);

  void saveUserProject(@NotNull UserProjectBodyParams userProject);

  void deleteUserProject(@NotNull Long projectId, @NotNull Long userId);
}
