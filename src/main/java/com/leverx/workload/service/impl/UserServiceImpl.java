package com.leverx.workload.service.impl;

import com.leverx.workload.controller.request.UserRequest;
import com.leverx.workload.controller.response.UserResponse;
import com.leverx.workload.entity.UserEntity;
import com.leverx.workload.exception.UserNotExistException;
import com.leverx.workload.exception.UserWithSuchEmailExists;
import com.leverx.workload.mapper.UserMapper;
import com.leverx.workload.repository.UserRepository;
import com.leverx.workload.service.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository repository;
  private final UserMapper mapper;

  @Override
  public UserResponse findByEmail(String email) {
    return mapper.toResponse(mapper.toModelFromEntity(repository.findByEmail(email).orElseThrow(
        () -> new UserNotExistException(String.format("User with email=%s not found", email)))));
  }

  @Override
  public List<UserResponse> findAllUsers(Pageable pageable) {
    Page<UserEntity> entities = repository.findAll(pageable);
    return entities.getContent().stream().map((e) -> mapper.toResponse(mapper.toModelFromEntity(e)))
        .toList();
  }

  @Override
  public List<UserResponse> findByFirstNameIgnoreCaseContaining(String firstName,
      Pageable pageable) {
    Page<UserEntity> entities = repository.findByFirstNameIgnoreCaseContaining(firstName, pageable);
    return entities.getContent().stream().map((e) -> mapper.toResponse(mapper.toModelFromEntity(e)))
        .toList();
  }

  @Override
  public UserResponse findById(long id) {
    return mapper.toResponse(mapper.toModelFromEntity(repository.findById(id).orElseThrow(
        () -> new UserNotExistException(String.format("User with id=%s not found", id)))));
  }

  @Override
  @Transactional
  public UserResponse createUser(UserRequest user) {
    if (repository.findByEmail(user.getEmail()).isPresent()) {
      throw new UserWithSuchEmailExists(
          String.format("Email = %s already exists, email must be unique", user.getEmail()));
    }
    return mapper.toResponse(mapper
        .toModelFromEntity(repository.save(mapper.toEntity(mapper.toModelFromRequest(user)))));
  }
}
