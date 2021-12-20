package com.leverx.workload.repository;

import com.leverx.workload.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  UserEntity findByEmail(String email);

  Page<UserEntity> findByFirstNameIgnoreCaseContaining(String firstName, Pageable pageable);
}
