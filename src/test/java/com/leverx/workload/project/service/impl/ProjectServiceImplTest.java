package com.leverx.workload.project.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.leverx.workload.project.repository.ProjectRepository;
import com.leverx.workload.project.repository.entity.ProjectEntity;
import com.leverx.workload.project.service.ProjectService;
import com.leverx.workload.project.service.converter.ProjectConverter;
import com.leverx.workload.project.web.dto.request.ProjectRequestParams;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
}
