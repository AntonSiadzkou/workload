package com.leverx.workload.report.task.report;

import com.leverx.workload.report.task.ReportType;
import com.leverx.workload.report.task.creator.TaskTableCreator;
import com.leverx.workload.userproject.repository.UserProjectRepository;
import com.leverx.workload.userproject.repository.entity.UserProjectEntity;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@AllArgsConstructor
public class UnoccupiedReportTask {

  private final UserProjectRepository repository;
  private final TaskTableCreator creator;

  @Transactional(readOnly = true)
  public Workbook createUnoccupiedReport() {
    log.info("Start creating Unoccupied report.");
    LocalDate now = LocalDate.now();
    LocalDate nextMonth = now.plusMonths(1);
    List<UserProjectEntity> entities = repository.findAllByCancelDateBetween(now, nextMonth);
    entities = entities.stream()
        .sorted(
            Comparator.comparingLong(entity -> entity.getId().getUser().getDepartment().getId()))
        .toList();
    log.info("User-Project entities loaded from database and sorted by departments");
    Workbook workbook = creator.createFilledWorkbook(entities, ReportType.UNOCCUPIED_REPORT);
    log.info("Unoccupied report was successfully created");
    return workbook;
  }
}
