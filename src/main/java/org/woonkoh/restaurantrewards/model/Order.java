package org.woonkoh.restaurantrewards.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private Date orderDate;
    private double totalPrice;

    public Order(Date orderDate, double totalPrice) {
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }
}
