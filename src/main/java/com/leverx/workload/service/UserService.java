package com.leverx.workload.service;

import com.leverx.workload.controller.response.UserResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface UserService {

  UserResponse findByEmail(String email);

  List<UserResponse> findAllUsers(Pageable pageable);

  List<UserResponse> findByFirstNameIgnoreCaseContaining(String firstName, Pageable pageable);

  UserResponse findById(long id);
}
