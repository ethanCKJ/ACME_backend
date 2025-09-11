package com.website_backend.orders.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * View of an order sent to staff
 */
public class StaffOrder {

  private int orderId;
  private String customerName;
  private String email;
  private String phone;
  private String addressLine1;
  private String addressLine2;
  private String addressLine3;
  private String postcode;
  private String city;
  private List<StaffOrderDetail> orderDetails;
  private String requiredDate;
  private String dispatchDate;
  private String orderState;

  public StaffOrder(
      int orderId,
      String customerName,
      String email,
      String phone,
      String addressLine1,
      String addressLine2,
      String addressLine3,
      String postcode,
      String city,
      List<StaffOrderDetail> orderDetails,
      String requiredDate,   // “2025-05-30”
      String dispatchDate,   // or null
      String orderState      // e.g. “READY_TO_SHIP”
  ) {
    this.orderId = orderId;
    this.customerName = customerName;
    this.email = email;
    this.phone = phone;
    this.addressLine1 = addressLine1;
    this.addressLine2 = addressLine2;
    this.addressLine3 = addressLine3;
    this.postcode = postcode;
    this.city = city;
    this.orderDetails = orderDetails;
    this.requiredDate = requiredDate;
    this.dispatchDate = dispatchDate;
    this.orderState = orderState;
  }

  public int getOrderId() {
    return orderId;
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
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

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
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

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public List<StaffOrderDetail> getOrderDetails() {
    return orderDetails;
  }

  public void setOrderDetails(List<StaffOrderDetail> orderDetails) {
    this.orderDetails = orderDetails;
  }

  public String getRequiredDate() {
    return requiredDate;
  }

  public void setRequiredDate(String requiredDate) {
    this.requiredDate = requiredDate;
  }

  public String getDispatchDate() {
    return dispatchDate;
  }

  public void setDispatchDate(String dispatchDate) {
    this.dispatchDate = dispatchDate;
  }

  public String getOrderState() {
    return orderState;
  }

  public void setOrderState(String orderState) {
    this.orderState = orderState;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (StaffOrder) obj;
    return this.orderId == that.orderId &&
        Objects.equals(this.customerName, that.customerName) &&
        Objects.equals(this.email, that.email) &&
        Objects.equals(this.phone, that.phone) &&
        Objects.equals(this.addressLine1, that.addressLine1) &&
        Objects.equals(this.addressLine2, that.addressLine2) &&
        Objects.equals(this.addressLine3, that.addressLine3) &&
        Objects.equals(this.postcode, that.postcode) &&
        Objects.equals(this.city, that.city) &&
        Objects.equals(this.orderDetails, that.orderDetails) &&
        Objects.equals(this.requiredDate, that.requiredDate) &&
        Objects.equals(this.dispatchDate, that.dispatchDate) &&
        Objects.equals(this.orderState, that.orderState);
  }



  @Override
  public int hashCode() {
    return Objects.hash(orderId, customerName, email, phone, addressLine1, addressLine2,
        addressLine3, postcode, city, orderDetails, requiredDate, dispatchDate, orderState);
  }

  @Override
  public String toString() {
    return "StaffOrder[" +
        "orderId=" + orderId + ", " +
        "customerName=" + customerName + ", " +
        "username=" + email + ", " +
        "phone=" + phone + ", " +
        "addressLine1=" + addressLine1 + ", " +
        "addressLine2=" + addressLine2 + ", " +
        "addressLine3=" + addressLine3 + ", " +
        "postcode=" + postcode + ", " +
        "city=" + city + ", " +
        "orderDetails=" + orderDetails + ", " +
        "requiredDate=" + requiredDate + ", " +
        "dispatchDate=" + dispatchDate + ", " +
        "orderState=" + orderState + ']';
  }
}
