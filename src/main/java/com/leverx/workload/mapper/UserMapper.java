package com.leverx.workload.mapper;

import com.leverx.workload.controller.request.UserRequest;
import com.leverx.workload.controller.response.UserResponse;
import com.leverx.workload.entity.UserEntity;
import com.leverx.workload.model.User;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {

  private ModelMapper mapper;

  public UserEntity toEntity(User model) {
    return Objects.isNull(model) ? null : mapper.map(model, UserEntity.class);
  }

  public User toModelFromEntity(UserEntity entity) {
    return Objects.isNull(entity) ? null : mapper.map(entity, User.class);
  }

  public UserResponse toResponse(User model) {
    return Objects.isNull(model) ? null : mapper.map(model, UserResponse.class);
  }

  public User toModelFromRequest(UserRequest request) {
    return Objects.isNull(request) ? null : mapper.map(request, User.class);
  }
}
