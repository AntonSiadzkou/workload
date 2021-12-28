package com.leverx.workload.department.service.impl;

import com.leverx.workload.department.exception.DepartmentNotExistException;
import com.leverx.workload.department.exception.DuplicatedTitleException;
import com.leverx.workload.department.repository.DepartmentRepository;
import com.leverx.workload.department.repository.entity.DepartmentEntity;
import com.leverx.workload.department.service.DepartmentService;
import com.leverx.workload.department.service.converter.DepartmentConverter;
import com.leverx.workload.department.web.dto.request.DepartmentBodyParams;
import com.leverx.workload.department.web.dto.request.DepartmentRequestParams;
import com.leverx.workload.user.repository.entity.UserEntity;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Validated
public class DepartmentServiceImpl implements DepartmentService {

  private DepartmentRepository repository;
  private DepartmentConverter mapper;

  @Override
  @Transactional(readOnly = true)
  public List<DepartmentEntity> findAllDepartments(@NotNull DepartmentRequestParams params) {
    Pageable pageable = PageRequest.of(params.page(), params.size());

    return repository.findAll(pageable).getContent();
  }

  @Override
  @Transactional(readOnly = true)
  public DepartmentEntity findById(@NotNull Long id) {
    return repository.findById(id).orElseThrow(() -> new DepartmentNotExistException(
        String.format("Department with id=%s not found", id)));
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserEntity> findAllUsersInDepartment(@NotNull Long id) {
    DepartmentEntity department =
        repository.findById(id).orElseThrow(() -> new DepartmentNotExistException(
            String.format("Department with id=%s not found", id)));
    return department.getUsers().stream().toList(); // todo fix how to get lazy correctly: may we
                                                    // return Stream?
  }

  @Override
  @Transactional
  public long createDepartment(@NotNull DepartmentBodyParams department) {
    if (repository.findByTitle(department.getTitle()).isPresent()) {
      throw new DuplicatedTitleException(
          String.format("Department with title = %s already exists", department.getTitle()));
    }
    return repository.save(mapper.toEntity(department)).getId();
  }

  @Override
  @Transactional
  public void updateDepartment(@NotNull DepartmentBodyParams department) {
    DepartmentEntity entity =
        repository.findById(department.getId()).orElseThrow(() -> new DepartmentNotExistException(
            "Unable to update department. Department doesn't exist."));
    long anotherId = repository.findByTitle(department.getTitle()).orElse(entity).getId();
    if (entity.getId() != anotherId) {
      throw new DuplicatedTitleException(
          String.format("Department title = %s already exists", department.getTitle()));
    }
    repository.save(mapper.toEntity(department));
  }

  @Override
  @Transactional
  public void deleteDepartmentById(@NotNull Long id) {
    repository.findById(id).orElseThrow(() -> new DepartmentNotExistException(
        "Unable to delete the department. Department doesn't exist."));
    repository.deleteById(id);
  }
}
