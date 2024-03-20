package org.woonkoh.capstonedraft.model;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RewardDto {
    private Long id;
    private int pointsRequired;
    private Reward.RewardStatus status; // Use your existing RewardStatus enum
}
