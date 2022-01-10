package com.leverx.workload.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.leverx.workload.config.ApplicationConfig;
import com.leverx.workload.config.H2TestConfig;
import com.leverx.workload.config.MapperConfig;
import com.leverx.workload.config.WebConfig;
import com.leverx.workload.department.repository.entity.DepartmentEntity;
import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.user.repository.specification.UserSpecifications;
import java.util.ArrayList;
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
class UserRepositoryTest {

  @Autowired
  private UserRepository underTest;

  @Test
  void emailAndUser_UserExistsAndTheSame_EqualUsers() {
    String email = "mail3@joy.com";
    UserEntity expected = new UserEntity(3, "John", "Archibald", email, "pass24WQ", "senior",
        new DepartmentEntity(), "admin", true, new ArrayList<>());

    UserEntity actual = underTest.findByEmail(email).orElse(null);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void nameAndQuantity_UsersExists_CorrectQuantity() {
    String name = "John";
    int expected = 3;

    Page<UserEntity> actual =
        underTest.findAll(UserSpecifications.hasFirstName(name), PageRequest.of(0, 5));

    assertThat(actual.getTotalElements()).isEqualTo(expected);
  }

  @Test
  void idAndUser_UserExistsAndTheSame_EqualUsers() {
    long id = 2;
    UserEntity expected = new UserEntity(2, "Zoey", "Aco", "mail2@joy.com", "pass24WQ", "lead",
        new DepartmentEntity(), "user", true, new ArrayList<>());

    UserEntity actual = underTest.findById(id).orElse(null);

    assertThat(actual).isEqualTo(expected);
  }
}
