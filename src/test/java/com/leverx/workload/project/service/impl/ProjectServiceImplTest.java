package com.leverx.workload.project.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.leverx.workload.exception.NotValidEntityException;
import com.leverx.workload.project.repository.ProjectRepository;
import com.leverx.workload.project.repository.entity.ProjectEntity;
import com.leverx.workload.project.service.ProjectService;
import com.leverx.workload.project.service.converter.ProjectConverter;
import com.leverx.workload.project.web.dto.request.ProjectBodyParams;
import com.leverx.workload.project.web.dto.request.ProjectRequestParams;
import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.user.web.dto.request.UserBodyParams;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith({MockitoExtension.class})
class ProjectServiceImplTest {

  private ProjectService underTest;
  @Mock
  private ProjectRepository repository;
  @Mock
  private ProjectConverter mapper;

  @BeforeEach
  void setUp() {
    mapper = mock(ProjectConverter.class);
    repository = mock(ProjectRepository.class);
    underTest = new ProjectServiceImpl(repository, mapper);
  }

  @Test
  void endDate_FindAllProjects_ListOfProjects() {
    String endDate = "2022-11-01";
    ProjectEntity entity = new ProjectEntity();
    entity.setEndDate(LocalDate.parse(endDate));
    List<ProjectEntity> expected = new ArrayList<>();
    expected.add(entity);
    ProjectRequestParams params = new ProjectRequestParams(null, endDate, 1, 1, "startDate", "asc");
    Page<ProjectEntity> page = mock(Page.class);

    given(repository.findAll(any(Specification.class), any(Pageable.class))).willReturn(page);
    given(page.getContent()).willReturn(expected);

    List<ProjectEntity> actual = underTest.findAllProjects(params);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void id_findById_Project() {
    long id = 2;
    ProjectEntity expected = new ProjectEntity();
    expected.setId(id);

    given(repository.findById(id)).willReturn(Optional.of(expected));

    ProjectEntity actual = underTest.findById(id);

    verify(repository).findById(id);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void validProject_CreateProject_Created() {
    long expected = 3;
    ProjectBodyParams request = new ProjectBodyParams();
    request.setId(expected);
    ProjectEntity entity = new ProjectEntity();
    entity.setId(expected);

    given(mapper.toEntity(request)).willReturn(entity);
    given(repository.save(entity)).willReturn(entity);

    long actual = underTest.createProject(request);

    verify(repository).save(entity);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void validProject_UpdateProject_Updated() {
    long id = 3;
    ProjectBodyParams request = new ProjectBodyParams();
    request.setId(id);
    ProjectEntity entity = new ProjectEntity();
    entity.setId(id);

    given(repository.findById(id)).willReturn(Optional.of(entity));
    given(mapper.toEntity(request)).willReturn(entity);

    underTest.updateProject(request);

    verify(repository).findById(id);
    verify(repository).save(entity);
  }

  @Test
  void notExistedProject_UpdateProject_Exception() {
    Exception exception = assertThrows(EntityNotFoundException.class, () -> {
      ProjectBodyParams params = new ProjectBodyParams();
      params.setId(999L);

      underTest.updateProject(params);
    });

    String expectedMessage = "Unable to update project. Project doesn't exist.";
    String actualMessage = exception.getMessage();

    assertThat(actualMessage).contains(expectedMessage);
  }

  @Test
  void wrongDates_UpdateProject_Exception() {
    Exception exception = assertThrows(NotValidEntityException.class, () -> {
      long id = 3;
      String startDate = "2022-10-10";
      String endDate = "2020-10-10";
      ProjectBodyParams params = new ProjectBodyParams();
      params.setId(id);
      params.setStartDate(startDate);
      params.setEndDate(endDate);
      ProjectEntity entity = new ProjectEntity();
      entity.setId(id);
      entity.setStartDate(LocalDate.parse(startDate));
      entity.setEndDate(LocalDate.parse(endDate));

      given(repository.findById(id)).willReturn(Optional.of(entity));
      given(mapper.toEntity(params)).willReturn(entity);

      underTest.updateProject(params);
    });

    String expectedMessage = "The project end date must be later than the start date.";
    String actualMessage = exception.getMessage();

    assertThat(actualMessage).contains(expectedMessage);
  }
}
