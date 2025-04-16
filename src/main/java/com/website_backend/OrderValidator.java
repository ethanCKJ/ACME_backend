package com.website_backend;

import com.website_backend.Data.Order;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class OrderValidator {

  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  OrderRowMapper rowMapper = new OrderRowMapper();

  public boolean isOrderValid(Order order) {
    // Check if product ids requested exist in database and stock for each item is enough for the order
    for (int i = 0; i < order.products().length; i++) {
      MapSqlParameterSource paramSource = new MapSqlParameterSource();
      paramSource.addValue("id", order.products()[i].productId());
      List<Integer> stock = namedParameterJdbcTemplate.query("SELECT stock FROM product WHERE id=:id", paramSource, rowMapper);
      if (stock.isEmpty() || stock.get(0) < order.products()[i].quantity()){
        return false;
      }
    }
    // TODO: Is it possible to optimize this query. Try PreparedStatement way
    return true;

  }
}
