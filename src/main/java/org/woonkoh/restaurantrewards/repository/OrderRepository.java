package org.woonkoh.restaurantrewards.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woonkoh.restaurantrewards.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    public Order findOrderById(Long id);
}
