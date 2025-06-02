package com.website_backend.controller;

import com.website_backend.account.dto.LoginRequest;
import com.website_backend.account.dto.SignupStaffDetails;
import com.website_backend.account.dto.SignupResponse;
import com.website_backend.account.StaffService;
import com.website_backend.token.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(originPatterns = "http://localhost:*", maxAge = 3600)
public class AuthController {

  private final StaffService staffService;
  private final AuthenticationManager authManager;
  private final TokenService tokenService;

  public AuthController(StaffService staffService, AuthenticationManager authManager,
      TokenService tokenService, JwtDecoder jwtDecoder) {
    this.staffService = staffService;
    this.authManager = authManager;
    this.tokenService = tokenService;
  }

  @PostMapping("/signup/staff")
  public ResponseEntity<SignupResponse> signup (@RequestBody SignupStaffDetails newStaffInfo){
    String errorMsg = staffService.createStaff(newStaffInfo);
    if (errorMsg.isEmpty()){
      return new ResponseEntity<>(new SignupResponse("success", ""), HttpStatus.OK);
    }
    else{
      return new ResponseEntity<>(new SignupResponse("error",errorMsg), HttpStatus.BAD_REQUEST);
    }
  }

//  @PostMapping("/signup/customer")
//  public ResponseEntity<SignupResponse> signup (@RequestBody CustomerProfile customerProfile){
//    String errorMsg = staffService.createStaff(newStaffInfo);
//    if (errorMsg.isEmpty()){
//      return new ResponseEntity<>(new SignupResponse("success", ""), HttpStatus.OK);
//    }
//    else{
//      return new ResponseEntity<>(new SignupResponse("error",errorMsg), HttpStatus.BAD_REQUEST);
//    }
//  }

  // TODO: Handle adding new customers.


  // username s@gmail.com password: password
  @PostMapping("/token")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
    try {
      Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
      String token = tokenService.generateToken(auth);
      System.out.println(token);
      return new ResponseEntity<>(token, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("error: invalid credentials",HttpStatus.FORBIDDEN);
    }
  }

  /**
   * Returns 401 Unauthorized if user sends an invalid token. debugJwt never runs
   * @param auth
   * @return
   */
  @GetMapping("/debug_jwt")
  public ResponseEntity<?> debugJwt(Authentication auth){
    System.out.println("Debug JWT");
//    String token = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoic0BnbWFpbC5jb20iLCJpZCI6MSwiZXhwIjoxNzQ4ODA1NDE2LCJpYXQiOjE3NDg3ODc0MTYsInNjb3BlIjoiUk9MRV9TVEFGRiJ9.v4lmveZDhSAumF53S51xr1_u9fSda4ptUU8l4ch7fSovnGVIPYIcTdQG6yICn2T2JnVXoYyQHbcCe1Q4GnLkk_GX9-t-ms_LfQZVpks5Mh_AngoV7kgcR9FkXaYKIk0IyL4lFiXiGCfcq8jcEhfP3RVCPQQLhFZYpE_3fZQC6aQlehlwgnfkBc2UMuUZxKp_P9h6O3ec948UaKa2lkqz7bIVIPgaUGwcCHSeRoX9VrYEwwdBmgp4X9eBWlRAWSfQeLuD654saIycOAPoEqwDgR7gHQuTd8sQC045DYG3Fg5Sj-AiDIX-8Sk8lTnmwiaqqUn9FXmHTWv6lFxyWw9m8A";
//    Jwt jwt1 = jwtDecoder.decode(token);
//    System.out.println(jwt1);
    Long id = -1L;
    if (auth instanceof JwtAuthenticationToken){
      Jwt jwt = ((JwtAuthenticationToken) auth).getToken();
      id = jwt.getClaim("id");
      return new ResponseEntity<>("Hello " + auth.getName() + "id " + id, HttpStatus.OK);
    }
    else{
      // If an empty token is sent no problems.
      return new ResponseEntity<>("You are not logged in", HttpStatus.BAD_REQUEST);
    }
  }
}
