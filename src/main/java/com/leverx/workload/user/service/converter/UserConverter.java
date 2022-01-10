package com.leverx.workload.user.service.converter;

import com.leverx.workload.security.service.model.Role;
import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.user.web.dto.request.UserBodyParams;
import com.leverx.workload.user.web.dto.response.UserResponse;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class UserConverter {

  private final ModelMapper mapper;
  private final PasswordEncoder encoder;

  public UserEntity toEntity(UserBodyParams params) {
    log.info("Converting user: from body params to entity");
    mapper.typeMap(UserBodyParams.class, UserEntity.class)
        .addMappings(mapper -> mapper.skip(UserEntity::setPassword))
        .addMappings(mapper -> mapper.skip(UserEntity::setRole))
        .setPostConverter(securedDataConverter());
    return Objects.isNull(params) ? null : mapper.map(params, UserEntity.class);
  }

  public UserResponse toResponse(UserEntity entity) {
    log.info("Converting user: from entity to response");
    return Objects.isNull(entity) ? null : mapper.map(entity, UserResponse.class);
  }

  public Converter<UserBodyParams, UserEntity> securedDataConverter() {
    return context -> {
      UserBodyParams source = context.getSource();
      UserEntity destination = context.getDestination();
      destination.setPassword(encoder.encode(source.getPassword()));
      destination.setRole(Role.valueOf(source.getRole().trim().toUpperCase()));
      return context.getDestination();
    };
  }
}
