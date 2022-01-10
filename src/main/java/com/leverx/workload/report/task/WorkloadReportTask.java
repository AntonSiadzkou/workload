package com.leverx.workload.report.task;

import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.userproject.repository.UserProjectRepository;
import com.leverx.workload.userproject.repository.entity.UserProjectEntity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class WorkloadReportTask {

  @Value("${report.dir}")
  private String dir;

  private final UserProjectRepository repository;

  private static final String DEPARTMENT_COL = "Department";
  private static final String FULL_NAME_COL = "Full Name";
  private static final String PROJECT_COL = "Project";
  private static final String FROM_COL = "From";
  private static final String TILL_COL = "Till";
  private static final String EXCEL_DATE_FORMAT = "dd.mm.yyyy";

  public WorkloadReportTask(UserProjectRepository repository) {
    this.repository = repository;
  }

  @Transactional(readOnly = true)
  @Scheduled(cron = "${report.workload.cron}")
  public void createWorkloadReport() {
    log.info("Start creating Workload report");
    File file =
        new File(String.format("%s/Workload-%s.xlsx", dir, LocalDate.now().getMonth().name()));
    log.info("Start writing xml-file: " + file);
    LocalDate now = LocalDate.now();
    LocalDate monthAgo = now.minusMonths(1);
    List<UserProjectEntity> entities = repository.findAllActiveProjectWithinPeriod(monthAgo, now);
    entities = entities.stream()
        .sorted(
            Comparator.comparingLong(entity -> entity.getId().getUser().getDepartment().getId()))
        .toList();
    log.info("User-Project entities loaded from database and sorted by departments");

    try (XSSFWorkbook workBook = new XSSFWorkbook();
        FileOutputStream fos = new FileOutputStream(file)) {
      log.debug("Create workbook, start creating document");
      Sheet sheet = workBook.createSheet();
      DataFormat format = workBook.createDataFormat();
      CellStyle dateStyle = workBook.createCellStyle();
      dateStyle.setDataFormat(format.getFormat(EXCEL_DATE_FORMAT));

      Row header = sheet.createRow(0);
      createTableHeader(header, DEPARTMENT_COL, FULL_NAME_COL, PROJECT_COL, FROM_COL, TILL_COL);
      log.debug("Header of xml document was created");

      for (int i = 0; i < entities.size(); i++) {
        Row row = sheet.createRow(i + 1);
        UserProjectEntity entity = entities.get(i);

        createWorkloadTableRow(row, entity, dateStyle);
      }
      log.debug("Data was added to document");
      int i = 0;
      while (i <= entities.size()) {
        sheet.autoSizeColumn(i++);
      }
      log.info("The document was successfully created, start writing into a file.");
      workBook.write(fos);
    } catch (IOException e) {
      log.error("Workload Report problem: " + e.getMessage());
    }
    log.info("Successfully created Workload report and saved it into a file: " + file);
  }

  private void createTableHeader(Row row, String... columns) {
    for (int i = 0; i < columns.length; i++) {
      Cell cell = row.createCell(i);
      cell.setCellValue(columns[i]);
    }
  }

  private void createWorkloadTableRow(Row row, UserProjectEntity entity, CellStyle dateStyle) {
    UserEntity user = entity.getId().getUser();
    Cell department = row.createCell(0);
    department.setCellValue(user.getDepartment().getTitle());

    Cell name = row.createCell(1);
    String fullName = String.format("%s %s", user.getFirstName(), user.getLastName());
    name.setCellValue(fullName);

    Cell project = row.createCell(2);
    project.setCellValue(entity.getId().getProject().getName());

    Cell from = row.createCell(3);
    from.setCellStyle(dateStyle);
    from.setCellValue(entity.getAssignDate());

    Cell till = row.createCell(4);
    till.setCellStyle(dateStyle);
    till.setCellValue(entity.getCancelDate());
  }
}
