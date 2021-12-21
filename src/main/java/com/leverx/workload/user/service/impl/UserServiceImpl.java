package com.leverx.workload.user.service.impl;

import com.leverx.workload.user.exception.DuplicatedEmailException;
import com.leverx.workload.user.exception.NotValidUserException;
import com.leverx.workload.user.exception.UserNotExistException;
import com.leverx.workload.user.model.User;
import com.leverx.workload.user.repository.UserRepository;
import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.user.service.UserService;
import com.leverx.workload.user.service.converter.EntityModelConverter;
import com.leverx.workload.user.service.util.ViolationParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository repository;
  private final EntityModelConverter mapper;
  private final Validator validator;

  @Override
  public List<User> findAllUsers(String firstName, String email, int page, int size, String[] sort) {
    List<User> users = new ArrayList<>();

    if (email != null) {
      User user = findByEmail(email);
      users.add(user);
    } else {
      List<Order> orders = new ArrayList<>();
      if (sort[0].contains(",")) {
        for (String sortOrder : sort) {
          String[] sorts = sortOrder.split(",");
          orders.add(new Order(getSortDirection(sorts[1]), sorts[0]));
        }
      } else {
        orders.add(new Order(getSortDirection(sort[1]), sort[0]));
      }
      Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
      if (firstName == null) {
        users = findUsers(pageable);
      } else {
        users = findByFirstNameIgnoreCaseContaining(firstName, pageable);
      }
    }
    return users;
  }

  @Override
  public User findById(long id) {
    return mapper.toModelFromEntity(repository.findById(id).orElseThrow(
        () -> new UserNotExistException(String.format("User with id=%s not found", id))));
  }

  @Override
  @Transactional
  public User createUser(User user) {
    if (repository.findByEmail(user.getEmail()).isPresent()) {
      throw new DuplicatedEmailException(
          String.format("Email = %s already exists, email must be unique", user.getEmail()));
    }
    UserEntity entity = mapper.toEntity(user);
    checkEntityViolations(entity);
    return mapper.toModelFromEntity(repository.save(entity));
  }

  @Override
  @Transactional
  public User updateUser(User user) {
    long id = user.getId();
    if (repository.findById(id).isEmpty()) {
      throw new UserNotExistException("Unable to update user. User doesn't exist.");
    }
    String email = user.getEmail();
    if (repository.findByEmail(email).isPresent()) {
      long anotherId = repository.findByEmail(email).get().getId();
      if (id != anotherId) {
        throw new DuplicatedEmailException(
            String.format("Email = %s already exists, email must be unique", user.getEmail()));
      }
    }
    UserEntity entity = mapper.toEntity(user);
    checkEntityViolations(entity);
    return mapper.toModelFromEntity(repository.save(entity));
  }

  @Override
  @Transactional
  public void deleteUserById(long id) {
    if (repository.findById(id).isEmpty()) {
      throw new UserNotExistException("Unable to delete user. User doesn't exist.");
    }
    repository.deleteById(id);
  }

  private User findByEmail(String email) {
    return mapper.toModelFromEntity(repository.findByEmail(email).orElseThrow(
        () -> new UserNotExistException(String.format("User with email = %s not found", email))));
  }

  private List<User> findUsers(Pageable pageable) {
    Page<UserEntity> entities = repository.findAll(pageable);
    return entities.getContent().stream().map(mapper::toModelFromEntity).toList();
  }

  private List<User> findByFirstNameIgnoreCaseContaining(String firstName, Pageable pageable) {
    Page<UserEntity> entities = repository.findByFirstNameIgnoreCaseContaining(firstName, pageable);
    return entities.getContent().stream().map(mapper::toModelFromEntity).toList();
  }

  private Sort.Direction getSortDirection(String direction) {
    if (direction.equals("asc")) {
      return Sort.Direction.ASC;
    } else if (direction.equals("desc")) {
      return Sort.Direction.DESC;
    }
    return Sort.Direction.ASC;
  }

  private void checkEntityViolations(UserEntity entity) {
    Set<ConstraintViolation<UserEntity>> violations = validator.validate(entity);
    if (!violations.isEmpty()) {
      throw new NotValidUserException(
          "User has not valid field(s). " + ViolationParser.parse(violations));
    }
  }
}
