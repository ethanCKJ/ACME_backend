package com.website_backend.controller;

import com.website_backend.errors.DatabaseException;
import com.website_backend.token.TokenService;
import com.website_backend.user.CustomerService;
import com.website_backend.user.StaffService;
import com.website_backend.user.dto.LoginRequest;
import com.website_backend.user.dto.SignupCustomerDetails;
import com.website_backend.user.dto.SignupResponse;
import com.website_backend.user.dto.SignupStaffDetails;
import com.website_backend.user.errors.UsernameExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  private final StaffService staffService;
  private final CustomerService customerService;
  private final AuthenticationManager authManager;
  private final TokenService tokenService;

  public AuthController(StaffService staffService, AuthenticationManager authManager,
      TokenService tokenService, CustomerService customerService) {
    this.staffService = staffService;
    this.authManager = authManager;
    this.tokenService = tokenService;
    this.customerService = customerService;
  }

  /**
   * Save staff username (email), password and profile information e.g. name
   * @param newStaffInfo
   * @return
   */
  @PostMapping("/signup/staff")
  public ResponseEntity<SignupResponse> signupStaff (@RequestBody SignupStaffDetails newStaffInfo){
    staffService.createStaff(newStaffInfo);
    return new ResponseEntity<>(new SignupResponse("success", ""), HttpStatus.OK);
  }

  /**
   * Save admin username (email), password and profile information.
   * @param newAdminInfo
   * @return
   */
  @PostMapping("/signup/admin")
  public ResponseEntity<SignupResponse> signupAdmin (@RequestBody SignupStaffDetails newAdminInfo){
    staffService.createAdmin(newAdminInfo);
    return new ResponseEntity<>(new SignupResponse("success",""), HttpStatus.OK);
  }

  /**
   * Save customer username (email), password and profile information e.g. address
   * @param newCustomerInfo
   * @return
   */
  @PostMapping("/signup/customer")
  public ResponseEntity<SignupResponse> signupCustomer (@RequestBody SignupCustomerDetails newCustomerInfo){
    try{
      customerService.saveCustomer(newCustomerInfo);
    } catch (DatabaseException e) {
      return new ResponseEntity<>(new SignupResponse("Database error",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (UsernameExistsException e){
      return new ResponseEntity<>(new SignupResponse("Username exists already", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(new SignupResponse("success",""), HttpStatus.OK);
  }

  /**
   * Get JWT access token which also specifies role.
   * @param loginRequest
   * @return
   */
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

}
