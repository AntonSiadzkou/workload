package com.leverx.workload.department.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.leverx.workload.department.exception.DepartmentNotExistException;
import com.leverx.workload.department.exception.DuplicatedTitleException;
import com.leverx.workload.department.repository.DepartmentRepository;
import com.leverx.workload.department.repository.entity.DepartmentEntity;
import com.leverx.workload.department.service.converter.DepartmentConverter;
import com.leverx.workload.department.service.impl.DepartmentServiceImpl;
import com.leverx.workload.department.web.dto.request.DepartmentBodyParams;
import com.leverx.workload.department.web.dto.request.DepartmentRequestParams;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ExtendWith({MockitoExtension.class})
class DepartmentServiceTest {

  private DepartmentService underTest;
  @Mock
  private DepartmentRepository repository;
  @Mock
  private DepartmentConverter mapper;

  @BeforeEach
  void setUp() {
    mapper = mock(DepartmentConverter.class);
    repository = mock(DepartmentRepository.class);
    underTest = new DepartmentServiceImpl(repository, mapper);
  }

  @Test
  void findAllDepartments_EverythingOk_ListOfUser() {
    DepartmentEntity entity = new DepartmentEntity();
    List<DepartmentEntity> expected = new ArrayList<>();
    expected.add(entity);
    DepartmentRequestParams params = new DepartmentRequestParams(0, 3);
    Page<DepartmentEntity> page = mock(Page.class);

    given(repository.findAll(any(Pageable.class))).willReturn(page);
    given(page.getContent()).willReturn(expected);

    List<DepartmentEntity> actual = underTest.findAllDepartments(params);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void id_findById_Department() {
    long id = 3;
    DepartmentEntity expected = new DepartmentEntity();
    expected.setId(id);

    given(repository.findById(id)).willReturn(Optional.of(expected));

    DepartmentEntity actual = underTest.findById(id);

    verify(repository).findById(id);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void id_findById_Exception() {
    long id = 999;
    Exception exception = assertThrows(DepartmentNotExistException.class, () -> {

      given(repository.findById(id)).willReturn(Optional.empty());

      underTest.findById(id);
    });

    String expectedMessage = "Department with id=" + id + " not found";
    String actualMessage = exception.getMessage();

    assertThat(actualMessage).contains(expectedMessage);
  }

  @Test
  void validDepartment_CreateDepartment_Created() {
    long expected = 3;
    DepartmentBodyParams request = new DepartmentBodyParams();
    request.setId(expected);
    DepartmentEntity entity = new DepartmentEntity();
    entity.setId(expected);

    given(repository.findByTitle(any())).willReturn(Optional.empty());
    given(mapper.toEntity(request)).willReturn(entity);
    given(repository.save(entity)).willReturn(entity);

    long actual = underTest.createDepartment(request);

    verify(repository).findByTitle(any());
    verify(repository).save(entity);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void duplicatedTitle_CreateDepartment_Exception() {
    String title = "TEST";
    Exception exception = assertThrows(DuplicatedTitleException.class, () -> {
      DepartmentBodyParams params = new DepartmentBodyParams();
      params.setTitle(title);

      given(repository.findByTitle(title)).willReturn(Optional.of(new DepartmentEntity()));

      underTest.createDepartment(params);
    });

    String expectedMessage = "Department with title = " + title + " already exists";
    String actualMessage = exception.getMessage();

    assertThat(actualMessage).contains(expectedMessage);
  }

  @Test
  void validDepartment_UpdateDepartment_Updated() {
    long id = 3;
    DepartmentBodyParams request = new DepartmentBodyParams();
    request.setId(id);
    DepartmentEntity entity = new DepartmentEntity();
    entity.setId(id);

    given(repository.findById(id)).willReturn(Optional.of(entity));
    given(repository.findByTitle(any())).willReturn(Optional.empty());
    given(mapper.toEntity(request)).willReturn(entity);

    underTest.updateDepartment(request);

    verify(repository).findById(id);
    verify(repository).findByTitle(any());
    verify(repository).save(entity);
  }

  @Test
  void notExistedDepartment_UpdateDepartment_Exception() {
    Exception exception = assertThrows(DepartmentNotExistException.class, () -> {
      DepartmentBodyParams department = new DepartmentBodyParams();
      department.setId(999L);

      underTest.updateDepartment(department);
    });

    String expectedMessage = "Unable to update department. Department doesn't exist.";
    String actualMessage = exception.getMessage();

    assertThat(actualMessage).contains(expectedMessage);
  }

  @Test
  void duplicatedTitle_UpdateDepartment_Exception() {
    String title = "IT";
    Exception exception = assertThrows(DuplicatedTitleException.class, () -> {
      long id = 2;
      long id2 = 4;
      DepartmentEntity entity = new DepartmentEntity();
      entity.setId(id);
      entity.setTitle(title);
      DepartmentEntity entity2 = new DepartmentEntity();
      entity2.setId(id2);
      entity2.setTitle(title);
      DepartmentBodyParams params = new DepartmentBodyParams();
      params.setId(2L);
      params.setTitle(title);

      given(repository.findById(id)).willReturn(Optional.of(entity));
      given(repository.findByTitle(title)).willReturn(Optional.of(entity2));

      underTest.updateDepartment(params);
    });

    String expectedMessage = "Department title = " + title + " already exists";
    String actualMessage = exception.getMessage();

    assertThat(actualMessage).contains(expectedMessage);
  }
}
