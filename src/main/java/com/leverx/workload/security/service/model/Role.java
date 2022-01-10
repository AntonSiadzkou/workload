package com.leverx.workload.security.service.model;

import java.util.Set;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
  USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

  private final String authority;

  Role(String authority) {
    this.authority = authority;
  }

  public String getAuthority() {
    return authority;
  }

  public Set<SimpleGrantedAuthority> getAuthorities() {
    return Set.of(new SimpleGrantedAuthority(authority));
  }
}
