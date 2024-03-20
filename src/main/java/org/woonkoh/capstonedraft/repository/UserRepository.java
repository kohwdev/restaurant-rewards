package org.woonkoh.capstonedraft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woonkoh.capstonedraft.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

