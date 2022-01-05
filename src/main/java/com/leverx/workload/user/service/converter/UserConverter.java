package com.leverx.workload.user.service.converter;

import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.user.web.dto.request.UserBodyParams;
import com.leverx.workload.user.web.dto.response.UserResponse;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserConverter {

  private final ModelMapper mapper;

  public UserEntity toEntity(UserBodyParams params) {
    return Objects.isNull(params) ? null : mapper.map(params, UserEntity.class);
  }

  public UserResponse toResponse(UserEntity entity) {
    return Objects.isNull(entity) ? null : mapper.map(entity, UserResponse.class);
  }

}
