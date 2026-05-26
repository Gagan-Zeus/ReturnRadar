package com.returnradar.mapper;

import com.returnradar.dto.request.CustomerRequest;
import com.returnradar.dto.response.CustomerResponse;
import com.returnradar.entity.Customer;

public final class CustomerMapper {

    private CustomerMapper() {
    }

    public static CustomerResponse toResponse(Customer customer) {
        if (customer == null) {
            return null;
        }

        return CustomerResponse.builder()
                .customerId(customer.getCustomerId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .city(customer.getCity())
                .registeredAt(customer.getRegisteredAt())
                .totalOrders(customer.getTotalOrders())
                .totalReturns(customer.getTotalReturns())
                .totalRefundAmount(customer.getTotalRefundAmount())
                .flaggedAsSuspicious(customer.getFlaggedAsSuspicious())
                .build();
    }

    public static Customer toEntity(CustomerRequest request) {
        if (request == null) {
            return null;
        }

        return Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .city(request.getCity())
                .build();
    }
}
