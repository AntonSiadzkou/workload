package com.leverx.workload.service.impl;

import com.leverx.workload.controller.response.UserResponse;
import com.leverx.workload.entity.UserEntity;
import com.leverx.workload.mapper.UserMapper;
import com.leverx.workload.repository.UserRepository;
import com.leverx.workload.service.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository repository;
  private final UserMapper mapper;

  @Override
  public UserResponse findByEmail(String email) {
    return mapper.toResponse(mapper.toModelFromEntity(repository.findByEmail(email)));
  }

  @Override
  public List<UserResponse> findAllUsers(Pageable pageable) {
    Page<UserEntity> entities = repository.findAll(pageable);
    return entities.getContent().stream()
        .map((e) -> mapper.toResponse(mapper.toModelFromEntity(e)))
        .toList();
  }

  @Override
  public List<UserResponse> findByFirstNameIgnoreCaseContaining(
      String firstName, Pageable pageable) {
    Page<UserEntity> entities = repository.findByFirstNameIgnoreCaseContaining(firstName, pageable);
    return entities.getContent().stream()
        .map((e) -> mapper.toResponse(mapper.toModelFromEntity(e)))
        .toList();
  }
}
