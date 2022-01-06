package com.leverx.workload.report.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface ReportService {

  XSSFWorkbook downloadWorkloadReport(String month);
}
