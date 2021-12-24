package com.leverx.workload.user.service.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.leverx.workload.config.MapperConfig;
import com.leverx.workload.department.repository.entity.DepartmentEntity;
import com.leverx.workload.department.web.dto.responce.DepartmentResponse;
import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.user.web.dto.request.UserBodyParams;
import com.leverx.workload.user.web.dto.response.UserResponse;
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
class EntityModelConverterTest {

  private UserConverter underTest;
  @Autowired
  private ModelMapper mapper;

  @BeforeEach
  void setUp() {
    underTest = new UserConverter(mapper);
  }

  @Test
  void bodyParamsAndEntity_IfTheSame_Equal() {
    UserBodyParams params = createUserBodyParam();
    UserEntity expected = createUserEntity();

    UserEntity actual = underTest.toEntity(params);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void bodyParamsAndEntity_IfDifferent_NotEqual() {
    UserBodyParams params = createUserBodyParam();
    params.setFirstName("Aliona");
    UserEntity expected = createUserEntity();
    expected.setLastName("OneMoreName");

    UserEntity actual = underTest.toEntity(params);

    assertThat(actual).isNotEqualTo(expected);
  }

  @Test
  void entityAndResponse_IfTheSame_Equal() {
    UserEntity entity = createUserEntity();
    UserResponse expected = createUserResponse();

    UserResponse actual = underTest.toResponse(entity);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void entityAndResponse_IfDifferent_NotEqual() {
    UserEntity entity = createUserEntity();
    entity.setFirstName("NewName");
    UserResponse expected = createUserResponse();
    expected.setLastName("AnotherName");

    UserResponse actual = underTest.toResponse(entity);

    assertThat(actual).isNotEqualTo(expected);
  }

  private UserBodyParams createUserBodyParam() {
    UserBodyParams user = new UserBodyParams();
    user.setId(11L);
    user.setFirstName("John");
    user.setLastName("Admin");
    user.setEmail("email@mail.com");
    user.setPassword("pass24ER");
    user.setPosition("junior");
    user.setDepartment(new DepartmentResponse());
    user.setRole("user");
    user.setActive(true);
    return user;
  }

  private UserResponse createUserResponse() {
    UserResponse user = new UserResponse();
    user.setId(11);
    user.setFirstName("John");
    user.setLastName("Admin");
    user.setEmail("email@mail.com");
    user.setPosition("junior");
    user.setDepartment(new DepartmentResponse());
    user.setRole("user");
    user.setActive(true);
    return user;
  }

  private UserEntity createUserEntity() {
    UserEntity user = new UserEntity();
    user.setId(11);
    user.setFirstName("John");
    user.setLastName("Admin");
    user.setEmail("email@mail.com");
    user.setPassword("pass24ER");
    user.setPosition("junior");
    user.setDepartment(new DepartmentEntity());
    user.setRole("user");
    user.setActive(true);
    return user;
  }
}
