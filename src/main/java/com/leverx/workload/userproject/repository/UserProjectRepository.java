package com.leverx.workload.userproject.repository;

import com.leverx.workload.project.repository.entity.ProjectEntity;
import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.userproject.repository.entity.UserProjectEntity;
import com.leverx.workload.userproject.repository.entity.UserProjectId;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserProjectRepository extends JpaRepository<UserProjectEntity, UserProjectId> {

  List<UserProjectEntity> findAllByIdUserOrderByAssignDate(UserEntity user);

  List<UserProjectEntity> findAllByIdUserAndCancelDateGreaterThanOrderByAssignDate(UserEntity user,
      LocalDate date);

  List<UserProjectEntity> findAllByIdProjectOrderByAssignDate(ProjectEntity project);

  List<UserProjectEntity> findAllByIdProjectAndCancelDateGreaterThanOrderByAssignDate(
      ProjectEntity project, LocalDate date);

  @Query("SELECT up from UserProjectEntity up WHERE up.assignDate BETWEEN :fromDate AND :tillDate "
      + "OR up.cancelDate BETWEEN :fromDate AND :tillDate "
      + "OR (up.assignDate < :fromDate AND up.cancelDate > :tillDate)")
  List<UserProjectEntity> findAllActiveProjectWithinPeriod(@Param("fromDate") LocalDate fromDate,
      @Param("tillDate") LocalDate tillDate);

  List<UserProjectEntity> findAllByCancelDateBetween(LocalDate from, LocalDate till);
}
