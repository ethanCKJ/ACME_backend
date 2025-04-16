package com.website_backend;

import com.website_backend.Data.Order;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class HandleOrder {
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final PriceRowMapper priceRowMapper = new PriceRowMapper();

  // Using a bean from a non-bean function
  @Autowired
  public HandleOrder(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public boolean handleOrder(Order order){
    // Write to customer_order table
    MapSqlParameterSource customerOrderParams = new MapSqlParameterSource();
    customerOrderParams.addValue("customer_name", order.customerName());
    customerOrderParams.addValue("email", order.email());
    customerOrderParams.addValue("address", order.address());
    customerOrderParams.addValue("city", order.city());
    customerOrderParams.addValue("phone", order.phone());
    customerOrderParams.addValue("shipping", order.shipping());
    customerOrderParams.addValue("is_fulfilled", false);
    customerOrderParams.addValue("customer_id", order.customerId());
    int rows;
    try {

    rows = namedParameterJdbcTemplate.update("INSERT INTO customer_order(customer_name, email, address, city, phone, shipping, is_fulfilled, customer_id) VALUES (:customer_name, :email, :address, :city, :phone, :shipping, :is_fulfilled, :customer_id)", customerOrderParams);
    } catch (Exception e) {
      return false;
    }
    // Get product prices from db to populate sale_price
    MapSqlParameterSource getPriceParams = new MapSqlParameterSource();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("(");
    for (int i = 0; i < order.products().length; i++) {
      stringBuilder.append(order.products()[i].productId());
      stringBuilder.append(",");
    }
    stringBuilder.append(")");
    getPriceParams.addValue("id_list", stringBuilder);
    List<HashMap> prices = namedParameterJdbcTemplate.query("SELECT id, price FROM product WHERE id IN :id_list", getPriceParams, priceRowMapper);
    // Write to order_product table


    return rows != 0;
  }

}
