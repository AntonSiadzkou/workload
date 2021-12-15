package com.leverx.workload.repository;

import com.leverx.workload.entity.Department;
import java.util.List;

public interface DepartmentRepository {
  List<Department> findAllDepartments();
}
