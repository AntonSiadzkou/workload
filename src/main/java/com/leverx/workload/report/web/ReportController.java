package com.leverx.workload.report.web;

import com.leverx.workload.exception.ReportDownloadException;
import com.leverx.workload.report.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
@AllArgsConstructor
@Slf4j
@Api(tags = "Downloading excel reports")
public class ReportController {

  public static final String MEDIA_TYPE_EXCEL_XLSX =
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  private static final String HEADER_REPORT_ATTACHMENT = "attachment, filename=Report.xslx";

  private final ReportService service;

  @GetMapping("/workload")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Downloading workload report")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public void downloadWorkloadReport(
      @ApiParam(name = "month", value = "The month of a workload report")
      @RequestParam(required = false) String month, HttpServletResponse response) {
    log.info("Start downloading workload report");
    // response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE); //todo what's better?
    response.setContentType(MEDIA_TYPE_EXCEL_XLSX);
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, HEADER_REPORT_ATTACHMENT);
    Workbook workbook = service.downloadWorkloadReport(month);
    writeResponse(response, workbook);
    closeWorkbook(workbook);
    log.info("Workload report was successfully downloaded");
  }

  @GetMapping("/unoccupied")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Creating and downloading unoccupied report")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public void downloadUnoccupiedReport(HttpServletResponse response) {
    log.info("Start downloading unoccupied report");
    response.setContentType(MEDIA_TYPE_EXCEL_XLSX);
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, HEADER_REPORT_ATTACHMENT);
    Workbook workbook = service.downloadUnoccupiedUsersWithinMonth();
    writeResponse(response, workbook);
    closeWorkbook(workbook);
    log.info("Unoccupied report was successfully downloaded");
  }

  private void writeResponse(HttpServletResponse response, Workbook workbook) {
    try (ServletOutputStream outputStream = response.getOutputStream()) {
      workbook.write(outputStream);
    } catch (IOException e) {
      throw new ReportDownloadException(e.getMessage());
    }
  }

  private void closeWorkbook(Workbook workbook) {
    try {
      workbook.close();
    } catch (IOException e) {
      throw new ReportDownloadException(e.getMessage());
    }
  }
}
