package org.woonkoh.restaurantrewards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.woonkoh.restaurantrewards.repository.CustomerRepository;

@SpringBootApplication
public class RestaurantRewardsApplication {

    @Autowired
    CustomerRepository customerRepository;

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(RestaurantRewardsApplication.class, args);
    }

}
