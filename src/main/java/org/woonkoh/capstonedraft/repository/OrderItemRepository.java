package org.woonkoh.capstonedraft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woonkoh.capstonedraft.model.OrderItem;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByProductId(Long productId);
}
