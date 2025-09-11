package com.website_backend.account;

import com.website_backend.account.dto.CustomerProfile;
import com.website_backend.account.dto.SignupCustomerDetails;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public CustomerRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
      JdbcUserDetailsManager jdbcUserDetailsManager) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public CustomerProfile loadCustomerByUsername(int id) {
    List<CustomerProfile> result;
    try{
      result =  namedParameterJdbcTemplate.query("SELECT customer_name, address_line1, address_line2, address_line3, phone, postcode, city"
          + " FROM acme_db.customer "
          + "WHERE id=:id AND is_deleted=FALSE",
          Map.of("id",id),
          (rs, rowNum) -> (
            new CustomerProfile(
                rs.getString(2),
                rs.getString(1),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getString(6),
                rs.getString(7)
            )
          ));
      return result.get(0);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void saveCustomerProfile(SignupCustomerDetails customerDetails){
    try {
      int customerId = namedParameterJdbcTemplate.queryForObject("SELECT id FROM acme_db.users WHERE username=:username", Map.of("username", customerDetails.username()), Integer.class);
      namedParameterJdbcTemplate.update("INSERT INTO acme_db.customer(id, customer_name, phone, address_line1, address_line2, address_line3, city, postcode)"
              + " VALUES (:id, :customerName, :phone, :addressLine1, :addressLine2, :addressLine3, :city, :postcode)",
          Map.of("id", customerId,
              "customerName", customerDetails.customerName(),
              "phone", customerDetails.phone(),
              "addressLine1", customerDetails.addressLine1(),
              "addressLine2", customerDetails.addressLine2(),
              "addressLine3", customerDetails.addressLine3(),
              "city", customerDetails.city(),
              "postcode", customerDetails.postcode()
              ));
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
