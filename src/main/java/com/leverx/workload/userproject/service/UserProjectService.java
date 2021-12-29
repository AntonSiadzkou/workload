package com.leverx.workload.userproject.service;

import com.leverx.workload.userproject.web.dto.response.UserWithAssignedProjectsResponse;
import javax.validation.constraints.NotNull;

public interface UserProjectService {

  UserWithAssignedProjectsResponse findAllUserProjectsByUserId(@NotNull Long id);

  UserWithAssignedProjectsResponse findAllCurrentUserProjectsByUserId(@NotNull Long id);
}
