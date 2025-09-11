package com.website_backend.account;

import com.website_backend.account.dto.SignupCustomerDetails;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final JdbcUserDetailsManager jdbcUserDetailsManager;
  private final PasswordEncoder encoder;

  public CustomerService(CustomerRepository customerRepository,
      JdbcUserDetailsManager jdbcUserDetailsManager, PasswordEncoder encoder) {
    this.customerRepository = customerRepository;
    this.jdbcUserDetailsManager = jdbcUserDetailsManager;
    this.encoder = encoder;
  }

  public void saveCustomer(SignupCustomerDetails customerDetails){
    try {
      UserDetails userDetails = User.builder()
          .username(customerDetails.username())
          .password(encoder.encode(customerDetails.password()))
          .roles("CUSTOMER")
          .build();
      jdbcUserDetailsManager.createUser(userDetails);
      customerRepository.saveCustomerProfile(customerDetails);
    } catch (DuplicateKeyException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An account with that username already exists");
    }
    catch (Exception e){
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to save new user");
    }
  }

}
