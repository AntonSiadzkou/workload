package com.leverx.workload.repository.impl;

import com.leverx.workload.entity.Department;
import com.leverx.workload.repository.DepartmentRepository;
import java.util.List;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
@AllArgsConstructor
public class DepartmentRepositoryImpl implements DepartmentRepository {
  private final SessionFactory sessionFactory;

  @Override
  public List<Department> findAllDepartments() {
    Session session = sessionFactory.getCurrentSession();
    Query<Department> query = session.createQuery("from Department", Department.class);
    List<Department> departments = query.getResultList();
    return departments;
  }
}
