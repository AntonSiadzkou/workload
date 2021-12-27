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
import com.leverx.workload.user.exception.DuplicatedEmailException;
import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.user.web.dto.request.UserBodyParams;
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
  void userWithDuplicatedEmail_CreateUser_Exception() {
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

}
