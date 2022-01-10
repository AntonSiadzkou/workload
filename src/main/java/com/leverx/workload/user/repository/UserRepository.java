package com.leverx.workload.user.repository;

import com.leverx.workload.security.service.model.Role;
import com.leverx.workload.user.repository.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository
    extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

  @Override
  Page<UserEntity> findAll(Specification<UserEntity> spec, Pageable pageable);

  Optional<UserEntity> findById(Long id);

  Optional<UserEntity> findByEmail(String email);

  List<UserEntity> findAllByActiveAndRole(boolean isActive, Role role);
}
