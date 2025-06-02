package com.website_backend.account;

import com.website_backend.account.dto.SignupStaffDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

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

  public String createStaff(SignupStaffDetails staffInfo){
    UserDetails userDetails = User
        .builder()
        .username(staffInfo.username())
        .password(encoder.encode(staffInfo.password()))
        .roles("STAFF")
        .build();
    try {
      jdbcUserDetailsManager.createUser(userDetails);
      staffAccountRepository.saveStaff(staffInfo);
    } catch (Exception e) {
      if (e.getLocalizedMessage().contains("Duplicate entry")){
        return "An account with that email already exists";
      }
      return "Server error";
    }
    return "";
  }

}
