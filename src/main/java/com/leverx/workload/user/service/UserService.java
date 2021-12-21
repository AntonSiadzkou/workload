package com.leverx.workload.user.service;

import com.leverx.workload.user.model.User;
import java.util.List;

public interface UserService {

  List<User> findAllUsers(String firstName, String email, int page, int size, String[] sort);

  User findById(long id);

  User createUser(User user);

  User updateUser(User user);

  void deleteUserById(long id);
}
