package com.leverx.workload.user.repository.specification;

import com.leverx.workload.user.repository.entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;

public class Specifications {
  private Specifications() {
    throw new UnsupportedOperationException("Class instance can't be instantiated.");
  }

  public static Specification<UserEntity> hasEmail(String email) {
    return (userEntity, criteriaQuery, criteriaBuilder) -> {
      if (email != null) {
        return criteriaBuilder.equal(userEntity.get("email"), email);
      } else {
        return criteriaBuilder.and();
      }
    };
  }

  public static Specification<UserEntity> hasFirstName(String firstName) {
    return (userEntity, criteriaQuery, criteriaBuilder) -> {
      if (firstName != null) {
        return criteriaBuilder.like(criteriaBuilder.lower(userEntity.get("firstName")),
            "%" + firstName.toLowerCase() + "%");
      } else {
        return criteriaBuilder.and();
      }
    };
  }
}
