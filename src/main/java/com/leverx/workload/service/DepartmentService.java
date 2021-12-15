package com.leverx.workload.service;

import com.leverx.workload.entity.Department;
import java.util.List;

public interface DepartmentService {

  List<Department> findAllDepartments();
}
