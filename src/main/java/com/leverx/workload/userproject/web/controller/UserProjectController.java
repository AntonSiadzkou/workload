package com.leverx.workload.userproject.web.controller;

import com.leverx.workload.user.service.converter.UserConverter;
import com.leverx.workload.user.web.dto.response.UserResponse;
import com.leverx.workload.userproject.service.UserProjectService;
import com.leverx.workload.userproject.web.dto.request.UserProjectBodyParams;
import com.leverx.workload.userproject.web.dto.response.ProjectWithAssignedUsersResponse;
import com.leverx.workload.userproject.web.dto.response.UserWithAssignedProjectsResponse;
import com.leverx.workload.util.GeneralUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Api(tags = "User and project relations operations")
@Slf4j
public class UserProjectController {

  private final UserProjectService service;
  private final UserConverter userMapper;

  @GetMapping("/users/{id}/projects")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Get list of all assigned projects for a specific user")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public UserWithAssignedProjectsResponse getAllUserProjectsByUserId(
      @ApiParam(name = "id", value = "Identifier of user") @PathVariable @Valid Long id) {
    log.info("Start getting all assigned projects for a specific user");
    return service.findAllUserProjectsByUserId(id);
  }

  @GetMapping("/users/{id}/projects/current")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Get list of all current and future assigned projects for a specific user")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public UserWithAssignedProjectsResponse getAllCurrentUserProjectsByUserId(
      @ApiParam(name = "id", value = "Identifier of user") @PathVariable @Valid Long id) {
    log.info("Start getting all current and future assigned projects for a specific user");
    return service.findAllCurrentUserProjectsByUserId(id);
  }

  @GetMapping("/projects/{id}/users")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Get list of all assigned users for a specific project")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public ProjectWithAssignedUsersResponse getAllUserProjectsByProjectId(
      @ApiParam(name = "id", value = "Identifier of project") @PathVariable @Valid Long id) {
    log.info("Start getting all assigned users for a specific project");
    return service.findAllUserProjectsByProjectId(id);
  }

  @GetMapping("/projects/{id}/users/current")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Get list of all current and future assigned users for a specific project")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public ProjectWithAssignedUsersResponse getAllCurrentUserProjectsByProjectId(
      @ApiParam(name = "id", value = "Identifier of project") @PathVariable @Valid Long id) {
    log.info("Start getting all current and future assigned users for a specific project");
    return service.findAllCurrentUserProjectsByProjectId(id);
  }

  @GetMapping("/projects/users/available")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(
      value = "Get list of available users (users without project) within a specified period from specified date")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public List<UserResponse> getAllAvailableUsers(
      @ApiParam(name = "days", defaultValue = "${available.days}",
          value = "A specified period (days)")
      @RequestParam(defaultValue = "${available.days}") int days,
      @ApiParam(name = "date", defaultValue = "current date", value = "Specified date")
      @RequestParam(required = false) String date) {
    log.info(
        "Start getting available users without project within a specified period from specified date");
    return service.findAllAvailableUsers(days, date).stream().map(userMapper::toResponse).toList();
  }

  @PutMapping("/projects/users") // todo ? Put(response 201) or Post(can update dates)
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(
      value = "Add a specific user to a specific project for a period (create or update userProject)")
  @ApiResponses(value = {@ApiResponse(code = 201, message = "Created"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public void addUserToProject(
      @ApiParam(name = "userProject", value = "Project and user with assign and cancel dates")
      @RequestBody @Valid UserProjectBodyParams userProject, BindingResult bindingResult) {
    log.info("Start adding a specific user to a specific project for a period");
    GeneralUtils.checkViolations(bindingResult);
    service.saveUserProject(userProject);
  }

  @DeleteMapping("/projects/{projectId}/users/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiOperation(value = "Delete a specific user from a specific project")
  @ApiResponses(value = {@ApiResponse(code = 204, message = "No content"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public void deleteUserProject(
      @ApiParam(name = "projectId", value = "Identifier of project") @PathVariable
      @Valid Long projectId,
      @ApiParam(name = "userId", value = "Identifier of user") @PathVariable @Valid Long userId) {
    log.info("Start deleting a specific user from a specific project");
    service.deleteUserProject(projectId, userId);
  }
}
