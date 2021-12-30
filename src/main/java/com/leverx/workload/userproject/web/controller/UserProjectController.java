package com.leverx.workload.userproject.web.controller;

import com.leverx.workload.userproject.service.UserProjectService;
import com.leverx.workload.userproject.service.converter.UserProjectConverter;
import com.leverx.workload.userproject.web.dto.request.UserProjectBodyParams;
import com.leverx.workload.userproject.web.dto.response.ProjectWithAssignedUsersResponse;
import com.leverx.workload.userproject.web.dto.response.UserWithAssignedProjectsResponse;
import com.leverx.workload.util.GeneralUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Api(tags = "User and project relations operations")
public class UserProjectController {

  private final UserProjectService service;
  private final UserProjectConverter mapper;

  @GetMapping("/users/{id}/projects")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Get list of all assigned projects for a specific user")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public UserWithAssignedProjectsResponse getAllUserProjectsByUserId(
      @ApiParam(name = "id", value = "Identifier of user") @PathVariable @Valid Long id) {
    return service.findAllUserProjectsByUserId(id);
  }

  @GetMapping("/users/{id}/projects/current")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Get list of all current and future assigned projects for a specific user")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public UserWithAssignedProjectsResponse getAllCurrentUserProjectsByUserId(
      @ApiParam(name = "id", value = "Identifier of user") @PathVariable @Valid Long id) {
    return service.findAllCurrentUserProjectsByUserId(id);
  }

  @GetMapping("/projects/{id}/users")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Get list of all assigned users for a specific project")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public ProjectWithAssignedUsersResponse getAllUserProjectsByProjectId(
      @ApiParam(name = "id", value = "Identifier of project") @PathVariable @Valid Long id) {
    return service.findAllUserProjectsByProjectId(id);
  }

  @GetMapping("/projects/{id}/users/current")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Get list of all current and future assigned users for a specific project")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public ProjectWithAssignedUsersResponse getAllCurrentUserProjectsByProjectId(
      @ApiParam(name = "id", value = "Identifier of project") @PathVariable @Valid Long id) {
    return service.findAllCurrentUserProjectsByProjectId(id);
  }

  @PutMapping("/projects/users") // todo Put or Post - with post we can update dates but put
                                 // response always 201?
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(
      value = "Add a specific user to a specific project for a period (create or update userProject)")
  @ApiResponses(value = {@ApiResponse(code = 201, message = "Created"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public void addUserToProject(
      @ApiParam(name = "userProject", value = "Project and user with assign and cancel dates")
      @RequestBody @Valid UserProjectBodyParams userProject, BindingResult bindingResult) {
    GeneralUtils.checkViolations(bindingResult);
    service.saveUserProject(userProject);
  }

}
