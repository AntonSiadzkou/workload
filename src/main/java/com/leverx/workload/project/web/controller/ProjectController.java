package com.leverx.workload.project.web.controller;

import com.leverx.workload.project.service.ProjectService;
import com.leverx.workload.project.service.converter.ProjectConverter;
import com.leverx.workload.project.web.dto.request.ProjectRequestParams;
import com.leverx.workload.project.web.dto.responce.ProjectResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
@Api(tags = "Project operations")
public class ProjectController {

  private ProjectService service;
  private ProjectConverter mapper;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(
      value = "Get list of all projects with filter by start and end date, pagination and sorting by start date")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public List<ProjectResponse> getAllProjects(
      @ApiParam(name = "startDate",
          value = "Start date of project by which the selection is filtered")
      @RequestParam(name = "startDate", required = false) String startDate,
      @ApiParam(name = "endDate", value = "End date of project by which the selection is filtered")
      @RequestParam(name = "endDate", required = false) String endDate,
      @ApiParam(name = "page", defaultValue = "${page.num.default}",
          value = "Number of page, start from 0")
      @RequestParam(defaultValue = "${page.num.default}") int page,
      @ApiParam(name = "size", defaultValue = "${page.size.default}",
          value = "Maximum number of items per page")
      @RequestParam(defaultValue = "${page.size.default}") int size,
      @ApiParam(name = "sort", defaultValue = "${page.sort.project.default}",
          value = "A column to sort the selection")
      @RequestParam(defaultValue = "${page.sort.project.default}") String column,
      @ApiParam(name = "sort", defaultValue = "${page.sort.direction.default}",
          value = "A direction to sort the selection")
      @RequestParam(defaultValue = "${page.sort.direction.default}") String direction) {
    return service
        .findAllProjects(
            new ProjectRequestParams(startDate, endDate, page, size, column, direction))
        .stream().map(mapper::toResponse).toList();
  }
}
