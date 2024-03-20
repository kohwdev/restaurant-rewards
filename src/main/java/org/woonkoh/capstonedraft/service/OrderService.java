package org.woonkoh.capstonedraft.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.woonkoh.capstonedraft.model.*;
import org.woonkoh.capstonedraft.repository.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductsRepository productRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final RewardRepository rewardRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductsRepository productRepository, UserRepository userRepository, OrderItemRepository orderItemRepository, RewardRepository rewardRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        this.rewardRepository = rewardRepository;
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }



    @Transactional
    public Order addProductToOrderItem(Long userId, Long productId, int quantity) {
        // Find or create an active order for the user
        Order order = findOrCreateActiveOrder(userId);

        // Find the product that we are adding to the order
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + productId + " not found"));

        // Create an order item for the product and quantity
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(product.getPrice());
        orderItem.setOrder(order); // Associate the order item with the order

        // Save the order item in the database
        orderItemRepository.save(orderItem);

        // Add the order item to the order's list of items
        order.addOrderItem(orderItem);

        order.setOrderDate(LocalDateTime.now());
        double totalAmount = order.getOrderItems().stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();

        order.setTotalAmount(BigDecimal.valueOf(totalAmount));
        order.setStatus(OrderStatus.NEW);

        orderRepository.save(order); // Save the order with the new item

        return order;
    }

    @Transactional
    public Order findOrCreateActiveOrder(Long userId) {
        // Define the status that you will check for an active order, for example 'IN_CART'
        final OrderStatus activeStatus = OrderStatus.NEW;

        // Check for an existing active order for the user
        Order activeOrder = orderRepository.findFirstByUserIdAndStatus(userId, activeStatus);

        // If there's no active order, create a new one
        if (activeOrder == null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));

            activeOrder = new Order();
            // Set fields on the new order, for example, user and status
            activeOrder.setUser(user);
            activeOrder.setStatus(activeStatus);
            // Save the new order to the database
            activeOrder = orderRepository.save(activeOrder);
        }

        return activeOrder;
    }




    @Transactional
    public Order checkoutOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new IllegalStateException("Order is already completed.");
        }

        order.setStatus(OrderStatus.COMPLETED);

        // Calculate reward points (1 point per dollar, rounded down)
        int rewardPoints = (int) Math.floor(order.getTotalAmount().doubleValue());

        // Fetch the user and update their points
        User user = order.getUser();
        user.setPoints(user.getPoints() + rewardPoints);

        checkAndUpdateUserRewards(user);

        // Save the updates
        userRepository.save(user);
        orderRepository.save(order);

        return order;
    }

    private void checkAndUpdateUserRewards(User user) {
        // Assuming 100 points are required for a reward to become available
        if (user.getPoints() >= 100) {
            // Implement the logic to update rewards
            // For example, mark an existing reward as AVAILABLE or create a new one
            updateRewardStatusToAvailable(user);
        }
    }

    private void updateRewardStatusToAvailable(User user) {
        // This method should encapsulate the logic for marking rewards as available
        // It could check for an existing reward to update, or create a new Reward entity with status AVAILABLE
        Reward reward = new Reward();
        reward.setStatus(Reward.RewardStatus.AVAILABLE);
        // Assuming you're setting additional properties of the reward based on your application needs
        reward = rewardRepository.save(reward);
        user.getRewards().add(reward);
        // No need to explicitly save the user if you're in a transactional context and using cascading properly
    }

    @Transactional
    public void applyRewardToOrder(Long userId, Long orderId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() == OrderStatus.COMPLETED || user.getPoints() < 100) {
            throw new IllegalStateException("Cannot apply reward.");
        }

        user.setPoints(user.getPoints() - 100); // Deduct points
        order.setTotalAmount(order.getTotalAmount().subtract(BigDecimal.valueOf(10))); // Apply discount
        order.setRewardUsed(true);

        updateRewardsBasedOnPoints(user);

        userRepository.save(user);
        orderRepository.save(order);
    }

    private void updateRewardsBasedOnPoints(User user) {
        // If points are below 100, find and update any available rewards to reflect the new status
        if (user.getPoints() < 100) {
            user.getRewards().forEach(reward -> {
                if (reward.getStatus() == Reward.RewardStatus.AVAILABLE) {
                    // Assuming you have logic to set rewards as not available or redeemed
                    reward.setStatus(Reward.RewardStatus.NOT_AVAILABLE); // Example status update
                    rewardRepository.save(reward);
                }
            });
        }
    }

    @Transactional
    public void deleteProduct(Long id) {
        List<OrderItem> orderItems = orderItemRepository.findAllByProductId(id);
        for (OrderItem orderItem : orderItems) {
            // Assuming you have a setProduct method and that you're okay with having order items with no product.
            orderItem.setProduct(null);
            orderItemRepository.save(orderItem);
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        // Attempt to delete product image
        Path imagePath = Paths.get("public/images/" + product.getImageFileName());
        try {
            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            // Handle the case where the image file couldn't be deleted
            e.printStackTrace(); // Consider a more robust way to log or handle the error
        }

        // Delete the product from the database
        productRepository.delete(product);
    }

}