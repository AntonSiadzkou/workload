package com.leverx.workload.security.web;

import com.leverx.workload.security.service.AuthService;
import com.leverx.workload.security.web.dto.AuthRequest;
import com.leverx.workload.security.web.dto.AuthResponse;
import com.leverx.workload.util.GeneralUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Api(tags = "Authentication operations")
@Slf4j
public class AuthenticationController {

  private AuthService service;

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Authenticate user and create jwt token for this user")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public AuthResponse authenticate(@ApiParam(name = "authRequest",
      value = "Email and password for user authentication and creating jwt token")
  @RequestBody @Valid AuthRequest authRequest, BindingResult bindingResult) {
    log.info("Start authentication and creating token");
    GeneralUtils.checkViolations(bindingResult);
    return service.authenticate(authRequest);
  }

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Log out (delete security authentication from security context)")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok"),
      @ApiResponse(code = 500, message = "Internal server error")})
  public void logout(
      @ApiParam(name = "request", value = "Http Servlet Request") HttpServletRequest request,
      @ApiParam(name = "response", value = "Http Servlet Response") HttpServletResponse response) {
    log.info("Start logout");
    service.logout(request, response);
  }
}
