package com.leverx.workload.service.impl;

import com.leverx.workload.entity.Department;
import com.leverx.workload.repository.DepartmentRepository;
import com.leverx.workload.service.DepartmentService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

  private final DepartmentRepository repository;

  @Override
  public List<Department> findAllDepartments() {
    return repository.findAllDepartments();
  }
}
