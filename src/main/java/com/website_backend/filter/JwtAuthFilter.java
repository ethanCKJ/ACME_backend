package com.website_backend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.web.filter.OncePerRequestFilter;


public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtDecoder jwtDecoder;
  private final UserDetailsService userDetailsService;

  public JwtAuthFilter(JwtDecoder jwtDecoder, UserDetailsService userDetailsService) {
    this.jwtDecoder = jwtDecoder;
    this.userDetailsService = userDetailsService;
  }

  /**
   * Same contract as for {@code doFilter}, but guaranteed to be just invoked once per request
   * within a single request thread. See {@link #shouldNotFilterAsyncDispatch()} for details.
   * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
   * default ServletRequest and ServletResponse ones.
   *
   * @param request
   * @param response
   * @param filterChain
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String header = request.getHeader("Authorization");
    if (header != null && header.length() > 8){
      String token = header.substring(7);
      try{
        // Throws error if token is malformed, has invalid signature or is expired
        Jwt jwt = jwtDecoder.decode(token);
        String username = jwt.getSubject();
        // Throws error if user does not exist
        userDetailsService.loadUserByUsername(username);
        // If no exception thrown at this stage, jwt token is fully valid for some user in the system

        // Sets Granted Authorities and Authenticated=true
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("roles");
        authoritiesConverter.setAuthorityPrefix("");
        Collection<GrantedAuthority> grantedAuthorities = authoritiesConverter.convert(jwt);
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(jwt, grantedAuthorities);

        SecurityContextHolder.getContext().setAuthentication(authToken);
      } catch (JwtException | UsernameNotFoundException ignored) {
      }
    }
    filterChain.doFilter(request, response);
  }
}
