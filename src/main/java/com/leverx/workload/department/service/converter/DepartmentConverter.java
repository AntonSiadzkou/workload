package com.leverx.workload.department.service.converter;

import com.leverx.workload.department.repository.entity.DepartmentEntity;
import com.leverx.workload.department.web.dto.responce.DepartmentResponse;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DepartmentConverter {

  private final ModelMapper mapper;

  public DepartmentEntity toEntity(DepartmentResponse response) {
    return Objects.isNull(response) ? null : mapper.map(response, DepartmentEntity.class);
  }

  public DepartmentResponse toResponse(DepartmentEntity entity) {
    return Objects.isNull(entity) ? null : mapper.map(entity, DepartmentResponse.class);
  }
}
