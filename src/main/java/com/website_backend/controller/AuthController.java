package com.website_backend.controller;

import com.website_backend.account.CustomerService;
import com.website_backend.account.dto.LoginRequest;
import com.website_backend.account.dto.SignupCustomerDetails;
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
  private final CustomerService customerService;
  private final AuthenticationManager authManager;
  private final TokenService tokenService;

  public AuthController(StaffService staffService, AuthenticationManager authManager,
      TokenService tokenService, JwtDecoder jwtDecoder, CustomerService customerService) {
    this.staffService = staffService;
    this.authManager = authManager;
    this.tokenService = tokenService;
    this.customerService = customerService;
  }

  @PostMapping("/signup/staff")
  public ResponseEntity<SignupResponse> signupStaff (@RequestBody SignupStaffDetails newStaffInfo){
    staffService.createStaff(newStaffInfo);
    return new ResponseEntity<>(new SignupResponse("success", ""), HttpStatus.OK);
  }

  @PostMapping("/signup/admin")
  public ResponseEntity<SignupResponse> signupAdmin (@RequestBody SignupStaffDetails newAdminInfo){
    staffService.createAdmin(newAdminInfo);
    return new ResponseEntity<>(new SignupResponse("success",""), HttpStatus.OK);
  }

  @PostMapping("/signup/customer")
  public ResponseEntity<SignupResponse> signupCustomer (@RequestBody SignupCustomerDetails newCustomerInfo){
    customerService.saveCustomer(newCustomerInfo);
    return new ResponseEntity<>(new SignupResponse("success", ""), HttpStatus.OK);
  }

  // username s@gmail.com password: password
  @PostMapping("/token")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
    try {
      Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
      String token = tokenService.generateToken(auth);
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
    Long id = -1L;
    if (auth instanceof JwtAuthenticationToken){
      Jwt jwt = ((JwtAuthenticationToken) auth).getToken();
      id = jwt.getClaim("id");
      return new ResponseEntity<>("Hello " + auth.getName() + " id " + id, HttpStatus.OK);
    }
    else{
      // If an empty token is sent no problems.
      return new ResponseEntity<>("You are not logged in", HttpStatus.BAD_REQUEST);
    }
  }
}
