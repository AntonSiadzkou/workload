package com.leverx.workload.report.service.impl;

import com.leverx.workload.exception.ReportDownloadException;
import com.leverx.workload.report.service.ReportService;
import com.leverx.workload.report.task.report.UnoccupiedReportTask;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

  @Value("${report.dir}")
  private String dir;

  private static final String FILE_NAME_PATTERN = "^Workload-%s.xlsx$";

  private final UnoccupiedReportTask unoccupiedReportTask;

  public ReportServiceImpl(UnoccupiedReportTask unoccupiedReportTask) {
    this.unoccupiedReportTask = unoccupiedReportTask;
  }

  @Override
  public Workbook downloadWorkloadReport(String month) {
    Month reportMonth =
        (month == null) ? LocalDate.now().getMonth() : Month.valueOf(month.trim().toUpperCase());
    File report = getReport(reportMonth);
    log.info("Successfully get report for " + reportMonth.name());
    try {
      return new XSSFWorkbook(report);
    } catch (IOException | InvalidFormatException e) {
      log.error("Exception of writing excel-report");
      throw new ReportDownloadException(e.getMessage());
    }
  }

  @Override
  public Workbook downloadUnoccupiedUsersWithinMonth() {
    return unoccupiedReportTask.createUnoccupiedReport();
  }

  private File getReport(Month month) {
    File[] reports = new File(dir).listFiles();
    if (reports == null) {
      throw new ReportDownloadException("No reports available");
    }
    File concreteReport = Arrays.stream(reports)
        .filter(file -> file.getName().matches(String.format(FILE_NAME_PATTERN, month.name())))
        .findFirst()
        .orElseThrow(() -> new ReportDownloadException("No report is found for " + month.name()));
    return concreteReport;
  }
}
