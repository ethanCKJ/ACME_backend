package com.website_backend.orders.dto;

import com.website_backend.orders.enums.ProductCategory;

public record StaffOrderDetail(int productId, int quantity, ProductCategory category, String productName) {

}
