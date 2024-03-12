package org.woonkoh.restaurantrewards.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.woonkoh.restaurantrewards.model.Customer;
import org.woonkoh.restaurantrewards.repository.CustomerRepository;
import org.woonkoh.restaurantrewards.service.CustomerServiceImpl;


@Controller
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final CustomerServiceImpl customerServiceImpl;

    public CustomerController(CustomerRepository customerRepository, CustomerServiceImpl customerServiceImpl) {
        this.customerRepository = customerRepository;
        this.customerServiceImpl = customerServiceImpl;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("customer", customerRepository.findAll());
        return "home";
    }


    @GetMapping("/sign-up")
    public String showSignUpForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "sign-up";
    }

    @PostMapping("/add-customer")
    public String addCustomer(@Valid Customer customer, BindingResult result) {
        if (result.hasErrors()) {
            return "sign-up";
        }
        customerServiceImpl.registerCustomer(customer);
        return "confirmation";
    }
}