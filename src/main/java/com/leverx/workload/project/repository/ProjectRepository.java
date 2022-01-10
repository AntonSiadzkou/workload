package com.leverx.workload.project.repository;

import com.leverx.workload.project.repository.entity.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProjectRepository
    extends JpaRepository<ProjectEntity, Long>, JpaSpecificationExecutor<ProjectEntity> {

  @Override
  Page<ProjectEntity> findAll(Specification<ProjectEntity> spec, Pageable pageable);
}
