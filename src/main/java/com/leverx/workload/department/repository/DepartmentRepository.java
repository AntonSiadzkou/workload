package com.leverx.workload.department.repository;

import com.leverx.workload.department.repository.entity.DepartmentEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

  Optional<DepartmentEntity> findByTitle(String title);
}
