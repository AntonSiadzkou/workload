package com.leverx.workload.project.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.leverx.workload.config.ApplicationConfig;
import com.leverx.workload.config.H2TestConfig;
import com.leverx.workload.config.MapperConfig;
import com.leverx.workload.config.WebConfig;
import com.leverx.workload.project.repository.entity.ProjectEntity;
import com.leverx.workload.project.repository.specification.ProjectSpecifications;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(
    classes = {ApplicationConfig.class, MapperConfig.class, WebConfig.class, H2TestConfig.class})
@WebAppConfiguration
@Sql("classpath:test-data.sql")
class ProjectRepositoryTest {

  @Autowired
  private ProjectRepository underTest;

  @Test
  void findAll() {}

  @Test
  void endDateAndQuantity_ProjectsExists_CorrectQuantity() {
    String endDate = "2022-11-01";
    int expected = 1;

    Page<ProjectEntity> actual =
        underTest.findAll(ProjectSpecifications.lessThanStartDate(endDate), PageRequest.of(0, 5));

    assertThat(actual.getTotalElements()).isEqualTo(expected);
  }
}
