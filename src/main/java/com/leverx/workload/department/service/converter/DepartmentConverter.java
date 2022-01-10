package com.leverx.workload.department.service.converter;

import com.leverx.workload.department.repository.entity.DepartmentEntity;
import com.leverx.workload.department.web.dto.request.DepartmentBodyParams;
import com.leverx.workload.department.web.dto.responce.DepartmentResponse;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class DepartmentConverter {

  private final ModelMapper mapper;

  public DepartmentEntity toEntity(DepartmentBodyParams request) {
    log.info("Converting department: from body params to entity");
    return Objects.isNull(request) ? null : mapper.map(request, DepartmentEntity.class);
  }

  public DepartmentResponse toResponse(DepartmentEntity entity) {
    log.info("Converting department: from entity to response");
    return Objects.isNull(entity) ? null : mapper.map(entity, DepartmentResponse.class);
  }
}
