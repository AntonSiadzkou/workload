package com.leverx.workload.user.service;

import com.leverx.workload.user.repository.entity.UserEntity;
import com.leverx.workload.user.web.dto.request.UserBodyParams;
import com.leverx.workload.user.web.dto.request.UserRequestParams;
import java.util.List;
import javax.validation.constraints.NotNull;

public interface UserService {

  List<UserEntity> findAllUsers(@NotNull UserRequestParams params);

  UserEntity findById(@NotNull Long id);

  long createUser(@NotNull UserBodyParams user);

  void updateUser(@NotNull UserBodyParams user);

  void deleteUserById(@NotNull Long id);
}
