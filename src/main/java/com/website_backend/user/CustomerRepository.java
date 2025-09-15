package com.website_backend.user;

import com.website_backend.user.dto.CustomerProfile;
import com.website_backend.user.dto.SignupCustomerDetails;
import com.website_backend.errors.DatabaseException;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Handles loading and saving the profile information (name, email, address etc) of a customer
 */
@Repository
public class CustomerRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public CustomerRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  /**
   * Loads full profile information of a customer. Note profile excludes email
   * @param id database primary key not email.
   * @return
   */
  public CustomerProfile loadCustomerByUsername(int id) throws DatabaseException {
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
      throw new DatabaseException(e.getMessage());
    }
  }

  /**
   * Save full profile of a customer on signup.
   * @param customerDetails
   * @throws DatabaseException
   */
  public void saveCustomerProfile(SignupCustomerDetails customerDetails) throws DatabaseException {
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
      throw new DatabaseException(e.getMessage());
    }
  }
}
