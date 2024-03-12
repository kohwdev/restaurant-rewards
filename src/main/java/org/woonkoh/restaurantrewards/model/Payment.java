package org.woonkoh.restaurantrewards.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date paymentDate;
    private String paymentType;
    private double paymentAmount;

    public Payment(Date paymentDate, String paymentType, double paymentAmount) {
        this.paymentDate = paymentDate;
        this.paymentType = paymentType;
        this.paymentAmount = paymentAmount;
    }

}