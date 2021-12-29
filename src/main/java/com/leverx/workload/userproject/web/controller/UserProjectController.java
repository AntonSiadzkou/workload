package com.leverx.workload.userproject.web.controller;

import com.leverx.workload.userproject.service.UserProjectService;
import com.leverx.workload.userproject.service.converter.UserProjectConverter;
import com.leverx.workload.userproject.web.dto.response.UserWithAssignedProjectsResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
