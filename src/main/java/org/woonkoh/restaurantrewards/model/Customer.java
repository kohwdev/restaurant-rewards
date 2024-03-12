package org.woonkoh.restaurantrewards.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phone;
    private String firstName;
    private String lastName;
    private String email;
    private int points;
    private double totalSpend;
    private String userName;
    private String password;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Collection<Order> orders = new ArrayList<>();

    public Customer(String firstName, String lastName, String phone, String email, int points, double totalSpend, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.points = points;
        this.totalSpend = totalSpend;
        this.userName = userName;
        this.password = password;
    }


}
