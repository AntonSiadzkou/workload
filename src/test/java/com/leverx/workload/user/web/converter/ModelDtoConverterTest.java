package com.leverx.workload.user.web.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.leverx.workload.config.MapperConfig;
import com.leverx.workload.user.model.User;
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
class ModelDtoConverterTest {

  private ModelDtoConverter underTest;
  @Autowired
  private ModelMapper mapper;

  @BeforeEach
  void setUp() {
    underTest = new ModelDtoConverter(mapper);
  }

  @Test
  void modelAndResponse_IfTheSame_Equal() {
    User model = createUserModel();
    UserResponse expected = createUserResponse();

    UserResponse actual = underTest.toResponse(model);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void modelAndResponse_IfDifferent_NotEqual() {
    User model = createUserModel();
    model.setFirstName("NewName");
    UserResponse expected = createUserResponse();
    expected.setDepartment("AnotherDep");

    UserResponse actual = underTest.toResponse(model);

    assertThat(actual).isNotEqualTo(expected);
  }

  @Test
  void bodyParamsAndModel_IfTheSame_Equal() {
    UserBodyParams request = createUserBodyParam();
    User expected = createUserModel();

    User actual = underTest.toModelFromRequest(request);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void bodyParamsAndModel_IfDifferent_NotEqual() {
    UserBodyParams request = createUserBodyParam();
    request.setLastName("NewSurname");
    User expected = createUserModel();
    request.setPosition("new position");

    User actual = underTest.toModelFromRequest(request);

    assertThat(actual).isNotEqualTo(expected);
  }

  private UserBodyParams createUserBodyParam() {
    UserBodyParams user = new UserBodyParams();
    user.setId(11);
    user.setFirstName("John");
    user.setLastName("Admin");
    user.setEmail("email@mail.com");
    user.setPassword("password");
    user.setPosition("junior");
    user.setDepartment("PR");
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
    user.setDepartment("PR");
    user.setRole("user");
    user.setActive(true);
    return user;
  }

  private User createUserModel() {
    User user = new User();
    user.setId(11);
    user.setFirstName("John");
    user.setLastName("Admin");
    user.setEmail("email@mail.com");
    user.setPassword("password");
    user.setPosition("junior");
    user.setDepartment("PR");
    user.setRole("user");
    user.setActive(true);
    return user;
  }
}
