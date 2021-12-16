package com.leverx.workload.controller;

import com.leverx.workload.entity.Department;
import com.leverx.workload.service.DepartmentService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/departments")
@AllArgsConstructor
public class TestController {

  private final DepartmentService departmentService;

  @GetMapping
  public List<Department> findAllDepartments() {
    return departmentService.findAllDepartments();
  }
}
