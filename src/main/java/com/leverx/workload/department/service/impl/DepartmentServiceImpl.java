package com.leverx.workload.department.service.impl;

import com.leverx.workload.department.repository.DepartmentRepository;
import com.leverx.workload.department.repository.entity.DepartmentEntity;
import com.leverx.workload.department.service.DepartmentService;
import com.leverx.workload.department.service.converter.DepartmentConverter;
import com.leverx.workload.department.web.dto.request.DepartmentBodyParams;
import com.leverx.workload.department.web.dto.request.DepartmentRequestParams;
import com.leverx.workload.exception.DuplicatedValueException;
import com.leverx.workload.user.repository.entity.UserEntity;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Validated
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

  private DepartmentRepository repository;
  private DepartmentConverter mapper;

  @Override
  @Transactional(readOnly = true)
  public List<DepartmentEntity> findAllDepartments(@NotNull DepartmentRequestParams params) {
    Pageable pageable = PageRequest.of(params.page(), params.size());
    log.info("Searching all departments");
    List<DepartmentEntity> departments = repository.findAll(pageable).getContent();
    log.info("Successfully found all departments");
    return departments;
  }

  @Override
  @Transactional(readOnly = true)
  public DepartmentEntity findById(@NotNull Long id) {
    log.info("Searching a department by id " + id);
    DepartmentEntity department = repository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(String.format("Department with id=%s not found", id)));
    log.info("Successfully found a department with id " + id);
    return department;
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserEntity> findAllUsersInDepartment(@NotNull Long id) {
    log.info("Searching all users in department with id " + id);
    DepartmentEntity department = repository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(String.format("Department with id=%s not found", id)));
    List<UserEntity> users = department.getUsers().stream().toList(); // todo how get lazy correctly
    log.info("Successfully found all users in department with id " + id);
    return users;
  }

  @Override
  @Transactional
  public long createDepartment(@NotNull DepartmentBodyParams department) {
    log.info("Creating a new department");
    if (repository.findByTitle(department.getTitle()).isPresent()) {
      throw new DuplicatedValueException(
          String.format("Department with title = %s already exists", department.getTitle()));
    }
    long id = repository.save(mapper.toEntity(department)).getId();
    log.info("Successfully created a new department with id " + id);
    return id;
  }

  @Override
  @Transactional
  public void updateDepartment(@NotNull DepartmentBodyParams department) {
    long id = department.getId();
    log.info("Updating a department with id " + id);
    DepartmentEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(
        "Unable to update department. Department doesn't exist."));
    long anotherId = repository.findByTitle(department.getTitle()).orElse(entity).getId();
    if (entity.getId() != anotherId) {
      throw new DuplicatedValueException(
          String.format("Department title = %s already exists", department.getTitle()));
    }
    repository.save(mapper.toEntity(department));
    log.info("Successfully updated a department with id " + id);
  }

  @Override
  @Transactional
  public void deleteDepartmentById(@NotNull Long id) {
    log.info("Deleting a department with id " + id);
    repository.findById(id).orElseThrow(() -> new EntityNotFoundException(
        "Unable to delete the department. Department doesn't exist."));
    repository.deleteById(id);
    log.info("Successfully deleted a department with id " + id);
  }
}
