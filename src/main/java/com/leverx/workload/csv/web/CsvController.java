package com.leverx.workload.csv.web;

import com.leverx.workload.csv.service.CsvService;
import com.leverx.workload.csv.web.dto.CsvResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@Api(tags = "Operations with CSV-multipart file")
@Slf4j
public class CsvController {

  private final CsvService service;

  @PostMapping("/users/load")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Load users from cvs-file to database")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public CsvResponse loadUsers(
      @ApiParam(name = "file", value = "Multipart file (csv) with users data") MultipartFile file) {
    log.info("Start loading users");
    return service.loadUsers(file);
  }
}
