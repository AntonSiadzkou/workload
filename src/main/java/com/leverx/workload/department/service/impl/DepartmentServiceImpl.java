package com.leverx.workload.department.service.impl;

import com.leverx.workload.department.exception.DepartmentNotExistException;
import com.leverx.workload.department.repository.DepartmentRepository;
import com.leverx.workload.department.repository.entity.DepartmentEntity;
import com.leverx.workload.department.service.DepartmentService;
import com.leverx.workload.department.service.converter.DepartmentConverter;
import com.leverx.workload.department.web.dto.request.DepartmentRequestParams;
import java.util.List;
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
  public List<DepartmentEntity> findAllDepartments(DepartmentRequestParams params) {
    Pageable pageable = PageRequest.of(params.page(), params.size());

    return repository.findAll(pageable).getContent();
  }

  @Override
  public DepartmentEntity findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new DepartmentNotExistException(
        String.format("Department with id=%s not found", id)));
  }
}
