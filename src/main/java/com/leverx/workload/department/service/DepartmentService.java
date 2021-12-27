package com.leverx.workload.department.service;

import com.leverx.workload.department.repository.entity.DepartmentEntity;
import com.leverx.workload.department.web.dto.request.DepartmentRequestParams;
import java.util.List;
import javax.validation.constraints.NotNull;

public interface DepartmentService {

  List<DepartmentEntity> findAllDepartments(@NotNull DepartmentRequestParams params);

  DepartmentEntity findById(@NotNull Long id);

}
