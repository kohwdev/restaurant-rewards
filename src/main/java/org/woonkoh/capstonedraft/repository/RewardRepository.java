package org.woonkoh.capstonedraft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woonkoh.capstonedraft.model.Reward;

public interface RewardRepository extends JpaRepository<Reward, Long> {
}