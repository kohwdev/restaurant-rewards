package org.woonkoh.restaurantrewards.service;

import org.woonkoh.restaurantrewards.model.Customer;

import java.util.List;

public interface CustomerService {
    public void registerCustomer(Customer customer);
    public Customer getCustomer(Long customerId);
    public List<Customer> getCustomers();
}
