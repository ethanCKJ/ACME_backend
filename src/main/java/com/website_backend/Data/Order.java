package com.website_backend.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.website_backend.Data.enums.Shipping;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Order {
  @JsonProperty("orderDetails")
  private OrderDetail[] orderDetails;

  private int orderId;

  @JsonProperty("customerId")
  private Integer customerId;

  @JsonProperty("customerName")
  private String customerName;

  @JsonProperty("email")
  private String email;

  @JsonProperty("addressLine1")
  private String addressLine1;

  @JsonProperty("addressLine2")
  private String addressLine2;

  @JsonProperty("addressLine3")
  private String addressLine3;

  @JsonProperty("city")
  private String city;

  @JsonProperty("phone")
  private String phone;

  @JsonProperty("postcode")
  private String postcode;

  @JsonProperty("shipping")
  private Shipping shipping;

  private LocalDateTime deliveredDatetime;
  private LocalDateTime dispatchDatetime;
  private LocalDateTime requiredDatetime;

  public Order() {
  }

  /**
   * DTO for each customer order.
   * The partial constructor is for deserializing an order then populating price, shipping dates
   * from the database.
   * @param customerId null if customer checked out as guest
   * @param customerName - if customerId is not null we get customerName, email, addressLine(1-3), city, phone from the database
   * @param email
   * @param addressLine1
   * @param addressLine2
   * @param addressLine3
   * @param city
   * @param phone
   * @param shipping
   */

  public Order(Integer customerId, String customerName,
      String email, String addressLine1, String addressLine2, String addressLine3, String city,
      String phone, Shipping shipping) {
    this.customerId = customerId;
    this.customerName = customerName;
    this.email = email;
    this.addressLine1 = addressLine1;
    this.addressLine2 = addressLine2;
    this.addressLine3 = addressLine3;
    this.city = city;
    this.phone = phone;
    this.shipping = shipping;
  }

  /**
   * DTO for each customer order.
   * The full is for loading an order from the database for the order management system.
   * @param orderDetails details for each product the customer ordered
   * @param orderId
   * @param customerId null if customer checked out as guest
   * @param customerName - if customerId is not null we get customerName, email, addressLine(1-3), city, phone from the database
   * @param email
   * @param addressLine1
   * @param addressLine2
   * @param addressLine3
   * @param city
   * @param phone
   * @param shipping
   * @param deliveredDatetime
   * @param dispatchDatetime
   * @param requiredDatetime
   */
  public Order(OrderDetail[] orderDetails, int orderId, Integer customerId, String customerName,
      String email, String addressLine1, String addressLine2, String addressLine3, String city,
      String phone, Shipping shipping, LocalDateTime deliveredDatetime,
      LocalDateTime dispatchDatetime, LocalDateTime requiredDatetime) {
    this.orderDetails = orderDetails;
    this.orderId = orderId;
    this.customerId = customerId;
    this.customerName = customerName;
    this.email = email;
    this.addressLine1 = addressLine1;
    this.addressLine2 = addressLine2;
    this.addressLine3 = addressLine3;
    this.city = city;
    this.phone = phone;
    this.shipping = shipping;
    this.deliveredDatetime = deliveredDatetime;
    this.dispatchDatetime = dispatchDatetime;
    this.requiredDatetime = requiredDatetime;
  }

  public OrderDetail[] getOrderDetails() {
    return orderDetails;
  }

  public void setOrderDetails(OrderDetail[] orderDetails) {
    this.orderDetails = orderDetails;
  }

  public int getOrderId() {
    return orderId;
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }

  public Integer getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Integer customerId) {
    this.customerId = customerId;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAddressLine1() {
    return addressLine1;
  }

  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  public String getAddressLine2() {
    return addressLine2;
  }

  public void setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
  }

  public String getAddressLine3() {
    return addressLine3;
  }

  public void setAddressLine3(String addressLine3) {
    this.addressLine3 = addressLine3;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Shipping getShipping() {
    return shipping;
  }

  public void setShipping(Shipping shipping) {
    this.shipping = shipping;
  }

  public LocalDateTime getDeliveredDatetime() {
    return deliveredDatetime;
  }

  public void setDeliveredDatetime(LocalDateTime deliveredDatetime) {
    this.deliveredDatetime = deliveredDatetime;
  }

  public LocalDateTime getDispatchDatetime() {
    return dispatchDatetime;
  }

  public void setDispatchDatetime(LocalDateTime dispatchDatetime) {
    this.dispatchDatetime = dispatchDatetime;
  }

  public LocalDateTime getRequiredDatetime() {
    return requiredDatetime;
  }

  public void setRequiredDatetime(LocalDateTime requiredDatetime) {
    this.requiredDatetime = requiredDatetime;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  @Override
  public String toString() {
    return "Order{" +
        "orderDetails=" + Arrays.toString(orderDetails) +
        ", orderId=" + orderId +
        ", customerId=" + customerId +
        ", customerName='" + customerName + '\'' +
        ", email='" + email + '\'' +
        ", addressLine1='" + addressLine1 + '\'' +
        ", addressLine2='" + addressLine2 + '\'' +
        ", addressLine3='" + addressLine3 + '\'' +
        ", city='" + city + '\'' +
        ", phone='" + phone + '\'' +
        ", postcode='" + postcode + '\'' +
        ", shipping=" + shipping +
        ", deliveredDatetime=" + deliveredDatetime +
        ", dispatchDatetime=" + dispatchDatetime +
        ", requiredDatetime=" + requiredDatetime +
        '}';
  }
}
