package com.returnradar.service;

import com.returnradar.dto.request.OrderRequest;
import com.returnradar.dto.response.OrderResponse;
import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    OrderResponse getOrderById(Long id);

    List<OrderResponse> getOrdersByCustomer(Long customerId);
}
