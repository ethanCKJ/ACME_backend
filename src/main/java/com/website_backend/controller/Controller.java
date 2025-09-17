package com.website_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import com.website_backend.product.ProductInfo;
import com.website_backend.errors.DatabaseException;
import com.website_backend.orders.OrderService;
import com.website_backend.orders.dto.Order;
import com.website_backend.orders.dto.OrderResponse;
import com.website_backend.orders.dto.OrderSetState;
import com.website_backend.orders.dto.StaffOrder;
import com.website_backend.orders.enums.OrderState;
import com.website_backend.orders.errors.CustomerIdNotExistException;
import com.website_backend.orders.errors.InsufficientStockException;
import com.website_backend.orders.errors.ProductIdNotExistException;
import com.website_backend.product.ProductService;
import com.website_backend.user.CustomerRepository;
import com.website_backend.user.StaffRepository;
import com.website_backend.user.dto.CustomerProfile;
import com.website_backend.user.dto.StaffProfile;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for all functions except getting tokens and signing up customers / staff
 */
@RestController
public class Controller {

  private final OrderService orderService;
  private final ProductService productService;
  private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final CustomerRepository customerRepository;

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  JsonSchema orderSchema;
  @Autowired
  private StaffRepository staffAccountRepository;

  public Controller(OrderService orderService, ProductService productService,
      CustomerRepository customerRepository) {
    this.orderService = orderService;
    this.productService = productService;
    this.customerRepository = customerRepository;
  }

  @GetMapping("/greeting")
  public ResponseEntity<?> greeting(){
    return new ResponseEntity<>("Hello world!", HttpStatus.OK);
  }

  /**
   * Get product details of all products in a particular category and within minPrice and maxPrice (inclusive)
   * @param category
   * @param minPrice
   * @param maxPrice
   * @return
   */
  @GetMapping("/products/{category}")
  public ResponseEntity<?> getProducts(@PathVariable String category, @RequestParam int minPrice,
      @RequestParam int maxPrice) {
    List<ProductInfo> productInfoList = productService.getProducts(category, minPrice, maxPrice);
    return new ResponseEntity<>(productInfoList, HttpStatus.OK);
  }

  @PostMapping("/order")
  public ResponseEntity<?> placeOrder(@RequestBody String json, Authentication auth) {
    JsonNode jsonNode;
    try {
      jsonNode = objectMapper.readTree(json);
      Set<ValidationMessage> msg = orderSchema.validate(jsonNode);
      if (msg.isEmpty()) {
        Order order = objectMapper.treeToValue(jsonNode, Order.class);
        if (auth instanceof JwtAuthenticationToken) {
          Jwt jwt = ((JwtAuthenticationToken) auth).getToken();
          Long customerId = jwt.getClaim("id");
          order.setCustomerId(customerId.intValue());
          order.setEmail(jwt.getSubject());
        }
        try {
          orderService.handleOrder(order);
          OrderResponse response = new OrderResponse(order.getOrderId(),
              order.getEmail(),
              fmt.format(order.getRequiredDatetime()));
          return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DatabaseException e) {
          return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ProductIdNotExistException | CustomerIdNotExistException | InsufficientStockException e){
          return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @PreAuthorize("hasRole('CUSTOMER')")
  @GetMapping("me/customer_profile_info")
  public ResponseEntity<?> getCustomerProfileInfo(Authentication auth) {
    if (auth instanceof JwtAuthenticationToken) {
      Jwt jwt = ((JwtAuthenticationToken) auth).getToken();
      Long customerId = jwt.getClaim("id");
      try {
        CustomerProfile response = customerRepository.loadCustomerByUsername(customerId.intValue());
        return new ResponseEntity<>(response, HttpStatus.OK);
      } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
      }
    }
    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
  }

  @PreAuthorize("hasRole('STAFF')")
  @GetMapping("me/staff_profile_info")
  public ResponseEntity<?> getStaffProfileInfo(Authentication auth) {
    if (auth instanceof JwtAuthenticationToken) {
      Jwt jwt = ((JwtAuthenticationToken) auth).getToken();
      Long staffId = jwt.getClaim("id");
      try {
        StaffProfile response = staffAccountRepository.getStaffProfile(staffId.intValue());
        return new ResponseEntity<>(response, HttpStatus.OK);
      } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
      }
    }
    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
  }

  //  @PreAuthorize("hasRole('STAFF','ADMIN')")
  @GetMapping("/view_orders")
  public ResponseEntity<?> viewOrders(@RequestParam OrderState orderState) {
    List<StaffOrder> staffOrders = orderService.viewOrder(orderState);
    return new ResponseEntity<>(staffOrders, HttpStatus.OK);
  }

  @PostMapping("/set_order_state")
  public ResponseEntity<?> setOrderState(@RequestBody OrderSetState orderSetState)
      throws Exception {
    orderService.setOrderState(orderSetState);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
