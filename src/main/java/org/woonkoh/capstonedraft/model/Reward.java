package org.woonkoh.capstonedraft.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "rewards")
public class Reward {
    public enum RewardStatus {
        AVAILABLE,
        NOT_AVAILABLE,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int pointsRequired = 100;

    @Enumerated(EnumType.STRING)
    private RewardStatus status;

}

