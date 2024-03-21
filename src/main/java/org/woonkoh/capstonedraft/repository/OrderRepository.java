package org.woonkoh.capstonedraft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woonkoh.capstonedraft.model.Order;
import org.woonkoh.capstonedraft.model.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findFirstByUserIdAndStatus(Long userId, OrderStatus status);

}
