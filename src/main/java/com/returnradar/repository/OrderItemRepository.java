package com.returnradar.repository;

import com.returnradar.entity.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<OrderItem> findByOrder_OrderIdAndProduct_ProductId(Long orderId, Long productId);

    List<OrderItem> findByOrder_OrderId(Long orderId);
}
