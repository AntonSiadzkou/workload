package com.leverx.workload.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.leverx.workload.controller.request.UserRequest;
import com.leverx.workload.controller.response.UserResponse;
import com.leverx.workload.entity.UserEntity;
import com.leverx.workload.exception.UserNotExistException;
import com.leverx.workload.exception.UserWithSuchEmailExists;
import com.leverx.workload.mapper.UserMapper;
import com.leverx.workload.model.User;
import com.leverx.workload.repository.UserRepository;
import com.leverx.workload.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith({MockitoExtension.class})
class UserServiceImplTest {

  private UserService underTest;
  @Mock
  private UserRepository repository;
  @Mock
  private UserMapper mapper;
  @Mock
  private Pageable pageable;

  @BeforeEach
  void setUp() {
    pageable = PageRequest.of(1, 1);
    mapper = mock(UserMapper.class);
    repository = mock(UserRepository.class);
    underTest = new UserServiceImpl(repository, mapper);
  }

  @Test
  void FindByEmail() {
    UserResponse expected = new UserResponse();
    String email = "email@mail.com";
    expected.setEmail(email);
    given(mapper.toResponse(any())).willReturn(expected);
    given(repository.findByEmail(email)).willReturn(Optional.of(new UserEntity()));

    UserResponse actual = underTest.findByEmail(expected.getEmail());

    verify(repository).findByEmail(expected.getEmail());
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void findAllUsers() {
    UserEntity entity = new UserEntity();
    List<UserEntity> fromRepo = new ArrayList<>();
    fromRepo.add(entity);
    UserResponse response = new UserResponse();
    List<UserResponse> expected = new ArrayList<>();
    expected.add(response);
    Page<UserEntity> page = mock(Page.class);

    given(repository.findAll(pageable)).willReturn(page);
    given(page.getContent()).willReturn(fromRepo);
    given(mapper.toResponse(mapper.toModelFromEntity(entity))).willReturn(response);

    List<UserResponse> actual = underTest.findAllUsers(pageable);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void findByFirstNameIgnoreCaseContaining() {
    String name = "user";
    UserEntity entity = new UserEntity();
    List<UserEntity> fromRepo = new ArrayList<>();
    fromRepo.add(entity);
    UserResponse response = new UserResponse();
    List<UserResponse> expected = new ArrayList<>();
    expected.add(response);
    Page<UserEntity> page = mock(Page.class);

    given(repository.findByFirstNameIgnoreCaseContaining(name, pageable)).willReturn(page);
    given(page.getContent()).willReturn(fromRepo);
    given(mapper.toResponse(mapper.toModelFromEntity(entity))).willReturn(response);

    List<UserResponse> actual = underTest.findByFirstNameIgnoreCaseContaining(name, pageable);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void findById() {
    UserResponse expected = new UserResponse();
    long id = 3;
    expected.setId(id);
    given(mapper.toResponse(any())).willReturn(expected);
    given(repository.findById(id)).willReturn(Optional.of(new UserEntity()));

    UserResponse actual = underTest.findById(id);

    verify(repository).findById(id);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void createUser() {
    UserResponse expected = new UserResponse();
    long id = 3;
    expected.setId(id);
    UserRequest request = new UserRequest();
    User model = new User();
    UserEntity entity = new UserEntity();

    given(repository.findByEmail(any())).willReturn(Optional.empty());
    given(mapper.toModelFromRequest(request)).willReturn(model);
    given(mapper.toEntity(model)).willReturn(entity);
    given(repository.save(entity)).willReturn(entity);
    given(mapper.toResponse(any())).willReturn(expected);

    UserResponse actual = underTest.createUser(request);

    verify(repository).findByEmail(any());
    verify(repository).save(entity);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void createUserException() {
    String email = "test@mail.com";
    Exception exception = assertThrows(UserWithSuchEmailExists.class, () -> {
      UserRequest request = new UserRequest();
      request.setEmail(email);

      given(repository.findByEmail(email)).willReturn(Optional.of(new UserEntity()));

      underTest.createUser(request);
    });

    String expectedMessage = "Email = " + email + " already exists, email must be unique";
    String actualMessage = exception.getMessage();

    assertThat(actualMessage).contains(expectedMessage);
  }

  @Test
  void updateUser() {
    UserResponse expected = new UserResponse();
    long id = 3;
    expected.setId(id);
    UserRequest request = new UserRequest();
    request.setId(id);
    User model = new User();
    UserEntity entity = new UserEntity();

    given(repository.findById(id)).willReturn(Optional.of(entity));
    given(repository.findByEmail(any())).willReturn(Optional.empty());
    given(mapper.toModelFromRequest(request)).willReturn(model);
    given(mapper.toEntity(model)).willReturn(entity);
    given(repository.save(entity)).willReturn(entity);
    given(mapper.toResponse(any())).willReturn(expected);

    UserResponse actual = underTest.updateUser(request);

    verify(repository).findById(id);
    verify(repository).findByEmail(any());
    verify(repository).save(entity);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void updateUserExceptionUserNotExist() {
    Exception exception = assertThrows(UserNotExistException.class, () -> {
      UserRequest request = new UserRequest();
      request.setId(999);

      underTest.updateUser(request);
    });

    String expectedMessage = "Unable to update user. User doesn't exist.";
    String actualMessage = exception.getMessage();

    assertThat(actualMessage).contains(expectedMessage);
  }

  @Test
  void updateUserExceptionSuchEmailExists() {
    String email = "test@mail.com";
    Exception exception = assertThrows(UserWithSuchEmailExists.class, () -> {
      long id = 2;
      long id2 = 4;
      UserEntity entity = new UserEntity();
      entity.setId(id);
      entity.setEmail(email);
      UserEntity entity2 = new UserEntity();
      entity2.setId(id2);
      entity2.setEmail(email);
      UserRequest request = new UserRequest();
      request.setId(id);
      request.setEmail(email);

      given(repository.findById(id)).willReturn(Optional.of(entity));
      given(repository.findByEmail(email)).willReturn(Optional.of(entity2));

      underTest.updateUser(request);
    });

    String expectedMessage = "Email = " + email + " already exists, email must be unique";
    String actualMessage = exception.getMessage();

    assertThat(actualMessage).contains(expectedMessage);
  }
}
