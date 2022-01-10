package com.leverx.workload.userproject.web.dto.response;

import com.leverx.workload.project.web.dto.responce.ProjectResponse;
import java.util.List;

public record ProjectWithAssignedUsersResponse(ProjectResponse project, List<AssignedUsers> users) {}
