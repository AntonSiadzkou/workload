package com.leverx.workload.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.leverx.workload.config.MapperConfig;
import com.leverx.workload.controller.request.UserRequest;
import com.leverx.workload.controller.response.UserResponse;
import com.leverx.workload.entity.UserEntity;
import com.leverx.workload.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {MapperConfig.class})
class UserMapperTest {

  private UserMapper underTest;
  @Autowired private ModelMapper mapper;

  @BeforeEach
  void setUp() {
    underTest = new UserMapper(mapper);
  }

  @Test
  void toEntityAssertTrue() {
    User model =
        new User("John", "Admin", "email@mail.com", "password", "junior", "PR", "user", true);
    UserEntity expected =
        new UserEntity("John", "Admin", "email@mail.com", "password", "junior", "PR", "user", true);

    UserEntity actual = underTest.toEntity(model);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void toEntityAssertFalse() {
    User model =
        new User("John", "Admin", "email@mail.com", "password", "junior", "PR", "user", true);
    UserEntity expected =
        new UserEntity("Ivan", "Admin", "email@mail.com", "password", "junior", "PR", "user", true);

    UserEntity actual = underTest.toEntity(model);

    assertThat(actual).isNotEqualTo(expected);
  }

  @Test
  void toModelFromEntityAssertTrue() {
    UserEntity entity =
        new UserEntity("John", "Admin", "email@mail.com", "password", "junior", "PR", "user", true);
    User expected =
        new User("John", "Admin", "email@mail.com", "password", "junior", "PR", "user", true);

    User actual = underTest.toModelFromEntity(entity);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void toModelFromEntityAssertFalse() {
    UserEntity entity =
        new UserEntity("John", "Admin", "email@mail.com", "password", "junior", "PR", "user", true);
    User expected =
        new User("John", "Ivanov", "email@mail.com", "password", "junior", "PR", "user", true);

    User actual = underTest.toModelFromEntity(entity);

    assertThat(actual).isNotEqualTo(expected);
  }

  @Test
  void toResponseAssertTrue() {
    User model =
        new User("John", "Admin", "email@mail.com", "password", "junior", "PR", "user", true);
    UserResponse expected =
        new UserResponse("John", "Admin", "email@mail.com", "junior", "PR", "user", true);

    UserResponse actual = underTest.toResponse(model);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void toResponseAssertFalse() {
    User model =
        new User("John", "Admin", "email@mail.com", "password", "junior", "PR", "user", true);
    UserResponse expected =
        new UserResponse("John", "Admin", "another@mail.com", "junior", "PR", "user", true);

    UserResponse actual = underTest.toResponse(model);

    assertThat(actual).isNotEqualTo(expected);
  }

  @Test
  void toModelFromRequestAssertTrue() {
    UserRequest request =
        new UserRequest(
            "John", "Admin", "email@mail.com", "password", "junior", "PR", "user", true);
    User expected =
        new User("John", "Admin", "email@mail.com", "password", "junior", "PR", "user", true);

    User actual = underTest.toModelFromRequest(request);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void toModelFromRequestAssertFalse() {
    UserRequest request =
        new UserRequest(
            "John", "Admin", "email@mail.com", "password", "junior", "PR", "user", true);
    User expected =
        new User("John", "Admin", "email@mail.com", "password", "middle", "HR", "admin", true);

    User actual = underTest.toModelFromRequest(request);

    assertThat(actual).isNotEqualTo(expected);
  }
}
