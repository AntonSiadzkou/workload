package com.leverx.workload.user.service.impl;

import com.leverx.workload.exception.DuplicatedValueException;
import com.leverx.workload.user.repository.UserRepository;
import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.user.repository.specification.UserSpecifications;
import com.leverx.workload.user.service.UserService;
import com.leverx.workload.user.service.converter.UserConverter;
import com.leverx.workload.user.web.dto.request.UserBodyParams;
import com.leverx.workload.user.web.dto.request.UserRequestParams;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository repository;
  private final UserConverter mapper;

  @Override
  @Transactional(readOnly = true)
  public List<UserEntity> findAllUsers(@NotNull UserRequestParams params) {
    Pageable pageable = PageRequest.of(params.page(), params.size(),
        Sort.by(new Order(getSortDirection(params.sortDirection()), params.sortColumn())));
    Specification<UserEntity> spec = Specification.where(UserSpecifications.hasEmail(params.email())
        .and(UserSpecifications.hasFirstName(params.firstName())));
    log.info("Searching all users with pagination and specifications");
    Page<UserEntity> pageData = repository.findAll(spec, pageable);
    log.info("Successfully found all users");
    return pageData.getContent();
  }

  @Override
  @Transactional(readOnly = true)
  public UserEntity findById(@NotNull Long id) {
    log.info("Searching a user with id:" + id);
    UserEntity user = repository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(String.format("User with id=%s not found", id)));
    log.info("Successfully found a user with id:" + id);
    return user;
  }

  @Override
  @Transactional
  public long createUser(@NotNull UserBodyParams user) {
    log.info("Creating a new user");
    if (repository.findByEmail(user.getEmail()).isPresent()) {
      throw new DuplicatedValueException(
          String.format("Email = %s already exists, email must be unique", user.getEmail()));
    }
    long id = repository.save(mapper.toEntity(user)).getId();
    log.info("Successfully created a user with id:" + id);
    return id;
  }

  @Override
  @Transactional
  public void updateUser(@NotNull UserBodyParams user) {
    log.info("Updating a user with id: " + user.getId());
    UserEntity entity = repository.findById(user.getId()).orElseThrow(
        () -> new EntityNotFoundException("Unable to update user. User doesn't exist."));
    long anotherId = repository.findByEmail(user.getEmail()).orElse(entity).getId();
    if (entity.getId() != anotherId) {
      throw new DuplicatedValueException(
          String.format("Email = %s already exists, email must be unique", user.getEmail()));
    }
    repository.save(mapper.toEntity(user));
    log.info("Successfully updated a user with id:" + entity.getId());
  }

  @Override
  @Transactional
  public void deleteUserById(@NotNull Long id) {
    log.info("Deleting a user with id: " + id);
    repository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Unable to delete user. User doesn't exist."));
    repository.deleteById(id);
    log.info("Successfully deleted a user with id:" + id);
  }

  private Sort.Direction getSortDirection(String direction) {
    return (direction.equals("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;
  }
}
