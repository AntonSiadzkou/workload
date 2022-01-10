package com.leverx.workload.security.jwt;

import com.leverx.workload.exception.JwtAuthenticationException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
@AllArgsConstructor
@Slf4j
public class JwtTokenFilter extends GenericFilterBean {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    log.debug("Start jwt filter");
    String tokenWithBearer = jwtTokenProvider.resolveToken((HttpServletRequest) request);

    // Extracting only jwt token by deleting 'Bearer ' from the beginning of the token from request
    String token = (tokenWithBearer != null) ? tokenWithBearer.substring(7) : null;
    log.debug("Extracted token: " + token);
    try {
      if (token != null && jwtTokenProvider.validateToken(token)) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        if (authentication != null) {
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    } catch (Exception e) {
      SecurityContextHolder.clearContext();
      log.warn(e.getMessage()); // todo fix this exception handling
      throw new JwtAuthenticationException(e.getMessage());
    }
    filterChain.doFilter(request, response);
  }
}
