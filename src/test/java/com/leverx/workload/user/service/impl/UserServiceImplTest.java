package com.leverx.workload.user.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.leverx.workload.user.exception.DuplicatedEmailException;
import com.leverx.workload.user.exception.UserNotExistException;
import com.leverx.workload.user.repository.UserRepository;
import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.user.service.UserService;
import com.leverx.workload.user.service.converter.UserConverter;
import com.leverx.workload.user.web.dto.request.UserBodyParams;
import com.leverx.workload.user.web.dto.request.UserRequestParams;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith({MockitoExtension.class})
class UserServiceImplTest {

  private UserService underTest;
  @Mock
  private UserRepository repository;
  @Mock
  private UserConverter mapper;

  @BeforeEach
  void setUp() {
    mapper = mock(UserConverter.class);
    repository = mock(UserRepository.class);
    underTest = new UserServiceImpl(repository, mapper);
  }

  @Test
  void email_FindAllUsers_ListOfUser() {
    String email = "email@mail.com";
    UserEntity entity = new UserEntity();
    entity.setEmail(email);
    List<UserEntity> expected = new ArrayList<>();
    expected.add(entity);
    UserRequestParams params = new UserRequestParams(null, email, 1, 1, "lastName", "asc");
    Page<UserEntity> page = mock(Page.class);

    given(repository.findAll(any(Specification.class), any(Pageable.class))).willReturn(page);
    given(page.getContent()).willReturn(expected);

    List<UserEntity> actual = underTest.findAllUsers(params);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void pageable_FindAllUsers_ListOfUsers() {
    UserEntity entity = new UserEntity();
    List<UserEntity> expected = new ArrayList<>();
    expected.add(entity);
    UserRequestParams params = new UserRequestParams(null, null, 1, 1, "lastName", "asc");
    Page<UserEntity> page = mock(Page.class);

    given(repository.findAll(any(Specification.class), any(Pageable.class))).willReturn(page);
    given(page.getContent()).willReturn(expected);

    List<UserEntity> actual = underTest.findAllUsers(params);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void pageableAndFirstName_FindAllUsers_ListOfUsers() {
    String name = "user";
    UserEntity entity = new UserEntity();
    List<UserEntity> expected = new ArrayList<>();
    expected.add(entity);
    UserRequestParams params = new UserRequestParams(name, null, 1, 1, "firstName", "desc");
    Page<UserEntity> page = mock(Page.class);

    given(repository.findAll(any(Specification.class), any(Pageable.class))).willReturn(page);
    given(page.getContent()).willReturn(expected);

    List<UserEntity> actual = underTest.findAllUsers(params);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void id_findById_User() {
    long id = 3;
    UserEntity expected = new UserEntity();
    expected.setId(id);

    given(repository.findById(id)).willReturn(Optional.of(expected));

    UserEntity actual = underTest.findById(id);

    verify(repository).findById(id);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void validUser_CreateUser_Created() {
    long expected = 3;
    UserBodyParams request = new UserBodyParams();
    request.setId(expected);
    UserEntity entity = new UserEntity();
    entity.setId(expected);

    given(repository.findByEmail(any())).willReturn(Optional.empty());
    given(mapper.toEntity(request)).willReturn(entity);
    given(repository.save(entity)).willReturn(entity);

    long actual = underTest.createUser(request);

    verify(repository).findByEmail(any());
    verify(repository).save(entity);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void userWithDuplicatedEmail_CreateUser_Exception() {
    String email = "test@mail.com";
    Exception exception = assertThrows(DuplicatedEmailException.class, () -> {
      UserBodyParams params = new UserBodyParams();
      params.setEmail(email);

      given(repository.findByEmail(email)).willReturn(Optional.of(new UserEntity()));

      underTest.createUser(params);
    });

    String expectedMessage = "Email = " + email + " already exists, email must be unique";
    String actualMessage = exception.getMessage();

    assertThat(actualMessage).contains(expectedMessage);
  }

  @Test
  void validUser_UpdateUser_Updated() {
    long id = 3;
    UserBodyParams request = new UserBodyParams();
    request.setId(id);
    UserEntity entity = new UserEntity();
    entity.setId(id);

    given(repository.findById(id)).willReturn(Optional.of(entity));
    given(repository.findByEmail(any())).willReturn(Optional.empty());
    given(mapper.toEntity(request)).willReturn(entity);

    underTest.updateUser(request);

    verify(repository).findById(id);
    verify(repository).findByEmail(any());
    verify(repository).save(entity);
  }

  @Test
  void notExistedUser_UpdateUser_Exception() {
    Exception exception = assertThrows(UserNotExistException.class, () -> {
      UserBodyParams user = new UserBodyParams();
      user.setId(999L);

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
      UserBodyParams params = new UserBodyParams();
      params.setId(2L);
      params.setEmail(email);

      given(repository.findById(id)).willReturn(Optional.of(entity));
      given(repository.findByEmail(email)).willReturn(Optional.of(entity2));

      underTest.updateUser(params);
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
