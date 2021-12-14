package com.leverx.workload.controller;

import com.leverx.workload.entity.User;
import com.leverx.workload.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/users")
  public Iterable<User> findAllUsers() {
    return userService.findAllUsers();
  }
}
