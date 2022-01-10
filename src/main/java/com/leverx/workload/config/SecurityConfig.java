package com.leverx.workload.config;

import com.leverx.workload.security.jwt.JwtConfigurer;
import com.leverx.workload.security.service.model.Role;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtConfigurer jwtConfigurer;

  private static final String[] PERMIT_ALL = new String[] {"/auth/**", "/v2/api-docs"};
  private static final String[] COMMON_GET_PERMISSIONS = new String[] {"/users", "/departments",
      "/projects", "/projects/*/users/**", "/users/*/projects/**", "/report/**"};
  private static final String[] USER_GET_PERMISSIONS =
      new String[] {"/users/**", "/departments/**", "/projects/**"};
  private static final String[] COMMON_PUT_PERMISSIONS = new String[] {"/projects/users"};

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.httpBasic().disable().csrf().disable().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
        .antMatchers(PERMIT_ALL).permitAll().antMatchers(HttpMethod.GET, COMMON_GET_PERMISSIONS)
        .hasAnyRole(Role.USER.name(), Role.ADMIN.name())
        .antMatchers(HttpMethod.GET, USER_GET_PERMISSIONS).hasRole(Role.USER.name())
        .antMatchers(HttpMethod.POST).hasRole(Role.ADMIN.name())
        .antMatchers(HttpMethod.PUT, COMMON_PUT_PERMISSIONS)
        .hasAnyRole(Role.USER.name(), Role.ADMIN.name()).antMatchers(HttpMethod.PUT)
        .hasRole(Role.ADMIN.name()).antMatchers(HttpMethod.DELETE).hasRole(Role.ADMIN.name())
        .anyRequest().authenticated().and().apply(jwtConfigurer);
  }

  @Bean
  protected PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
