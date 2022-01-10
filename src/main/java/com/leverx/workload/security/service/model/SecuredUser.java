package com.leverx.workload.security.service.model;

import com.leverx.workload.user.repository.entity.UserEntity;
import java.io.Serial;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class SecuredUser implements UserDetails {

  @Serial
  private static final long serialVersionUID = -4041866547951331356L;

  private final String username;
  private final String password;
  private final List<SimpleGrantedAuthority> authorities;
  private final boolean isActive;

  public SecuredUser(String username, String password, List<SimpleGrantedAuthority> authorities,
      boolean isActive) {
    this.username = username;
    this.password = password;
    this.authorities = authorities;
    this.isActive = isActive;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return isActive;
  }

  @Override
  public boolean isAccountNonLocked() {
    return isActive;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return isActive;
  }

  @Override
  public boolean isEnabled() {
    return isActive;
  }

  public static UserDetails fromUserEntity(UserEntity user) {
    return new User(user.getEmail(), user.getPassword(), user.getActive(), user.getActive(),
        user.getActive(), user.getActive(), user.getRole().getAuthorities());
  }
}
