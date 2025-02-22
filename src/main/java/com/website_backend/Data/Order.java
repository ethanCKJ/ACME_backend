package com.website_backend.Data;

/**
 * DTO for incoming order JSON
 * @param id
 * @param products
 * @param customerName
 * @param address - combination of all address lines separated by a comma
 * @param city
 * @param phone
 * @param shipping
 * @param customerId
 */
public record Order(int id,
                    OrderProduct[] products,
                    String customerName,
                    String address,
                    String city,
                    String phone,
                    Shipping shipping,
                    int customerId){}
