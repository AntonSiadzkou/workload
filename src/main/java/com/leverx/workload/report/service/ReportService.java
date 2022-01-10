package com.leverx.workload.report.service;

import org.apache.poi.ss.usermodel.Workbook;

public interface ReportService {

  Workbook downloadWorkloadReport(String month);

  Workbook downloadUnoccupiedUsersWithinMonth();
}
