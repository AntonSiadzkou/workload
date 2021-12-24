package com.leverx.workload.user.service.impl;

import com.leverx.workload.user.exception.DuplicatedEmailException;
import com.leverx.workload.user.exception.UserNotExistException;
import com.leverx.workload.user.repository.UserRepository;
import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.user.repository.specification.Specifications;
import com.leverx.workload.user.service.UserService;
import com.leverx.workload.user.service.converter.UserConverter;
import com.leverx.workload.user.web.dto.request.UserBodyParams;
import com.leverx.workload.user.web.dto.request.UserRequestParams;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Validated
public class UserServiceImpl implements UserService {

  private final UserRepository repository;
  private final UserConverter mapper;

  @Override
  @Transactional(readOnly = true)
  public List<UserEntity> findAllUsers(@NotNull UserRequestParams params) {
    Pageable pageable = PageRequest.of(params.page(), params.size(),
        Sort.by(new Order(getSortDirection(params.sortDirection()), params.sortColumn())));

    Specification<UserEntity> spec = Specification.where(Specifications.hasEmail(params.email())
        .and(Specifications.hasFirstName(params.firstName())));

    Page<UserEntity> pageData = repository.findAll(spec, pageable);
    return pageData.getContent();
  }

  @Override
  @Transactional(readOnly = true)
  public UserEntity findById(@NotNull Long id) {
    return repository.findById(id).orElseThrow(
        () -> new UserNotExistException(String.format("User with id=%s not found", id)));
  }

  @Override
  @Transactional
  public long createUser(@NotNull UserBodyParams user) {
    if (repository.findByEmail(user.getEmail()).isPresent()) {
      throw new DuplicatedEmailException(
          String.format("Email = %s already exists, email must be unique", user.getEmail()));
    }
    return repository.save(mapper.toEntity(user)).getId();
  }

  @Override
  @Transactional
  public void updateUser(@NotNull UserBodyParams user) {
    UserEntity entity = repository.findById(user.getId())
        .orElseThrow(() -> new UserNotExistException("Unable to update user. User doesn't exist."));
    long anotherId = repository.findByEmail(user.getEmail()).orElse(entity).getId();
    if (entity.getId() != anotherId) {
      throw new DuplicatedEmailException(
          String.format("Email = %s already exists, email must be unique", user.getEmail()));
    }
    repository.save(mapper.toEntity(user));
  }

  @Override
  @Transactional
  public void deleteUserById(@NotNull Long id) {
    repository.findById(id)
        .orElseThrow(() -> new UserNotExistException("Unable to delete user. User doesn't exist."));
    repository.deleteById(id);
  }

  private Sort.Direction getSortDirection(String direction) {
    return (direction.equals("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;
  }
}
