package com.website_backend.token;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  private final JwtEncoder jwtEncoder;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public TokenService(JwtEncoder jwtEncoder, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.jwtEncoder = jwtEncoder;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  /**
   * Generates fully signed JWT token. Throws an Exception if any error occurs.
   * @param authentication
   * @return
   */
  public String generateToken(Authentication authentication) throws Exception {
    Instant now = Instant.now();
    String roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
        .collect(
            Collectors.joining(" "));
    int expireMinutes = 30;
    if (roles.contains("ADMIN") || roles.contains("STAFF")){
      expireMinutes = 300;
    }
    int id = getUserId(authentication.getName());
    // Payload
    JwtClaimsSet claims = JwtClaimsSet
        .builder()
        .issuer("self")
        .issuedAt(now)
        .expiresAt(now.plus(expireMinutes, ChronoUnit.MINUTES))
        .subject(authentication.getName())
        .claim("roles", roles)
        .claim("id", id)
        .build();
    // id is an integer at this stage
    return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }

  private int getUserId(String username) throws Exception{
    Number id = namedParameterJdbcTemplate.queryForObject("SELECT (id) FROM acme_db.users WHERE username=:username", Map.of("username", username), Number.class);
    return id.intValue();
  }
}
