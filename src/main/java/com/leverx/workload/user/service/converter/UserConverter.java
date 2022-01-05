package com.leverx.workload.user.service.converter;

import com.leverx.workload.csv.service.model.CsvUser;
import com.leverx.workload.department.repository.entity.DepartmentEntity;
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

  public UserEntity fromCsvToEntity(CsvUser csvUser) {
    log.info("Converting user: from csv-user to entity");
    mapper.typeMap(CsvUser.class, UserEntity.class)
        .addMappings(mapper -> mapper.skip(UserEntity::setPassword))
        .setPostConverter(csvConverter());
    return Objects.isNull(csvUser) ? null : mapper.map(csvUser, UserEntity.class);
  }

  public Converter<CsvUser, UserEntity> csvConverter() {
    return context -> {
      CsvUser source = context.getSource();
      UserEntity destination = context.getDestination();
      destination.setPassword(encoder.encode(source.getPassword()));
      DepartmentEntity department = new DepartmentEntity();
      department.setId(source.getDepartment());
      destination.setDepartment(department);
      return context.getDestination();
    };
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
