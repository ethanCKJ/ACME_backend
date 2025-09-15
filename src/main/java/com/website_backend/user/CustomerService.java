package com.website_backend.user;

import com.website_backend.errors.DatabaseException;
import com.website_backend.user.dto.SignupCustomerDetails;
import com.website_backend.user.errors.UsernameExistsException;
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

  /**
   * 1. Saves customer username (email) and password
   * 2. Saves customer profile (name, address etc)
   * @param customerDetails
   */
  public void saveCustomer(SignupCustomerDetails customerDetails)
      throws DatabaseException, UsernameExistsException {
    try {
      UserDetails userDetails = User.builder()
          .username(customerDetails.username())
          .password(encoder.encode(customerDetails.password()))
          .roles("CUSTOMER")
          .build();
      jdbcUserDetailsManager.createUser(userDetails);
      customerRepository.saveCustomerProfile(customerDetails);
    } catch (DuplicateKeyException e) {
      throw new UsernameExistsException("An account with that username already exists");
    }
    catch (Exception e){
      throw new DatabaseException(e.getMessage());
    }
  }

}
