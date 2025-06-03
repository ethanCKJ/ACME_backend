package com.website_backend.orders;

import com.website_backend.orders.dto.StaffOrder;
import com.website_backend.orders.dto.StaffOrderDetail;
import com.website_backend.orders.enums.ErrorCode;
import com.website_backend.orders.dto.Order;
import com.website_backend.orders.dto.OrderDetail;
import com.website_backend.orders.enums.OrderState;
import com.website_backend.orders.enums.ProductCategory;
import java.sql.Date;
import java.sql.Types;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
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
  private final DateTimeFormatter staffFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // YYYY-MM-DD → Year-Month-Day ISO 8601

  public OrderRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public List<StaffOrder> loadStaffOrders(OrderState orderState) {
    // Get order details
//    List<StaffOrderDetail> staffOrderDetailList = namedParameterJdbcTemplate.query("SELECT product_id, quantity FROM acme_db.order_details WHERE order_id IN (SELECT id FROM acme_db.orders WHERE orderState=:orderState)", Map.of("orderState", orderState),
//        (rs, rowNum) -> {
//          St
//        });
    // Enums require explicit toString or else 'unable to convert binary to string' error.

    try{
      List<StaffOrder> staffOrderDetailList = namedParameterJdbcTemplate.query("SELECT id, customer_name, email, phone,"
              + " address_line1, address_line2, address_line3, postcode, city,"
              + " requiredDate, dispatchDatetime "
              + "FROM acme_db.orders "
              + "WHERE orderState=:orderState ORDER BY requiredDate ASC",
          Map.of("orderState", orderState.toString()),
          (row, rs) -> {
        Date requiredDate = row.getDate(11);
        StaffOrder staffOrder = new StaffOrder(
              row.getInt(1),
              row.getString(2),
              row.getString(3),
              row.getString(4),
              row.getString(5),
              row.getString(6),
              row.getString(7),
              row.getString(8),
              row.getString(9),
              null,
              row.getDate(10).toLocalDate().format(staffFormatter),
              requiredDate == null ? null : requiredDate.toLocalDate().format(staffFormatter),
              orderState.toString());
              return staffOrder;
          });

      // Load data for products involved in each order
      for (StaffOrder staffOrder : staffOrderDetailList){
        staffOrder.setOrderDetails(namedParameterJdbcTemplate.query("SELECT product_id, quantity, category, product_name"
            + " FROM acme_db.order_details INNER JOIN acme_db.product ON acme_db.order_details.product_id=acme_db.product.id"
            + " WHERE acme_db.order_details.order_id=:id",
            Map.of("id", staffOrder.getOrderId()),
            (row, rs) -> new StaffOrderDetail(
                row.getInt(1),
                row.getInt(2),
                ProductCategory.valueOf(row.getString(3)),
                row.getString(4)
        ) ));
      }
      System.out.println(staffOrderDetailList);
      return staffOrderDetailList;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Sets order to new state e.g. from NOT_READY to READY_TO_SHIP and throws error if order
   * does not exist or database error.
   * @param orderId
   * @param newState
   */
  public void setOrderState(int orderId, OrderState newState) throws Exception{
    int numRows;
    try {
      numRows = namedParameterJdbcTemplate.update("UPDATE acme_db.orders SET orderState=:newState WHERE id=:id",
          Map.of("id",orderId,"newState",newState.toString()));
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
    if (numRows != 1){
      throw new RuntimeException("order does not exist");
    }
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
    params.addValue("postcode",order.getPostcode());
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
          + "city, postcode, phone, shipping, customer_id, payment_amount, requiredDate) "
          + "VALUES (:customer_name, :email, :address_line1, :address_line2, :address_line3,"
          + ":city, :postcode, :phone, :shipping, :customer_id, :payment_amount, :requiredDate);",
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

    // save the order details
    for (OrderDetail orderDetail : order.getOrderDetails()){
      orderDetail.setOrderId(order.getOrderId());
      saveOrderDetail(orderDetail);
    }
    return ErrorCode.NO_ERROR;
  }

  /**
   * Save details of one product a customer ordered to the database
   * Saves (:productId, :orderId, :quantity, :price, :discount)
   * @param orderDetail
   */
  private void saveOrderDetail(OrderDetail orderDetail){
    try {
      Map<String, Object> params = Map.of("productId", orderDetail.getProductId(),
          "orderId", orderDetail.getOrderId(),
          "quantity", orderDetail.getQuantity(),
          "price", orderDetail.getPrice(),
          "discount", orderDetail.getDiscount()
          );
      namedParameterJdbcTemplate.update("INSERT INTO acme_db.order_details(product_id, order_id, quantity, price, discount)"
          + " VALUES (:productId, :orderId, :quantity, :price, :discount)",
          params);
    } catch (Exception e) {
      System.out.println("ERROR: saving " + orderDetail + " failed somehow " + e.getLocalizedMessage());
    }
  }
}
