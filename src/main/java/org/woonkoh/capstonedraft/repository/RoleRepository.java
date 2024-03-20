package org.woonkoh.capstonedraft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woonkoh.capstonedraft.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
