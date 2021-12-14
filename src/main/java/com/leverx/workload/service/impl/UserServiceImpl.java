package com.leverx.workload.service.impl;

import com.leverx.workload.entity.User;
import com.leverx.workload.repository.UserRepository;
import com.leverx.workload.service.UserService;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Iterable<User> findAllUsers() {
    return userRepository.findAll();
  }
}
