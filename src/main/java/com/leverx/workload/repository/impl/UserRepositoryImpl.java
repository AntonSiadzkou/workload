package com.leverx.workload.repository.impl;

import com.leverx.workload.entity.User;
import com.leverx.workload.repository.UserRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {
  private final SessionFactory sessionFactory;

  @Autowired
  public UserRepositoryImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public List<User> findAll() {
    Session session = sessionFactory.getCurrentSession();
    Query<User> query = session.createQuery("from User", User.class);
    List<User> users = query.getResultList();
    return users;
  }
}
