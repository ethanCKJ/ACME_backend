package com.website_backend.user;

import com.website_backend.user.dto.SignupStaffDetails;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StaffService {

  private final PasswordEncoder encoder;
  private final JdbcUserDetailsManager jdbcUserDetailsManager;
  private final StaffRepository staffAccountRepository;

  public StaffService(PasswordEncoder encoder, JdbcUserDetailsManager jdbcUserDetailsManager,
      StaffRepository staffAccountRepository) {
    this.encoder = encoder;
    this.jdbcUserDetailsManager = jdbcUserDetailsManager;
    this.staffAccountRepository = staffAccountRepository;
  }

  /**
   * Creates user with STAFF role
   * @param staffInfo
   */
  public void createStaff(SignupStaffDetails staffInfo) {
    UserDetails userDetails = User
        .builder()
        .username(staffInfo.username())
        .password(encoder.encode(staffInfo.password()))
        .roles("STAFF")
        .build();
    try {
      jdbcUserDetailsManager.createUser(userDetails);
      staffAccountRepository.saveStaffProfile(staffInfo);
    } catch (DuplicateKeyException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "An account with that username already exists");
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          "Unable to save new user");
    }
  }

  /**
   * Creates user with ADMIN role
   * @param adminInfo
   */
  public void createAdmin(SignupStaffDetails adminInfo) {
    UserDetails userDetails = User
        .builder()
        .username(adminInfo.username())
        .password(encoder.encode(adminInfo.password()))
        .roles("ADMIN")
        .build();
    try {
      jdbcUserDetailsManager.createUser(userDetails);
      staffAccountRepository.saveStaffProfile(adminInfo);
    } catch (DuplicateKeyException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "An account with that username already exists");
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          "Unable to save new user");
    }
  }
}
