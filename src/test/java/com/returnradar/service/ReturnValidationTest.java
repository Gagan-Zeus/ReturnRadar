package com.returnradar.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.returnradar.dto.request.ReturnRequestDto;
import com.returnradar.entity.Customer;
import com.returnradar.entity.Order;
import com.returnradar.entity.Product;
import com.returnradar.enums.ReturnReason;
import com.returnradar.exception.BusinessRuleException;
import com.returnradar.exception.ResourceNotFoundException;
import com.returnradar.exception.ValidationException;
import com.returnradar.repository.CustomerRepository;
import com.returnradar.repository.DataQualityIssueRepository;
import com.returnradar.repository.OrderRepository;
import com.returnradar.repository.ProductRepository;
import com.returnradar.repository.ReturnRequestRepository;
import com.returnradar.service.impl.ReturnRequestServiceImpl;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReturnValidationTest {

    @Mock
    private ReturnRequestRepository returnRequestRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DataQualityIssueRepository dataQualityIssueRepository;

    @InjectMocks
    private ReturnRequestServiceImpl returnRequestService;

    @Test
    void test_createReturn_throwsValidationException_whenQuantityIsZero() {
        ReturnRequestDto dto = validReturnRequest();
        dto.setQuantity(0);

        assertThrows(ValidationException.class, () -> returnRequestService.createReturn(dto));

        verify(customerRepository, never()).findById(any());
    }

    @Test
    void test_createReturn_throwsResourceNotFoundException_whenCustomerNotFound() {
        ReturnRequestDto dto = validReturnRequest();
        when(customerRepository.findById(dto.getCustomerId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> returnRequestService.createReturn(dto));

        verify(productRepository, never()).findById(any());
    }

    @Test
    void test_createReturn_throwsResourceNotFoundException_whenProductNotFound() {
        ReturnRequestDto dto = validReturnRequest();
        when(customerRepository.findById(dto.getCustomerId())).thenReturn(Optional.of(validCustomer()));
        when(productRepository.findById(dto.getProductId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> returnRequestService.createReturn(dto));

        verify(orderRepository, never()).findById(any());
    }

    @Test
    void test_createReturn_throwsBusinessRuleException_whenReturnDateBeforeOrderDate() {
        ReturnRequestDto dto = validReturnRequest();
        when(customerRepository.findById(dto.getCustomerId())).thenReturn(Optional.of(validCustomer()));
        when(productRepository.findById(dto.getProductId())).thenReturn(Optional.of(validProduct()));
        when(orderRepository.findById(dto.getOrderId())).thenReturn(Optional.of(futureOrder()));
        when(returnRequestRepository.existsByOrderIdAndProductId(dto.getOrderId(), dto.getProductId()))
                .thenReturn(false);

        assertThrows(BusinessRuleException.class, () -> returnRequestService.createReturn(dto));
    }

    private ReturnRequestDto validReturnRequest() {
        return ReturnRequestDto.builder()
                .orderId(1L)
                .productId(2L)
                .customerId(3L)
                .reason(ReturnReason.SIZE_TOO_SMALL)
                .quantity(1)
                .refundAmount(100.0)
                .build();
    }

    private Customer validCustomer() {
        return Customer.builder()
                .customerId(3L)
                .name("Test Customer")
                .email("test@email.com")
                .totalReturns(0)
                .totalRefundAmount(0.0)
                .build();
    }

    private Product validProduct() {
        return Product.builder()
                .productId(2L)
                .name("Test Product")
                .price(500.0)
                .totalSold(10)
                .totalReturned(0)
                .build();
    }

    private Order futureOrder() {
        return Order.builder()
                .orderId(1L)
                .orderDate(LocalDateTime.now().plusDays(1))
                .build();
    }
}
