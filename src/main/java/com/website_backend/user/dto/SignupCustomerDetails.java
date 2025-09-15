package com.website_backend.user.dto;

public record SignupCustomerDetails(String customerName,
                                    String phone,
                                    String addressLine1,
                                    String addressLine2,
                                    String addressLine3,
                                    String postcode,
                                    String city,
                                    String username,
                                    String password
                                    ) {
}
