package com.leverx.workload.department.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.leverx.workload.config.ApplicationConfig;
import com.leverx.workload.config.H2TestConfig;
import com.leverx.workload.config.MapperConfig;
import com.leverx.workload.config.WebConfig;
import com.leverx.workload.department.repository.entity.DepartmentEntity;
import com.leverx.workload.security.service.model.Role;
import com.leverx.workload.user.repository.entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(
    classes = {ApplicationConfig.class, MapperConfig.class, WebConfig.class, H2TestConfig.class})
@WebAppConfiguration
@Sql("classpath:test-data.sql")
class DepartmentRepositoryTest {

  @Autowired
  private DepartmentRepository underTest;

  @Test
  void title_DepartmentExists_EqualDepartments() {
    String title = "HR";
    DepartmentEntity expected = new DepartmentEntity(1L, "HR", null);
    UserEntity user = new UserEntity(1, "John", "Tudor", "mail1@joy.com", "pass24WQ", "junior",
        expected, Role.USER, true, new ArrayList<>());
    List<UserEntity> users = new ArrayList<>();
    users.add(user);
    expected.setUsers(users);

    DepartmentEntity actual = underTest.findByTitle(title).orElse(null);

    assertThat(actual).isEqualTo(expected);
  }
}
