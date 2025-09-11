package com.website_backend.account.dto;

/**
 * Customer information loaded from database to autofill the order submission form
 * Note email can be acquired from the "sub" field of the JWT token
 * @param customerName
 * @param addressLine1
 * @param addressLine2
 * @param addressLine3
 * @param phone
 * @param postcode
 * @param city
 */
public record CustomerProfile(
    String customerName,
    String addressLine1,
    String addressLine2,
    String addressLine3,
    String phone,
    String postcode,
    String city
) { }
