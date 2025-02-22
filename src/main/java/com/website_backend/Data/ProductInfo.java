package com.website_backend.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductInfo {

  private int id;
  private String productInfo;
  private int stock;
  private double price;
  private ProductCategory productCategory;
  private String productName;
  private boolean isDiscontinued;

  public ProductInfo() {

  }

  public String getProductInfo() {
    return productInfo;
  }

  public void setProductInfo(String productInfo) {
    this.productInfo = productInfo;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public int getStock() {
    return stock;
  }

  public void setStock(int stock) {
    this.stock = stock;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public ProductCategory getProductCategory() {
    return productCategory;
  }

  public void setProductCategory(ProductCategory productCategory) {
    this.productCategory = productCategory;
  }

  public String getProductName() {
    return productName;
  }

  @JsonProperty("productName")
  public void setProductName(String productName) {
    this.productName = productName;
  }

  @JsonProperty("isDiscontinued")
  public boolean getIsDiscontinued() {
    return isDiscontinued;
  }

  public void setDiscontinued(boolean isDiscontinued) {
    this.isDiscontinued = isDiscontinued;
  }
}
