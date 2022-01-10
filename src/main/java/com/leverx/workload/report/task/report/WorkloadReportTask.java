package com.leverx.workload.report.task.report;

import com.leverx.workload.report.task.ReportType;
import com.leverx.workload.report.task.creator.TaskTableCreator;
import com.leverx.workload.userproject.repository.UserProjectRepository;
import com.leverx.workload.userproject.repository.entity.UserProjectEntity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
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
  private final TaskTableCreator creator;

  public WorkloadReportTask(UserProjectRepository repository, TaskTableCreator creator) {
    this.repository = repository;
    this.creator = creator;
  }

  @Scheduled(cron = "${report.workload.cron}")
  public void writeWorkloadReport() {
    File file =
        new File(String.format("%s/Workload-%s.xlsx", dir, LocalDate.now().getMonth().name()));
    log.info("Start writing xlsx-file: " + file);
    try (Workbook workBook = createWorkloadReport();
        FileOutputStream fos = new FileOutputStream(file)) {
      log.info("Start writing workbook-data into a file");
      workBook.write(fos);
    } catch (IOException e) {
      log.error("Workload Report problem: " + e.getMessage());
    }
    log.info("Successfully created Workload report and saved it into a file: " + file);
  }

  @Transactional(readOnly = true)
  public Workbook createWorkloadReport() {
    log.info("Start creating Workload report");
    LocalDate now = LocalDate.now();
    LocalDate monthAgo = now.minusMonths(1);
    List<UserProjectEntity> entities = repository.findAllActiveProjectWithinPeriod(monthAgo, now);
    entities = entities.stream()
        .sorted(
            Comparator.comparingLong(entity -> entity.getId().getUser().getDepartment().getId()))
        .toList();
    log.info("User-Project entities loaded from database and sorted by departments");
    Workbook workbook = creator.createFilledWorkbook(entities, ReportType.WORKLOAD_REPORT);
    log.info("Workload report was successfully created");
    return workbook;
  }
}
