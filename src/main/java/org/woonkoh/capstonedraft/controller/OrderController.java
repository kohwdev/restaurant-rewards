package org.woonkoh.capstonedraft.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import org.woonkoh.capstonedraft.model.Order;
import org.woonkoh.capstonedraft.model.User;
import org.woonkoh.capstonedraft.service.OrderService;
import org.woonkoh.capstonedraft.service.UserServiceImpl;

import java.security.Principal;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final UserServiceImpl userServiceImpl;

    public OrderController(OrderService orderService, UserServiceImpl userServiceImpl) {
        this.orderService = orderService;
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/addProduct")
    public String addProductToOrder(
            @RequestParam("productId") Long productId,
            @RequestParam("quantity") int quantity,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (authentication == null) {
            redirectAttributes.addFlashAttribute("error", "you must be logged in");
            return "redirect:/login";
        }

        String email = authentication.getName();
        User user = userServiceImpl.findByEmail(email);

        try {
            Order order = orderService.addProductToOrderItem(user.getId(), productId, quantity);
            redirectAttributes.addFlashAttribute("success", "Product added to order successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "There was an error adding the product to the order: " + e.getMessage());
        }

        // Redirect to a confirmation page or back to the product listing
        return "redirect:/products";
    }


    @GetMapping("/orders/{orderId}")
    public String getOrderDetails(@PathVariable Long orderId, Model model, Principal principal, RedirectAttributes redirectAttributes) {


        String email = principal.getName();
        User user = userServiceImpl.findByEmail(email);

        Order order = orderService.getOrderById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        // Check if the order has items
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            // No items in the order, redirect to a suitable page with a message
            redirectAttributes.addFlashAttribute("emptyCartMessage", "Your cart is empty.");
            return "redirect:/products"; // Redirect to a page where items can be added
        }

        model.addAttribute("order", order);
        model.addAttribute("user", user);

        return "orderDetails";
    }

    @PostMapping("/orders/{orderId}/useReward")
    public String useReward(@PathVariable Long orderId, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            String email = principal.getName();
            User user = userServiceImpl.findByEmail(email);
            orderService.applyRewardToOrder(user.getId(), orderId);
            redirectAttributes.addFlashAttribute("success", "Reward applied successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error applying reward: " + e.getMessage());
        }
        return "redirect:/orders/" + orderId; // Adjust the redirect as needed
    }




    @PostMapping("/orders/{orderId}/checkout")
    public String checkoutOrder(@PathVariable Long orderId, RedirectAttributes redirectAttributes) {
        try {
           Order order = orderService.checkoutOrder(orderId);
            redirectAttributes.addFlashAttribute("orderId", order.getId());
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Order not found.");
            return "redirect:/orderDetails";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/orders/" + orderId;
        }

        return "redirect:/orders/completed";
    }

    @GetMapping("/orders/completed")
    public String showOrderSummary(@ModelAttribute("orderId") Long orderId, Model model, Principal principal) {
        if (orderId == null) {
            return "redirect:/products"; // or some error page if orderId is missing
        }

        Order order = orderService.getOrderById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
        String email = principal.getName();
        User user = userServiceImpl.findByEmail(email);

        model.addAttribute("user", user);

        model.addAttribute("order", order);

        return "completed";
    }




}
