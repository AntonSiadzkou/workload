package com.leverx.workload.user.service.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.leverx.workload.config.MapperConfig;
import com.leverx.workload.user.model.User;
import com.leverx.workload.user.repository.entity.UserEntity;
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

  private EntityModelConverter underTest;
  @Autowired
  private ModelMapper mapper;

  @BeforeEach
  void setUp() {
    underTest = new EntityModelConverter(mapper);
  }

  @Test
  void modelAndEntity_IfTheSame_Equal() {
    User model = createUserModel();
    UserEntity expected = createUserEntity();

    UserEntity actual = underTest.toEntity(model);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void modelAndEntity_IfDifferent_NotEqual() {
    User model = createUserModel();
    model.setFirstName("Aliona");
    UserEntity expected = createUserEntity();
    expected.setDepartment("OneMoreDep");

    UserEntity actual = underTest.toEntity(model);

    assertThat(actual).isNotEqualTo(expected);
  }

  @Test
  void entityAndModel_IfTheSame_Equal() {
    UserEntity entity = createUserEntity();
    User expected = createUserModel();

    User actual = underTest.toModelFromEntity(entity);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void entityAndModel_IfDifferent_NotEqual() {
    UserEntity entity = createUserEntity();
    entity.setLastName("LastKnight");
    User expected = createUserModel();
    expected.setEmail("batman@mail.ru");

    User actual = underTest.toModelFromEntity(entity);

    assertThat(actual).isNotEqualTo(expected);
  }

  private User createUserModel() {
    User user = new User();
    user.setId(11);
    user.setFirstName("John");
    user.setLastName("Admin");
    user.setEmail("email@mail.com");
    user.setPassword("pass24ER");
    user.setPosition("junior");
    user.setDepartment("PR");
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
    user.setDepartment("PR");
    user.setRole("user");
    user.setActive(true);
    return user;
  }
}
