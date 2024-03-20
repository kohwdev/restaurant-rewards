package org.woonkoh.capstonedraft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woonkoh.capstonedraft.model.Reward;

import java.util.List;

public interface RewardRepository extends JpaRepository<Reward, Long> {
}