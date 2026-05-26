package com.returnradar.service.impl;

import com.returnradar.dto.request.CustomerRequest;
import com.returnradar.dto.response.CustomerResponse;
import com.returnradar.entity.Customer;
import com.returnradar.exception.BusinessRuleException;
import com.returnradar.exception.ResourceNotFoundException;
import com.returnradar.mapper.CustomerMapper;
import com.returnradar.repository.CustomerRepository;
import com.returnradar.service.CustomerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        customerRepository.findByEmail(request.getEmail())
                .ifPresent(customer -> {
                    throw new BusinessRuleException("Customer already exists with email: " + request.getEmail());
                });

        Customer customer = CustomerMapper.toEntity(request);
        return CustomerMapper.toResponse(customerRepository.save(customer));
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(CustomerMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }
}
