package com.website_backend.orders;

import com.website_backend.orders.dto.Order;
import com.website_backend.orders.dto.OrderDetail;
import com.website_backend.orders.dto.OrderSetState;
import com.website_backend.orders.dto.StaffOrder;
import com.website_backend.orders.enums.OrderState;
import com.website_backend.orders.errors.CustomerIdNotExistException;
import com.website_backend.errors.DatabaseException;
import com.website_backend.orders.errors.InsufficientStockException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
   *
   * @param order
   * @return
   */
  public void handleOrder(Order order)
      throws DatabaseException, InsufficientStockException, CustomerIdNotExistException {
    // Throw exception if any product in the order has insufficient stock.
    for (OrderDetail orderDetail : order.getOrderDetails()) {
      int stock = orderRepository.getStock(orderDetail.getProductId());
      if (stock < orderDetail.getQuantity()) {
        throw new InsufficientStockException(
            "Insufficient stock level of %d units productId '%d' to satisfy request of %d".formatted(
                stock, orderDetail.getProductName(), orderDetail.getProductId(),
                orderDetail.getQuantity()));
      }
    }

    // Populate order with customer info, product details (e.g. price) and date the order must be delivered
    if (order.getCustomerId() != null) {
      orderPopulate.populateRegisteredCustomerInfo(order);
    }
    orderPopulate.populateOrderDetails(order);
    orderPopulate.populateRequiredDate(order);
    // Save entire order to database
    orderRepository.saveOrder(order);
    // Decrement stock of all products in the order
    for (OrderDetail orderDetail : order.getOrderDetails()) {
      orderRepository.decrementStock(orderDetail.getProductId(), orderDetail.getQuantity());
    }
  }

  public List<StaffOrder> viewOrder(OrderState orderState) {
    return orderRepository.loadStaffOrders(orderState);
  }

  public void setOrderState(OrderSetState orderSetState) throws Exception {
    try {
      orderRepository.setOrderState(orderSetState.orderId(), orderSetState.newState());
    } catch (Exception e) {
      if (e.getLocalizedMessage().contains("not exist")) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order does not exist");
      } else {
        throw new RuntimeException(e);
      }
    }
  }
}
