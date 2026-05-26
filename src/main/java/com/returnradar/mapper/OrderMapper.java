package com.returnradar.mapper;

import com.returnradar.dto.response.OrderItemResponse;
import com.returnradar.dto.response.OrderResponse;
import com.returnradar.entity.Order;
import com.returnradar.entity.OrderItem;
import java.util.List;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponse toResponse(Order order, List<OrderItem> items) {
        if (order == null) {
            return null;
        }

        Long customerId = order.getCustomer() == null ? null : order.getCustomer().getCustomerId();
        List<OrderItemResponse> itemResponses = items == null
                ? List.of()
                : items.stream()
                        .map(OrderMapper::toItemResponse)
                        .toList();

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .customerId(customerId)
                .orderDate(order.getOrderDate())
                .deliveryDate(order.getDeliveryDate())
                .totalAmount(order.getTotalAmount())
                .city(order.getCity())
                .status(order.getStatus())
                .items(itemResponses)
                .build();
    }

    public static OrderItemResponse toItemResponse(OrderItem item) {
        if (item == null) {
            return null;
        }

        Long productId = item.getProduct() == null ? null : item.getProduct().getProductId();
        String productName = item.getProduct() == null ? null : item.getProduct().getName();

        return OrderItemResponse.builder()
                .orderItemId(item.getOrderItemId())
                .productId(productId)
                .productName(productName)
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .size(item.getSize())
                .returnRequested(item.getReturnRequested())
                .build();
    }
}
