package com.website_backend.Data.enums;

public enum Shipping {
  FREE(0,10),
  STANDARD(100,7),
  PREMIUM(300,3);

  private final int shippingPrice;
  private final int timeToDeliver;

  Shipping(int shippingPrice, int timeToDeliver){
    this.shippingPrice = shippingPrice;
    this.timeToDeliver = timeToDeliver;
  }

  public int getShippingPrice() {
    return shippingPrice;
  }

  public int getTimeToDeliver() {
    return timeToDeliver;
  }
}
