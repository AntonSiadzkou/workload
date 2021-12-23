package com.leverx.workload.user.service;

import com.leverx.workload.user.model.User;
import com.leverx.workload.user.web.dto.request.UserRequestParams;
import java.util.List;

public interface UserService {

  List<User> findAllUsers(UserRequestParams params);

  User findById(long id);

  long createUser(User user);

  void updateUser(User user);

  void deleteUserById(long id);
}
