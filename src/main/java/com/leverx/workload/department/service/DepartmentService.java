package com.leverx.workload.department.service;

import com.leverx.workload.department.repository.entity.DepartmentEntity;
import com.leverx.workload.department.web.dto.request.DepartmentBodyParams;
import com.leverx.workload.department.web.dto.request.DepartmentRequestParams;
import com.leverx.workload.user.repository.entity.UserEntity;
import java.util.List;
import javax.validation.constraints.NotNull;

public interface DepartmentService {

  List<DepartmentEntity> findAllDepartments(@NotNull DepartmentRequestParams params);

  DepartmentEntity findById(@NotNull Long id);

  List<UserEntity> findAllUsersInDepartment(@NotNull Long id);

  long createDepartment(@NotNull DepartmentBodyParams department);

  void updateDepartment(@NotNull DepartmentBodyParams department);

  void deleteDepartmentById(@NotNull Long id);
}
