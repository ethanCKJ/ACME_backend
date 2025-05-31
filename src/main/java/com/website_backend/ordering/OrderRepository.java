package com.website_backend.ordering;

import com.website_backend.Data.enums.ErrorCode;
import com.website_backend.Data.Order;
import com.website_backend.Data.OrderDetail;
import java.sql.Types;
import java.time.format.DateTimeFormatter;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public OrderRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  /**
   * Save order and set orderId
   * @param order
   * @return
   */
  public ErrorCode saveOrder(Order order){
    int paymentAmount = 0;
    for (OrderDetail orderDetail : order.getOrderDetails()){
      paymentAmount += orderDetail.getPrice() * orderDetail.getQuantity();
    }
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("customer_name",order.getCustomerName());
    params.addValue("email",order.getEmail());
    params.addValue("address_line1",order.getAddressLine1());
    params.addValue("address_line2",order.getAddressLine2());
    params.addValue("address_line3",order.getAddressLine3());
    params.addValue("city",order.getCity());
    params.addValue("phone",order.getPhone());
    params.addValue("shipping",order.getShipping(), Types.VARCHAR);
    params.addValue("customer_id",order.getCustomerId());
    params.addValue("payment_amount",paymentAmount);
    String date = formatter.format(order.getRequiredDatetime());
    params.addValue("requiredDate",date);

    KeyHolder keyHolder = new GeneratedKeyHolder();
    try {
      namedParameterJdbcTemplate.update(
          "INSERT INTO acme_db.orders(customer_name, email, address_line1, address_line2, address_line3,"
          + "city, phone, shipping, customer_id, payment_amount, requiredDate) "
          + "VALUES (:customer_name, :email, :address_line1, :address_line2, :address_line3,"
          + ":city, :phone, :shipping, :customer_id, :payment_amount, :requiredDate);",
          params,
          keyHolder,
          new String[] {"id"}
          );
    } catch (DataAccessException e) {
      System.out.println(e.getLocalizedMessage());
      return ErrorCode.DATABASE_ERROR;
    }

    Number newId = keyHolder.getKey();
    if (newId != null){
      order.setOrderId(newId.intValue());
    }
    return ErrorCode.NO_ERROR;
  }
}
