package com.leverx.workload.user.web.controller;

import com.leverx.workload.user.service.UserService;
import com.leverx.workload.user.service.converter.UserConverter;
import com.leverx.workload.user.web.dto.request.UserBodyParams;
import com.leverx.workload.user.web.dto.request.UserRequestParams;
import com.leverx.workload.user.web.dto.response.UserResponse;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Api(tags = "User operations")
@Slf4j
public class UserController {

  private final UserService service;
  private final UserConverter mapper;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(
      value = "Get list of all users with pagination and sorting and filtering by first name and email")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public List<UserResponse> getAllUsers(
      @ApiParam(name = "firstName",
          value = "First name of users by which the selection is filtered")
      @RequestParam(name = "firstName", required = false) String firstName,
      @ApiParam(name = "email", value = "Email of user by which the selection is filtered")
      @RequestParam(required = false) String email,
      @ApiParam(name = "page", defaultValue = "${page.num.default}",
          value = "Number of page, start from 0")
      @RequestParam(defaultValue = "${page.num.default}") int page,
      @ApiParam(name = "size", defaultValue = "${page.size.default}",
          value = "Maximum number of items per page")
      @RequestParam(defaultValue = "${page.size.default}") int size,
      @ApiParam(name = "sort", defaultValue = "${page.sort.user.default}",
          value = "A column to sort the selection")
      @RequestParam(defaultValue = "${page.sort.user.default}") String column,
      @ApiParam(name = "sort", defaultValue = "${page.sort.direction.default}",
          value = "A direction to sort the selection")
      @RequestParam(defaultValue = "${page.sort.direction.default}") String direction) {
    log.info("Start getting all users");
    return service
        .findAllUsers(new UserRequestParams(firstName, email, page, size, column, direction))
        .stream().map(mapper::toResponse).toList();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Get a user by an identifier")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 404, message = "Not Found"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public UserResponse getUserById(
      @ApiParam(name = "id", value = "Identifier of user") @PathVariable @Valid Long id) {
    log.info("Start getting a user by id");
    return mapper.toResponse(service.findById(id));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Save a user to the database")
  @ApiResponses(value = {@ApiResponse(code = 203, message = "Created"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public long createUser(
      @ApiParam(name = "user", value = "User information") @RequestBody @Valid UserBodyParams user,
      BindingResult bindingResult) {
    log.info("Start saving a user to the database");
    GeneralUtils.checkViolations(bindingResult);
    return service.createUser(user);
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Update a user in the database")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public void updateUser(@ApiParam(name = "user", value = "User with updated information")
  @RequestBody @Valid UserBodyParams user, BindingResult bindingResult) {
    log.info("Start updating a user in the database");
    GeneralUtils.checkViolations(bindingResult);
    service.updateUser(user);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiOperation(value = "Delete a user in the database")
  @ApiResponses(value = {@ApiResponse(code = 204, message = "No content"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public void deleteUserById(
      @ApiParam(name = "id", value = "Identifier of user") @PathVariable @Valid Long id) {
    log.info("Start deleting a user in the database");
    service.deleteUserById(id);
  }
}
