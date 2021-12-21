package com.leverx.workload.user.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.leverx.workload.user.exception.DuplicatedEmailException;
import com.leverx.workload.user.exception.UserNotExistException;
import com.leverx.workload.user.model.User;
import com.leverx.workload.user.repository.UserRepository;
import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.user.service.UserService;
import com.leverx.workload.user.service.converter.EntityModelConverter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

@ExtendWith({MockitoExtension.class})
class UserServiceImplTest {

  private UserService underTest;
  @Mock
  private UserRepository repository;
  @Mock
  private EntityModelConverter mapper;
  @Mock
  private Validator validator;

  @BeforeEach
  void setUp() {
    mapper = mock(EntityModelConverter.class);
    repository = mock(UserRepository.class);
    underTest = new UserServiceImpl(repository, mapper, validator);
  }

  @Test
  void email_FindAllUsers_ListOfUser() {
    User user = new User();
    String email = "email@mail.com";
    user.setEmail(email);
    UserEntity entity = new UserEntity();
    entity.setEmail(email);
    List<User> expected = new ArrayList<>();
    expected.add(user);

    given(repository.findByEmail(email)).willReturn(Optional.of(entity));
    given(mapper.toModelFromEntity(entity)).willReturn(user);

    List<User> actual = underTest.findAllUsers(null, email, 0, 0, null);

    verify(repository).findByEmail(email);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void pageable_FindAllUsers_ListOfUsers() {
    UserEntity entity = new UserEntity();
    List<UserEntity> fromRepo = new ArrayList<>();
    fromRepo.add(entity);
    User user = new User();
    List<User> expected = new ArrayList<>();
    expected.add(user);
    Pageable pageable = PageRequest.of(1, 1, Sort.by(new Order(Direction.ASC, "lastName")));
    Page<UserEntity> page = mock(Page.class);

    given(repository.findAll(pageable)).willReturn(page);
    given(page.getContent()).willReturn(fromRepo);
    given(mapper.toModelFromEntity(entity)).willReturn(user);

    List<User> actual = underTest.findAllUsers(null, null, 1, 1, new String[] {"lastName", "asc"});

    verify(repository).findAll(pageable);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void pageableAndFirstName_FindAllUsers_ListOfUsers() {
    String name = "user";
    UserEntity entity = new UserEntity();
    List<UserEntity> fromRepo = new ArrayList<>();
    fromRepo.add(entity);
    User user = new User();
    List<User> expected = new ArrayList<>();
    expected.add(user);
    Pageable pageable = PageRequest.of(1, 1, Sort.by(new Order(Direction.DESC, "firstName")));
    Page<UserEntity> page = mock(Page.class);

    given(repository.findByFirstNameIgnoreCaseContaining(name, pageable)).willReturn(page);
    given(page.getContent()).willReturn(fromRepo);
    given(mapper.toModelFromEntity(entity)).willReturn(user);

    List<User> actual =
        underTest.findAllUsers(name, null, 1, 1, new String[] {"firstName", "desc"});

    verify(repository).findByFirstNameIgnoreCaseContaining(name, pageable);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void id_findById_User() {
    User expected = new User();
    long id = 3;
    expected.setId(id);
    UserEntity entity = new UserEntity();

    given(repository.findById(id)).willReturn(Optional.of(entity));
    given(mapper.toModelFromEntity(entity)).willReturn(expected);

    User actual = underTest.findById(id);

    verify(repository).findById(id);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void validUser_CreateUser_Created() {
    User expected = new User();
    long id = 3;
    expected.setId(id);
    User request = new User();
    UserEntity entity = new UserEntity();

    given(repository.findByEmail(any())).willReturn(Optional.empty());
    given(mapper.toEntity(request)).willReturn(entity);
    given(repository.save(entity)).willReturn(entity);
    given(mapper.toModelFromEntity(entity)).willReturn(expected);

    User actual = underTest.createUser(request);

    verify(repository).findByEmail(any());
    verify(repository).save(entity);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void userWithDuplicatedEmail_CreateUser_Exception() {
    String email = "test@mail.com";
    Exception exception = assertThrows(DuplicatedEmailException.class, () -> {
      User request = new User();
      request.setEmail(email);

      given(repository.findByEmail(email)).willReturn(Optional.of(new UserEntity()));

      underTest.createUser(request);
    });

    String expectedMessage = "Email = " + email + " already exists, email must be unique";
    String actualMessage = exception.getMessage();

    assertThat(actualMessage).contains(expectedMessage);
  }

  @Test
  void validUser_UpdateUser_Updated() {
    User expected = new User();
    long id = 3;
    expected.setId(id);
    User request = new User();
    request.setId(id);
    UserEntity entity = new UserEntity();

    given(repository.findById(id)).willReturn(Optional.of(entity));
    given(repository.findByEmail(any())).willReturn(Optional.empty());
    given(mapper.toEntity(request)).willReturn(entity);
    given(repository.save(entity)).willReturn(entity);
    given(mapper.toModelFromEntity(entity)).willReturn(expected);

    User actual = underTest.updateUser(request);

    verify(repository).findById(id);
    verify(repository).findByEmail(any());
    verify(repository).save(entity);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void notExistedUser_UpdateUser_Exception() {
    Exception exception = assertThrows(UserNotExistException.class, () -> {
      User user = new User();
      user.setId(999);

      underTest.updateUser(user);
    });

    String expectedMessage = "Unable to update user. User doesn't exist.";
    String actualMessage = exception.getMessage();

    assertThat(actualMessage).contains(expectedMessage);
  }

  @Test
  void userWithDuplicatedEmail_UpdateUser_Exception() {
    String email = "test@mail.com";
    Exception exception = assertThrows(DuplicatedEmailException.class, () -> {
      long id = 2;
      long id2 = 4;
      UserEntity entity = new UserEntity();
      entity.setId(id);
      entity.setEmail(email);
      UserEntity entity2 = new UserEntity();
      entity2.setId(id2);
      entity2.setEmail(email);
      User user = new User();
      user.setId(id);
      user.setEmail(email);

      given(repository.findById(id)).willReturn(Optional.of(entity));
      given(repository.findByEmail(email)).willReturn(Optional.of(entity2));

      underTest.updateUser(user);
    });

    String expectedMessage = "Email = " + email + " already exists, email must be unique";
    String actualMessage = exception.getMessage();

    assertThat(actualMessage).contains(expectedMessage);
  }

  @Test
  void validId_DeleteUser_Deleted() {
    long id = 3;

    given(repository.findById(id)).willReturn(Optional.of(new UserEntity()));

    underTest.deleteUserById(id);

    verify(repository).findById(id);
    verify(repository).deleteById(id);
  }

  @Test
  void notExistedUserId_DeleteUser_Exception() {
    Exception exception = assertThrows(UserNotExistException.class, () -> {
      long id = 999;

      underTest.deleteUserById(id);
    });

    String expectedMessage = "Unable to delete user. User doesn't exist.";
    String actualMessage = exception.getMessage();

    assertThat(actualMessage).contains(expectedMessage);
  }
}
