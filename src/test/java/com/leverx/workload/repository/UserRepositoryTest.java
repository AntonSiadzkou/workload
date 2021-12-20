package com.leverx.workload.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.leverx.workload.config.ApplicationConfig;
import com.leverx.workload.config.H2TestConfig;
import com.leverx.workload.config.LiquibaseConfig;
import com.leverx.workload.config.MapperConfig;
import com.leverx.workload.config.WebConfig;
import com.leverx.workload.entity.UserEntity;
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
@ContextConfiguration(classes = {ApplicationConfig.class, MapperConfig.class, LiquibaseConfig.class,
    WebConfig.class, H2TestConfig.class})
@WebAppConfiguration
@Sql("classpath:test-data.sql")
class UserRepositoryTest {

  @Autowired
  private UserRepository underTest;

  @Test
  void findByEmail() {
    String email = "mail3@joy.com";
    UserEntity expected =
        new UserEntity("John", "Archibald", email, "pass", "senior", "IT", "admin", true);
    expected.setId(3);

    UserEntity actual = underTest.findByEmail(email).get();

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void findByFirstNameIgnoreCaseContaining() {
    String name = "John";
    int expected = 3;

    Page<UserEntity> actual =
        underTest.findByFirstNameIgnoreCaseContaining(name, PageRequest.of(0, 5));

    assertThat(actual.getTotalElements()).isEqualTo(expected);
  }

  @Test
  void findById() {
    long id = 2;
    UserEntity expected =
        new UserEntity("Zoey", "Aco", "mail2@joy.com", "pass", "lead", "IT", "user", true);
    expected.setId(id);

    UserEntity actual = underTest.findById(id).get();

    assertThat(actual).isEqualTo(expected);
  }
}
