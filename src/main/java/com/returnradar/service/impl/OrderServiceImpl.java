package com.returnradar.service.impl;

import com.returnradar.dto.request.OrderItemRequest;
import com.returnradar.dto.request.OrderRequest;
import com.returnradar.dto.response.OrderResponse;
import com.returnradar.entity.Customer;
import com.returnradar.entity.Order;
import com.returnradar.entity.OrderItem;
import com.returnradar.entity.Product;
import com.returnradar.exception.ResourceNotFoundException;
import com.returnradar.mapper.OrderMapper;
import com.returnradar.repository.CustomerRepository;
import com.returnradar.repository.OrderItemRepository;
import com.returnradar.repository.OrderRepository;
import com.returnradar.repository.ProductRepository;
import com.returnradar.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with id: " + request.getCustomerId()));

        Order order = Order.builder()
                .customer(customer)
                .orderDate(request.getOrderDate())
                .deliveryDate(request.getDeliveryDate())
                .totalAmount(request.getTotalAmount())
                .city(request.getCity())
                .status("CREATED")
                .build();

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> savedItems = request.getItems()
                .stream()
                .map(itemRequest -> createOrderItem(savedOrder, itemRequest))
                .toList();

        customer.setTotalOrders(valueOrZero(customer.getTotalOrders()) + 1);
        customerRepository.save(customer);

        return OrderMapper.toResponse(savedOrder, savedItems);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        List<OrderItem> items = orderItemRepository.findByOrder_OrderId(id);
        return OrderMapper.toResponse(order, items);
    }

    @Override
    public List<OrderResponse> getOrdersByCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with id: " + customerId);
        }

        return orderRepository.findByCustomer_CustomerId(customerId)
                .stream()
                .map(order -> OrderMapper.toResponse(
                        order,
                        orderItemRepository.findByOrder_OrderId(order.getOrderId())))
                .toList();
    }

    private OrderItem createOrderItem(Order order, OrderItemRequest itemRequest) {
        Product product = productRepository.findById(itemRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + itemRequest.getProductId()));

        Integer quantity = itemRequest.getQuantity();
        product.setTotalSold(valueOrZero(product.getTotalSold()) + valueOrZero(quantity));
        productRepository.save(product);

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .unitPrice(itemRequest.getUnitPrice())
                .size(itemRequest.getSize())
                .build();

        return orderItemRepository.save(orderItem);
    }

    private Integer valueOrZero(Integer value) {
        return value == null ? 0 : value;
    }
}
