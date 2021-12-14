package com.leverx.workload.repository;

import com.leverx.workload.entity.User;
import java.util.List;

public interface UserRepository {
  List<User> findAll();
}
