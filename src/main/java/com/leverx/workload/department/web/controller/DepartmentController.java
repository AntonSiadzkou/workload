package com.leverx.workload.department.web.controller;

import com.leverx.workload.department.service.DepartmentService;
import com.leverx.workload.department.service.converter.DepartmentConverter;
import com.leverx.workload.department.web.dto.request.DepartmentRequestParams;
import com.leverx.workload.department.web.dto.responce.DepartmentResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/departments")
@AllArgsConstructor
@Api(tags = "Department operations")
public class DepartmentController {

  private DepartmentService service;
  private DepartmentConverter mapper;

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
    return service.findAllDepartments(new DepartmentRequestParams(page, size)).stream()
        .map(mapper::toResponse).toList();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Get a specific department")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public DepartmentResponse getDepartmentById(
      @ApiParam(name = "id", value = "Identifier of department") @PathVariable @Valid Long id) {
    return mapper.toResponse(service.findById(id));
  }
}
