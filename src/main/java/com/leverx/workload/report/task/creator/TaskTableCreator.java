package com.leverx.workload.report.task.creator;

import com.leverx.workload.report.task.ReportType;
import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.userproject.repository.entity.UserProjectEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaskTableCreator {

  private static final String DEPARTMENT_COL = "Department";
  private static final String FULL_NAME_COL = "Full Name";
  private static final String PROJECT_COL = "Project";
  private static final String FROM_COL = "From";
  private static final String TILL_COL = "Till";
  private static final String CANCEL_DATE_COL = "Cancel date";
  private static final String EXCEL_DATE_FORMAT = "dd.mm.yyyy";

  public Workbook createFilledWorkbook(List<UserProjectEntity> entities, ReportType type) {
    Workbook workBook = new XSSFWorkbook();
    log.debug("Create workbook, start creating document");
    Sheet sheet = workBook.createSheet();
    DataFormat format = workBook.createDataFormat();
    CellStyle dateStyle = workBook.createCellStyle();
    dateStyle.setDataFormat(format.getFormat(EXCEL_DATE_FORMAT));

    Row header = sheet.createRow(0);
    switch (type) {
      case WORKLOAD_REPORT -> createTableHeader(header, DEPARTMENT_COL, FULL_NAME_COL, PROJECT_COL,
          FROM_COL, TILL_COL);
      case UNOCCUPIED_REPORT -> createTableHeader(header, DEPARTMENT_COL, FULL_NAME_COL,
          PROJECT_COL, CANCEL_DATE_COL);
    }
    log.debug("Header of xml document was created");

    for (int i = 0; i < entities.size(); i++) {
      Row row = sheet.createRow(i + 1);
      UserProjectEntity entity = entities.get(i);

      createTableRow(row, entity, type, dateStyle);
    }
    log.debug("Data was added to document");
    autoSizeColumns(sheet, header);
    log.info("The document was successfully created");
    return workBook;
  }

  private void createTableHeader(Row row, String... columns) {
    for (int i = 0; i < columns.length; i++) {
      Cell cell = row.createCell(i);
      cell.setCellValue(columns[i]);
    }
  }

  private void autoSizeColumns(Sheet sheet, Row row) {
    int i = 0;
    while (i <= row.getPhysicalNumberOfCells()) {
      sheet.autoSizeColumn(i++);
    }
  }

  private void createTableRow(Row row, UserProjectEntity entity, ReportType type,
      CellStyle dateStyle) {
    UserEntity user = entity.getId().getUser();
    Cell department = row.createCell(0);
    department.setCellValue(user.getDepartment().getTitle());

    Cell name = row.createCell(1);
    String fullName = String.format("%s %s", user.getFirstName(), user.getLastName());
    name.setCellValue(fullName);

    Cell project = row.createCell(2);
    project.setCellValue(entity.getId().getProject().getName());

    switch (type) {
      case WORKLOAD_REPORT -> {
        Cell from = row.createCell(3);
        from.setCellStyle(dateStyle);
        from.setCellValue(entity.getAssignDate());

        Cell till = row.createCell(4);
        till.setCellStyle(dateStyle);
        till.setCellValue(entity.getCancelDate());
      }

      case UNOCCUPIED_REPORT -> {
        Cell cancelDate = row.createCell(3);
        cancelDate.setCellStyle(dateStyle);
        cancelDate.setCellValue(entity.getCancelDate());
      }
    }
  }
}
