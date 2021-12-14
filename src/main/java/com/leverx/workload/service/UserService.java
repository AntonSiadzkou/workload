package com.leverx.workload.service;

import com.leverx.workload.entity.User;

public interface UserService {

  Iterable<User> findAllUsers();
}
