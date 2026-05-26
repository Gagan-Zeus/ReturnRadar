package com.returnradar.seed;

import com.returnradar.entity.Customer;
import com.returnradar.entity.Order;
import com.returnradar.entity.OrderItem;
import com.returnradar.entity.Product;
import com.returnradar.entity.ReturnInspection;
import com.returnradar.entity.ReturnRequest;
import com.returnradar.enums.ProductCategory;
import com.returnradar.enums.ProductCondition;
import com.returnradar.enums.ReturnReason;
import com.returnradar.enums.ReturnStatus;
import com.returnradar.repository.CustomerRepository;
import com.returnradar.repository.OrderItemRepository;
import com.returnradar.repository.OrderRepository;
import com.returnradar.repository.ProductRepository;
import com.returnradar.repository.ReturnInspectionRepository;
import com.returnradar.repository.ReturnRequestRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ReturnRequestRepository returnRequestRepository;
    private final ReturnInspectionRepository returnInspectionRepository;

    @Override
    public void run(String... args) {
        if (customerRepository.count() > 0) {
            return;
        }

        List<Customer> customers = seedCustomers();
        List<Product> products = seedProducts();
        List<Order> orders = seedOrders(customers);
        seedOrderItems(orders, products);
        List<ReturnRequest> returnRequests = seedReturnRequests(customers, products, orders);
        seedReturnInspections(returnRequests);
    }

    private List<Customer> seedCustomers() {
        List<Customer> customers = List.of(
                Customer.builder()
                        .name("Rahul Sharma")
                        .email("rahul@email.com")
                        .city("Bangalore")
                        .totalOrders(3)
                        .totalReturns(1)
                        .totalRefundAmount(4999.0)
                        .build(),
                Customer.builder()
                        .name("Priya Singh")
                        .email("priya@email.com")
                        .city("Mumbai")
                        .totalOrders(4)
                        .totalReturns(2)
                        .totalRefundAmount(5998.0)
                        .build(),
                Customer.builder()
                        .name("Aman Verma")
                        .email("aman@email.com")
                        .city("Delhi")
                        .totalOrders(2)
                        .totalReturns(1)
                        .totalRefundAmount(2499.0)
                        .build(),
                Customer.builder()
                        .name("Sneha Rao")
                        .email("sneha@email.com")
                        .city("Hyderabad")
                        .totalOrders(3)
                        .totalReturns(1)
                        .totalRefundAmount(3499.0)
                        .build(),
                Customer.builder()
                        .name("Vikram Patel")
                        .email("vikram@email.com")
                        .city("Pune")
                        .totalOrders(8)
                        .totalReturns(6)
                        .totalRefundAmount(12495.0)
                        .flaggedAsSuspicious(true)
                        .build()
        );

        return customerRepository.saveAll(customers);
    }

    private List<Product> seedProducts() {
        List<Product> products = List.of(
                buildProduct("Nike Air Max", "Nike", ProductCategory.SHOES, "9", 4999.0, 120, 18),
                buildProduct("Levi's 511 Jeans", "Levi's", ProductCategory.JEANS, "32", 2499.0, 95, 12),
                buildProduct("Zara Floral Dress", "Zara", ProductCategory.DRESSES, "M", 3499.0, 70, 15),
                buildProduct("H&M Cotton Shirt", "H&M", ProductCategory.SHIRTS, "L", 999.0, 180, 9),
                buildProduct("Puma Running Jacket", "Puma", ProductCategory.JACKETS, "XL", 3999.0, 65, 10),
                buildProduct("Adidas Stan Smith", "Adidas", ProductCategory.SHOES, "8", 5499.0, 110, 20),
                buildProduct("Mango Slim Fit Jeans", "Mango", ProductCategory.JEANS, "30", 2999.0, 80, 8),
                buildProduct("Allen Solly Formal Shirt", "Allen Solly", ProductCategory.SHIRTS, "XL", 1499.0, 150, 7)
        );

        return productRepository.saveAll(products);
    }

    private Product buildProduct(
            String name,
            String brand,
            ProductCategory category,
            String size,
            Double price,
            Integer totalSold,
            Integer totalReturned
    ) {
        return Product.builder()
                .name(name)
                .brand(brand)
                .category(category)
                .size(size)
                .price(price)
                .stockQuantity(100)
                .totalSold(totalSold)
                .totalReturned(totalReturned)
                .returnRate((totalReturned * 100.0) / totalSold)
                .build();
    }

    private List<Order> seedOrders(List<Customer> customers) {
        LocalDateTime now = LocalDateTime.now();
        List<Order> orders = List.of(
                buildOrder(customers.get(0), now.minusDays(12), now.minusDays(8), 4999.0, "Bangalore"),
                buildOrder(customers.get(1), now.minusDays(10), now.minusDays(6), 5998.0, "Mumbai"),
                buildOrder(customers.get(2), now.minusDays(9), now.minusDays(5), 2499.0, "Delhi"),
                buildOrder(customers.get(3), now.minusDays(8), now.minusDays(4), 3499.0, "Hyderabad"),
                buildOrder(customers.get(4), now.minusDays(7), now.minusDays(3), 12495.0, "Pune")
        );

        return orderRepository.saveAll(orders);
    }

    private Order buildOrder(
            Customer customer,
            LocalDateTime orderDate,
            LocalDateTime deliveryDate,
            Double totalAmount,
            String city
    ) {
        return Order.builder()
                .customer(customer)
                .orderDate(orderDate)
                .deliveryDate(deliveryDate)
                .totalAmount(totalAmount)
                .city(city)
                .status("DELIVERED")
                .build();
    }

    private void seedOrderItems(List<Order> orders, List<Product> products) {
        List<OrderItem> orderItems = List.of(
                buildOrderItem(orders.get(0), products.get(0), 1),
                buildOrderItem(orders.get(1), products.get(1), 1),
                buildOrderItem(orders.get(1), products.get(3), 1),
                buildOrderItem(orders.get(2), products.get(1), 1),
                buildOrderItem(orders.get(3), products.get(2), 1),
                buildOrderItem(orders.get(4), products.get(5), 1),
                buildOrderItem(orders.get(4), products.get(6), 1),
                buildOrderItem(orders.get(4), products.get(7), 1)
        );

        orderItemRepository.saveAll(orderItems);
    }

    private OrderItem buildOrderItem(Order order, Product product, Integer quantity) {
        return OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .unitPrice(product.getPrice())
                .size(product.getSize())
                .returnRequested(true)
                .build();
    }

    private List<ReturnRequest> seedReturnRequests(
            List<Customer> customers,
            List<Product> products,
            List<Order> orders
    ) {
        LocalDateTime now = LocalDateTime.now();
        List<ReturnRequest> returnRequests = List.of(
                buildReturnRequest(orders.get(0), products.get(0), customers.get(0),
                        ReturnReason.SIZE_TOO_SMALL, ReturnStatus.REQUESTED, "Bangalore", 4999.0, now.minusDays(6)),
                buildReturnRequest(orders.get(1), products.get(1), customers.get(1),
                        ReturnReason.DAMAGED_ITEM, ReturnStatus.APPROVED, "Mumbai", 2499.0, now.minusDays(5)),
                buildReturnRequest(orders.get(1), products.get(3), customers.get(1),
                        ReturnReason.WRONG_PRODUCT, ReturnStatus.INSPECTED, "Mumbai", 999.0, now.minusDays(4)),
                buildReturnRequest(orders.get(2), products.get(1), customers.get(2),
                        ReturnReason.POOR_QUALITY, ReturnStatus.REFUNDED, "Delhi", 2499.0, now.minusDays(4)),
                buildReturnRequest(orders.get(3), products.get(2), customers.get(3),
                        ReturnReason.SIZE_TOO_LARGE, ReturnStatus.APPROVED, "Hyderabad", 3499.0, now.minusDays(3)),
                buildReturnRequest(orders.get(4), products.get(5), customers.get(4),
                        ReturnReason.NOT_AS_DESCRIBED, ReturnStatus.INSPECTED, "Bangalore", 5499.0, now.minusDays(2)),
                buildReturnRequest(orders.get(4), products.get(6), customers.get(4),
                        ReturnReason.SIZE_TOO_SMALL, ReturnStatus.REFUNDED, "Delhi", 2999.0, now.minusDays(1)),
                buildReturnRequest(orders.get(4), products.get(7), customers.get(4),
                        ReturnReason.POOR_QUALITY, ReturnStatus.REQUESTED, "Hyderabad", 1499.0, now.minusHours(6))
        );

        return returnRequestRepository.saveAll(returnRequests);
    }

    private ReturnRequest buildReturnRequest(
            Order order,
            Product product,
            Customer customer,
            ReturnReason returnReason,
            ReturnStatus returnStatus,
            String city,
            Double refundAmount,
            LocalDateTime requestedAt
    ) {
        return ReturnRequest.builder()
                .orderId(order.getOrderId())
                .productId(product.getProductId())
                .customerId(customer.getCustomerId())
                .returnReason(returnReason)
                .description("Seed return for " + product.getName())
                .quantity(1)
                .returnStatus(returnStatus)
                .requestedAt(requestedAt)
                .pickupDate(requestedAt.plusDays(1))
                .city(city)
                .refundAmount(refundAmount)
                .build();
    }

    private void seedReturnInspections(List<ReturnRequest> returnRequests) {
        LocalDateTime now = LocalDateTime.now();
        List<ReturnInspection> inspections = List.of(
                buildInspection(returnRequests.get(1), "WH-MUM-01", ProductCondition.DAMAGED,
                        true, null, "Nitin Mehra", now.minusDays(4)),
                buildInspection(returnRequests.get(2), "WH-MUM-01", ProductCondition.GOOD,
                        true, null, "Nitin Mehra", now.minusDays(3)),
                buildInspection(returnRequests.get(5), "WH-BLR-01", ProductCondition.FAIR,
                        true, null, "Kavya Nair", now.minusDays(1)),
                buildInspection(returnRequests.get(6), "WH-DEL-01", ProductCondition.GOOD,
                        true, null, "Rohit Bansal", now.minusHours(12))
        );

        returnInspectionRepository.saveAll(inspections);
    }

    private ReturnInspection buildInspection(
            ReturnRequest returnRequest,
            String warehouseId,
            ProductCondition productCondition,
            Boolean approved,
            String rejectionReason,
            String inspectorName,
            LocalDateTime inspectedAt
    ) {
        return ReturnInspection.builder()
                .returnRequest(returnRequest)
                .warehouseId(warehouseId)
                .productCondition(productCondition)
                .approved(approved)
                .rejectionReason(rejectionReason)
                .inspectedAt(inspectedAt)
                .inspectorName(inspectorName)
                .build();
    }
}
