package org.woonkoh.restaurantrewards.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.woonkoh.restaurantrewards.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    public Customer findCustomerByEmail(String email);
    public Customer findCustomerById(Long id);
}
