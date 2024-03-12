package org.woonkoh.restaurantrewards.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "rewards")
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private Date rewardDate;
    private double rewardAmount;
    private boolean redeemed;

    public Reward(Customer customer, Date rewardDate, double rewardAmount, boolean redeemed) {
        this.customer = customer;
        this.rewardDate = rewardDate;
        this.rewardAmount = rewardAmount;
        this.redeemed = redeemed;
    }
}
