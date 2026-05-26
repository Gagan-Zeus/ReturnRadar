package com.returnradar.service;

import com.returnradar.dto.request.CustomerRequest;
import com.returnradar.dto.response.CustomerResponse;
import java.util.List;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerRequest request);

    CustomerResponse getCustomerById(Long id);

    List<CustomerResponse> getAllCustomers();
}
