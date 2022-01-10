package com.leverx.workload.department.web.controller;

import com.leverx.workload.department.service.DepartmentService;
import com.leverx.workload.department.service.converter.DepartmentConverter;
import com.leverx.workload.department.web.dto.request.DepartmentBodyParams;
import com.leverx.workload.department.web.dto.request.DepartmentRequestParams;
import com.leverx.workload.department.web.dto.responce.DepartmentResponse;
import com.leverx.workload.user.service.converter.UserConverter;
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
@RequestMapping("/departments")
@AllArgsConstructor
@Api(tags = "Department operations")
@Slf4j
public class DepartmentController {

  private DepartmentService service;
  private DepartmentConverter departmentMapper;
  private UserConverter userMapper;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Get list of all departments with pagination")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public List<DepartmentResponse> getAllDepartments(
      @ApiParam(name = "page", defaultValue = "${page.num.default}",
          value = "Number of page, start from 0")
      @RequestParam(defaultValue = "${page.num.default}") int page,
      @ApiParam(name = "size", defaultValue = "${page.size.default}",
          value = "Maximum number of items per page")
      @RequestParam(defaultValue = "${page.size.default}") int size) {
    log.info("Start getting all departments with pagination");
    return service.findAllDepartments(new DepartmentRequestParams(page, size)).stream()
        .map(departmentMapper::toResponse).toList();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Get a specific department")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public DepartmentResponse getDepartmentById(
      @ApiParam(name = "id", value = "Identifier of department") @PathVariable @Valid Long id) {
    log.info("Start getting a specific department");
    return departmentMapper.toResponse(service.findById(id));
  }

  @GetMapping("/{id}/users")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Get list of all users in a specific department")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public List<UserResponse> getAllUsersInDepartment(
      @ApiParam(name = "id", value = "Identifier of department") @PathVariable @Valid Long id) {
    log.info("Start getting all users in a specific department");
    return service.findAllUsersInDepartment(id).stream().map(userMapper::toResponse).toList();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Save a new department to the database")
  @ApiResponses(value = {@ApiResponse(code = 203, message = "Created"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public long createDepartment(@ApiParam(name = "department", value = "Department information")
  @RequestBody @Valid DepartmentBodyParams department, BindingResult bindingResult) {
    log.info("Start saving a new department to the database");
    GeneralUtils.checkViolations(bindingResult);
    return service.createDepartment(department);
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Update a department in the database")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public void updateUser(
      @ApiParam(name = "department", value = "Department with updated information") @RequestBody
      @Valid DepartmentBodyParams department, BindingResult bindingResult) {
    log.info("Start updating a department in the database");
    GeneralUtils.checkViolations(bindingResult);
    service.updateDepartment(department);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiOperation(value = "Delete a department in the database")
  @ApiResponses(value = {@ApiResponse(code = 204, message = "No content"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public void deleteDepartmentById(
      @ApiParam(name = "id", value = "Identifier of a department") @PathVariable @Valid Long id) {
    log.info("Start deleting a department in the database");
    service.deleteDepartmentById(id);
  }
}
