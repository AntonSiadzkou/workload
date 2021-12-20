package com.leverx.workload.controller;

import com.leverx.workload.controller.response.UserResponse;
import com.leverx.workload.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@PropertySource("classpath:app.properties")
@Api(tags = "User operations")
public class UserController {
  private final UserService service;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(
      value =
          "Get list of all users with pagination and sorting and filtering by first name and email")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 500, message = "Internal server error")
      })
  public List<UserResponse> getAllUsers(
      @ApiParam(
              name = "firstName",
              value = "First name of users by which the selection is filtered")
          @RequestParam(name = "firstName", required = false)
          String firstName,
      @ApiParam(name = "email", value = "Email of user by which the selection is filtered")
          @RequestParam(required = false)
          String email,
      @ApiParam(name = "page", defaultValue = "0", value = "Number of page, start from 0")
          @RequestParam(defaultValue = "${page.num.default}")
          int page,
      @ApiParam(name = "size", defaultValue = "3", value = "Maximum number of items per page")
          @RequestParam(defaultValue = "${page.size.default}")
          int size,
      @ApiParam(
              name = "sort",
              defaultValue = "id,asc",
              value = "Array of pairs (column and direction) to sort the selection")
          @RequestParam(defaultValue = "${page.sort.default}")
          String[] sort) {
    List<UserResponse> users = new ArrayList<>();

    if (email != null) {
      UserResponse user = service.findByEmail(email);
      users.add(user);
    } else {
      List<Order> orders = new ArrayList<>();
      if (sort[0].contains(",")) {
        for (String sortOrder : sort) {
          String[] sorts = sortOrder.split(",");
          orders.add(new Order(getSortDirection(sorts[1]), sorts[0]));
        }
      } else {
        orders.add(new Order(getSortDirection(sort[1]), sort[0]));
      }
      Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
      if (firstName == null) {
        users = service.findAllUsers(pageable);
      } else {
        users = service.findByFirstNameIgnoreCaseContaining(firstName, pageable);
      }
    }
    return users;
  }

  private Sort.Direction getSortDirection(String direction) {
    if (direction.equals("asc")) {
      return Sort.Direction.ASC;
    } else if (direction.equals("desc")) {
      return Sort.Direction.DESC;
    }
    return Sort.Direction.ASC;
  }
}