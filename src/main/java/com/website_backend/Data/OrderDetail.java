package com.website_backend.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.website_backend.Data.enums.ProductCategory;

public class OrderDetail {
  @JsonProperty("productId")
  private int productId;

  private int orderId;

  @JsonProperty("quantity")
  private int quantity;

  private int price;
  private double discount;
  private int total;
  private ProductCategory category;
  private String productName;

  public OrderDetail(){
  }

  /**
   * Full constructor is for loading information about each product in an order from the database
   * @param productId
   * @param orderId
   * @param quantity
   * @param price
   * @param discount
   * @param total
   * @param category
   */
  public OrderDetail(int productId, int orderId, int quantity, int price, double discount,
      int total, ProductCategory category) {
    this.productId = productId;
    this.orderId = orderId;
    this.quantity = quantity;
    this.price = price;
    this.discount = discount;
    this.total = total;
    this.category = category;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public int getOrderId() {
    return orderId;
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public double getDiscount() {
    return discount;
  }

  public void setDiscount(double discount) {
    this.discount = discount;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public ProductCategory getCategory() {
    return category;
  }

  public void setCategory(ProductCategory category) {
    this.category = category;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  @Override
  public String toString() {
    return "OrderDetail{" +
        "productId=" + productId +
        ", orderId=" + orderId +
        ", quantity=" + quantity +
        ", price=" + price +
        ", discount=" + discount +
        ", total=" + total +
        ", category=" + category +
        ", productName='" + productName + '\'' +
        '}';
  }
}
