package com.leverx.workload.user.service.util;

import com.leverx.workload.user.repository.entity.UserEntity;
import java.util.Set;
import javax.validation.ConstraintViolation;

public class ViolationParser {

  private ViolationParser() {
    throw new UnsupportedOperationException("Forbidden to create an instance.");
  }

  public static String parse(Set<ConstraintViolation<UserEntity>> violations) {
    StringBuilder builder = new StringBuilder();
    for (ConstraintViolation<UserEntity> violation : violations) {
      builder.append("['").append(violation.getInvalidValue()).append("': '")
          .append(violation.getMessage()).append("']; ");
    }
    return builder.toString();
  }
}
