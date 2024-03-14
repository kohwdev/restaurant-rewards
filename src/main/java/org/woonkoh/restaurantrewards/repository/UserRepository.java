package org.woonkoh.restaurantrewards.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woonkoh.restaurantrewards.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

