package com.returnradar.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.returnradar.dto.request.ReturnRequestDto;
import com.returnradar.entity.Customer;
import com.returnradar.entity.Order;
import com.returnradar.entity.Product;
import com.returnradar.enums.ReturnReason;
import com.returnradar.exception.DuplicateReturnException;
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
class DuplicateReturnTest {

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
    void test_createReturn_throwsDuplicateReturnException_whenDuplicateExists() {
        ReturnRequestDto dto = validReturnRequest();
        when(customerRepository.findById(dto.getCustomerId())).thenReturn(Optional.of(validCustomer()));
        when(productRepository.findById(dto.getProductId())).thenReturn(Optional.of(validProduct()));
        when(orderRepository.findById(dto.getOrderId())).thenReturn(Optional.of(validOrder()));
        when(returnRequestRepository.existsByOrderIdAndProductId(dto.getOrderId(), dto.getProductId()))
                .thenReturn(true);

        assertThrows(DuplicateReturnException.class, () -> returnRequestService.createReturn(dto));
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
        return Customer.builder().customerId(3L).totalReturns(0).totalRefundAmount(0.0).build();
    }

    private Product validProduct() {
        return Product.builder().productId(2L).price(500.0).totalSold(10).totalReturned(0).build();
    }

    private Order validOrder() {
        return Order.builder().orderId(1L).orderDate(LocalDateTime.now().minusDays(1)).build();
    }
}
