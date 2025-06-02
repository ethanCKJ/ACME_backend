package com.website_backend.account;

import com.website_backend.account.dto.CustomerProfile;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public CustomerRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public CustomerProfile loadCustomerByUsername(int id) {
    List<CustomerProfile> result =  namedParameterJdbcTemplate.query("SELECT (customer_name, address_line1, address_line2, address_line3, phone, postcode, city)"
        + " FROM acme_db.customer "
        + "WHERE id=:id AND is_deleted=FALSE",
        Map.of("id",id),
        (rs, rowNum) -> (
          new CustomerProfile(
              rs.getString(1),
              rs.getString(2),
              rs.getString(3),
              rs.getString(4),
              rs.getString(5),
              rs.getString(6),
              rs.getString(7)
          )
        ));
    return result.get(0);
  }
}
