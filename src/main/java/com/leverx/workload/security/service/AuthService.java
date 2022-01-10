package com.leverx.workload.security.service;

import com.leverx.workload.security.web.dto.AuthRequest;
import com.leverx.workload.security.web.dto.AuthResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

public interface AuthService {

  AuthResponse authenticate(@NotNull AuthRequest request);

  void logout(HttpServletRequest request, HttpServletResponse response);
}
