package com.leverx.workload.userproject.repository;

import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.userproject.repository.entity.UserProjectEntity;
import com.leverx.workload.userproject.repository.entity.UserProjectId;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectRepository extends JpaRepository<UserProjectEntity, UserProjectId> {

  List<UserProjectEntity> findAllByIdUserOrderByAssignDate(UserEntity user);

  List<UserProjectEntity> findAllByIdUserAndCancelDateGreaterThanOrderByAssignDate(UserEntity user,
      LocalDate date);
}
