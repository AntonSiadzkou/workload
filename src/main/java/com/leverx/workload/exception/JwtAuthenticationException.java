package com.leverx.workload.exception;

import java.io.Serial;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

  @Serial
  private static final long serialVersionUID = 4183364779525846210L;

  public JwtAuthenticationException(String msg) {
    super(msg);
  }
}
