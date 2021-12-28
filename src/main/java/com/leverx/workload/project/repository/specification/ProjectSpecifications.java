package com.leverx.workload.project.repository.specification;

import com.leverx.workload.project.repository.entity.ProjectEntity;
import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;

public class ProjectSpecifications {
  private ProjectSpecifications() {
    throw new UnsupportedOperationException("Class instance can't be instantiated.");
  }

  public static Specification<ProjectEntity> greaterThanStartDate(String startDate) {
    return (projectEntity, criteriaQuery, criteriaBuilder) -> {
      if (startDate != null) {
        return criteriaBuilder.greaterThan(projectEntity.get("startDate"),
            LocalDate.parse(startDate));
      } else {
        return criteriaBuilder.and();
      }
    };
  }

  public static Specification<ProjectEntity> lessThanStartDate(String endDate) {
    return (projectEntity, criteriaQuery, criteriaBuilder) -> {
      if (endDate != null) {
        return criteriaBuilder.lessThan(projectEntity.get("endDate"), LocalDate.parse(endDate));
      } else {
        return criteriaBuilder.and();
      }
    };
  }
}
