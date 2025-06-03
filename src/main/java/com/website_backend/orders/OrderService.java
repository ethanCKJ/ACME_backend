package com.website_backend.orders;

import com.website_backend.orders.dto.StaffOrder;
import com.website_backend.orders.enums.ErrorCode;
import com.website_backend.orders.dto.Order;
import com.website_backend.orders.enums.OrderState;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final OrderPopulate orderPopulate;
  private final OrderRepository orderRepository;

  public OrderService(OrderPopulate orderPopulate, OrderRepository orderRepository) {
    this.orderPopulate = orderPopulate;
    this.orderRepository = orderRepository;
  }

  /**
   * Populates order with necessary data from database and saves order to database. Returns any
   * errors present
   * @param order
   * @return
   */
  public ErrorCode handleOrder(Order order) {
    ErrorCode errorCode;
    if (order.getCustomerId() != null) {
      errorCode = orderPopulate.populateRegisteredCustomerInfo(order);
      if (errorCode != ErrorCode.NO_ERROR) {
        return errorCode;
      }
    }
    errorCode = orderPopulate.populateOrderDetails(order);
    if (errorCode != ErrorCode.NO_ERROR) {
      return errorCode;
    }

    orderPopulate.populateRequiredDate(order);
    errorCode = orderRepository.saveOrder(order);

    return errorCode;
  }

  public List<StaffOrder> viewOrder(OrderState orderState){
    List<StaffOrder> staffOrder = orderRepository.loadStaffOrders(orderState);
    return staffOrder;
  }

}
