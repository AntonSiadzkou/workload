package com.leverx.workload.user.service.converter;

import com.leverx.workload.user.model.User;
import com.leverx.workload.user.repository.entity.UserEntity;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EntityModelConverter {

  private final ModelMapper mapper;

  public UserEntity toEntity(User model) {
    return Objects.isNull(model) ? null : mapper.map(model, UserEntity.class);
  }

  public User toModelFromEntity(UserEntity entity) {
    return Objects.isNull(entity) ? null : mapper.map(entity, User.class);
  }
}
