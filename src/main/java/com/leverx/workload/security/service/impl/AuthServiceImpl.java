package com.leverx.workload.security.service.impl;

import com.leverx.workload.exception.JwtAuthenticationException;
import com.leverx.workload.security.jwt.JwtTokenProvider;
import com.leverx.workload.security.service.AuthService;
import com.leverx.workload.security.web.dto.AuthRequest;
import com.leverx.workload.security.web.dto.AuthResponse;
import com.leverx.workload.user.repository.UserRepository;
import com.leverx.workload.user.repository.entity.UserEntity;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Validated
@Slf4j
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthenticationManager manager;

  @Override
  @Transactional(readOnly = true)
  public AuthResponse authenticate(@NotNull AuthRequest request) {
    try {
      log.info("Authenticate user");
      manager.authenticate(
          new UsernamePasswordAuthenticationToken(request.email(), request.password()));
      log.info("Successfully authenticated, getting user entity");
      String email = request.email();
      UserEntity user = userRepository.findByEmail(email).orElseThrow(
          () -> new EntityNotFoundException("User with email " + email + " doesn't exist"));
      log.info("Creating jwt token");
      String token = jwtTokenProvider.createToken(email, user.getRole().name());
      log.info("Successfully created jwt token");
      return new AuthResponse(email, token);
    } catch (AuthenticationException e) {
      throw new JwtAuthenticationException(e.getMessage());
    }
  }

  @Override
  @Transactional(readOnly = true)
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
    securityContextLogoutHandler.logout(request, response, null);
    log.info("Log out successfully");
  }
}
