package org.woonkoh.restaurantrewards.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woonkoh.restaurantrewards.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
