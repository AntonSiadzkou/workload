package com.leverx.workload.userproject.web.dto.response;

import com.leverx.workload.user.web.dto.response.UserResponse;
import java.util.List;

public record UserWithAssignedProjectsResponse(UserResponse user, List<ProjectWithoutUsers> projects) {}
