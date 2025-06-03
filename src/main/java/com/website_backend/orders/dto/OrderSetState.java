package com.website_backend.orders.dto;

import com.website_backend.orders.enums.OrderState;

public record OrderSetState(int orderId, OrderState newState) {

}
