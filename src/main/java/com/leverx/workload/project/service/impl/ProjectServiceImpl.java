package com.leverx.workload.project.service.impl;

import com.leverx.workload.exception.NotValidEntityException;
import com.leverx.workload.project.repository.ProjectRepository;
import com.leverx.workload.project.repository.entity.ProjectEntity;
import com.leverx.workload.project.repository.specification.ProjectSpecifications;
import com.leverx.workload.project.service.ProjectService;
import com.leverx.workload.project.service.converter.ProjectConverter;
import com.leverx.workload.project.web.dto.request.ProjectBodyParams;
import com.leverx.workload.project.web.dto.request.ProjectRequestParams;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ProjectServiceImpl implements ProjectService {

  private final ProjectRepository repository;
  private final ProjectConverter mapper;

  @Override
  @Transactional(readOnly = true)
  public List<ProjectEntity> findAllProjects(@NotNull ProjectRequestParams params) {
    Pageable pageable = PageRequest.of(params.page(), params.size(),
        Sort.by(new Order(getSortDirection(params.sortDirection()), params.sortColumn())));
    Specification<ProjectEntity> spec =
        Specification.where(ProjectSpecifications.greaterThanStartDate(params.startDate()))
            .and(ProjectSpecifications.lessThanEndDate(params.endDate()));
    log.info("Searching all projects with pagination and specifications");
    Page<ProjectEntity> pageData = repository.findAll(spec, pageable);
    log.info("Successfully found all projects");
    return pageData.getContent();
  }

  @Override
  @Transactional(readOnly = true)
  public ProjectEntity findById(@NotNull Long id) {
    log.info("Searching a project with id:" + id);
    ProjectEntity project = repository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(String.format("Project with id=%s not found", id)));
    log.info("Successfully found a project with id:" + id);
    return project;
  }

  @Override
  @Transactional
  public long createProject(@NotNull ProjectBodyParams project) {
    log.info("Creating a new project");
    ProjectEntity entity = mapper.toEntity(project);
    checkDates(entity);
    long id = repository.save(entity).getId();
    log.info("Successfully created a project with id:" + id);
    return id;
  }

  @Override
  @Transactional
  public void updateProject(@NotNull ProjectBodyParams project) {
    log.info("Updating a project with id: " + project.getId());
    repository.findById(project.getId()).orElseThrow(
        () -> new EntityNotFoundException("Unable to update project. Project doesn't exist."));
    ProjectEntity entity = mapper.toEntity(project);
    checkDates(entity);
    repository.save(mapper.toEntity(project));
    log.info("Successfully updated a project with id:" + entity.getId());
  }

  @Override
  @Transactional
  public void deleteProjectById(@NotNull Long id) {
    log.info("Deleting a project with id: " + id);
    repository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Unable to delete project. Project doesn't exist."));
    repository.deleteById(id);
    log.info("Successfully deleted a project with id:" + id);
  }

  private Sort.Direction getSortDirection(String direction) {
    return (direction.equals("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;
  }

  private void checkDates(ProjectEntity entity) {
    if (entity.getEndDate() != null && entity.getEndDate().isBefore(entity.getStartDate())) {
      throw new NotValidEntityException("The project end date must be later than the start date.");
    }
  }
}
