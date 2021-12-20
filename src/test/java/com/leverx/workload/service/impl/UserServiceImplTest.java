package com.leverx.workload.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.leverx.workload.controller.response.UserResponse;
import com.leverx.workload.entity.UserEntity;
import com.leverx.workload.mapper.UserMapper;
import com.leverx.workload.repository.UserRepository;
import com.leverx.workload.service.UserService;
import java.util.ArrayList;
import java.util.List;
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
  @Mock private UserRepository repository;
  @Mock private UserMapper mapper;
  @Mock private Pageable pageable;

  @BeforeEach
  void setUp() {
    pageable = PageRequest.of(1, 1);
    mapper = mock(UserMapper.class);
    repository = mock(UserRepository.class);
    underTest = new UserServiceImpl(repository, mapper);
  }

  @Test
  void checkFindByEmail() {
    UserResponse expected = new UserResponse();
    String email = "email@mail.com";
    expected.setEmail(email);
    given(mapper.toResponse(any())).willReturn(expected);

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
}
