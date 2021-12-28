package com.leverx.workload.project.service.impl;

import com.leverx.workload.project.repository.ProjectRepository;
import com.leverx.workload.project.repository.entity.ProjectEntity;
import com.leverx.workload.project.repository.specification.ProjectSpecifications;
import com.leverx.workload.project.service.ProjectService;
import com.leverx.workload.project.service.converter.ProjectConverter;
import com.leverx.workload.project.web.dto.request.ProjectRequestParams;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Validated
public class ProjectServiceImpl implements ProjectService {

  private ProjectRepository repository;
  private ProjectConverter mapper;

  @Override
  @Transactional(readOnly = true)
  public List<ProjectEntity> findAllProjects(@NotNull ProjectRequestParams params) {
    Pageable pageable = PageRequest.of(params.page(), params.size(),
        Sort.by(new Order(getSortDirection(params.sortDirection()), params.sortColumn())));

    Specification<ProjectEntity> spec =
        Specification.where(ProjectSpecifications.greaterThanStartDate(params.startDate()))
            .and(ProjectSpecifications.lessThanStartDate(params.endDate()));

    Page<ProjectEntity> pageData = repository.findAll(spec, pageable);
    return pageData.getContent();
  }

  @Override
  @Transactional(readOnly = true)
  public ProjectEntity findById(@NotNull Long id) {
    return repository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(String.format("Project with id=%s not found", id)));
  }

  private Sort.Direction getSortDirection(String direction) {
    return (direction.equals("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;
  }
}
