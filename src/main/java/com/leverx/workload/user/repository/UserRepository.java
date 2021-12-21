package com.leverx.workload.user.repository;

import com.leverx.workload.user.repository.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByEmail(String email);

  Page<UserEntity> findByFirstNameIgnoreCaseContaining(String firstName, Pageable pageable);

  Optional<UserEntity> findById(long id);
}
