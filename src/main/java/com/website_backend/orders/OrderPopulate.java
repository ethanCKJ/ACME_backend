package com.website_backend.orders;

import com.website_backend.orders.enums.ErrorCode;
import com.website_backend.orders.enums.ProductCategory;
import com.website_backend.orders.dto.Order;
import com.website_backend.orders.dto.OrderDetail;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderPopulate {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final JdbcTemplate jdbcTemplate;

  public OrderPopulate(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
      JdbcTemplate jdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * If customerId exists, populate the customerName, email, addressLine(1-3), city, phone, postcode
   * If successful return ErrorCode.NO_ERROR If unable to populate return
   * ErrorCode.CUSTOMER_ID_NOT_EXIST
   *
   * @param order
   * @return
   */
  public ErrorCode populateRegisteredCustomerInfo(Order order) {
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("id", order.getCustomerId());
    List<Map<String, Object>> resultList = namedParameterJdbcTemplate.queryForList(
        "SELECT customer_name,"
            + " email,"
            + " address_line1,"
            + " address_line2,"
            + " address_line3, "
            + "city, "
            + "phone,"
            + "postcode FROM acme_db.customer WHERE id=:id LIMIT 1",
        params);

    if (resultList.isEmpty()) {
      return ErrorCode.CUSTOMER_ID_NOT_EXIST;
    }
    Map<String, Object> result = resultList.get(0);
    order.setCustomerName((String) result.get("customer_name"));
    order.setEmail((String) result.get("email"));
    order.setAddressLine1((String) result.get("address_line1"));
    order.setAddressLine2((String) result.get("address_line2"));
    order.setAddressLine3((String) result.get("address_line3"));
    order.setCity((String) result.get("city"));
    order.setPhone((String) result.get("phone"));
    order.setPostcode((String) result.get("postcode"));
    return ErrorCode.NO_ERROR;
  }

  /**
   * For each OrderDetail populate product id, price, product name and category.
   *
   * @param order
   * @return DATABASE_ERROR for database errors. PRODUCT_ID_NOT_EXIST if id does not exist and
   * INSUFFICIENT_STOCK if insufficient stock found
   */
  public ErrorCode populateOrderDetails(Order order) {
    Map<Integer, Integer> productIdToIndex = new HashMap<>();
    List<Integer> productIds = new ArrayList<>();
    for (int i = 0; i < order.getOrderDetails().length; i++) {
      productIds.add(order.getOrderDetails()[i].getProductId());
      productIdToIndex.put(order.getOrderDetails()[i].getProductId(), i);
    }
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("productIds", productIds);
    List<Map<String, Object>> resultList;
    try {
      resultList = namedParameterJdbcTemplate.queryForList(
          "SELECT id, price, stock, category, product_name FROM acme_db.product WHERE id IN (:productIds) AND is_discontinued=FALSE",
          params);
    } catch (DataAccessException e) {
      return ErrorCode.DATABASE_ERROR;
    }
    if (resultList.size() < productIdToIndex.size()) {
      return ErrorCode.PRODUCT_ID_NOT_EXIST;
    }
    // Check if product out of stock. If not, populate price.
    for (Map<String, Object> row : resultList) {
      int productId = (int) row.get("id");
      OrderDetail orderDetail = order.getOrderDetails()[productIdToIndex.get(productId)];
      if (orderDetail.getQuantity() > (int) row.get("stock")) {
        return ErrorCode.INSUFFICIENT_STOCK;
      }
      orderDetail.setPrice((int) row.get("price"));
      orderDetail.setCategory(ProductCategory.valueOf((String) row.get("category")));
      orderDetail.setProductName((String) row.get("product_name"));
    }
    return ErrorCode.NO_ERROR;
  }

  /**
   * Populate requiredDate to date order submitted + number of days allowed for delivery based on
   * delivery mode chosen by customer
   *
   * @param order
   */
  public void populateRequiredDate(Order order) {
    LocalDateTime localDateTime = LocalDateTime.now()
        .plusDays(order.getShipping().getTimeToDeliver());
    order.setRequiredDatetime(LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(),
        localDateTime.getDayOfMonth(), 18, 0));
  }
}
