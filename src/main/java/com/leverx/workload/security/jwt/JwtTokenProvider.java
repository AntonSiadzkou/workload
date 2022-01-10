package com.leverx.workload.security.jwt;

import com.leverx.workload.exception.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:app.properties")
@Slf4j
public class JwtTokenProvider {

  private final UserDetailsService userDetailsService;

  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${jwt.header}")
  private String authorizationHeader;

  @Value("${jwt.duration}")
  private long validityInMinutes;

  public JwtTokenProvider(
      @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String createToken(String username, String role) {
    log.debug("Start creating jwt token");
    Claims claims = Jwts.claims().setSubject(username);
    claims.put("role", role);
    Date now = new Date();
    Date validity = Date.from(LocalDateTime.now().plusMinutes(validityInMinutes)
        .atZone(ZoneId.systemDefault()).toInstant());

    String token = Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    log.debug("Jwt token created: " + token);
    return token;
  }

  public boolean validateToken(String token) {
    log.debug("Start validating jwt token");
    try {
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      boolean isValid = !claimsJws.getBody().getExpiration().before(new Date());
      log.debug("Finish validating jwt token, is Valid: " + isValid);
      return isValid;
    } catch (ExpiredJwtException e) {
      throw new JwtAuthenticationException("JWT token is expired");
    } catch (UnsupportedJwtException e) {
      throw new JwtAuthenticationException("Unsupported JWT token");
    } catch (MalformedJwtException e) {
      throw new JwtAuthenticationException("Malformed JWT token");
    } catch (SignatureException e) {
      throw new JwtAuthenticationException("Invalid signature of JWT token");
    } catch (Exception e) {
      throw new JwtAuthenticationException("Invalid JWT token");
    }
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUsername(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
  }

  public String resolveToken(HttpServletRequest request) {
    return request.getHeader(authorizationHeader);
  }
}
