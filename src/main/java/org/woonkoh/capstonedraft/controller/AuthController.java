package org.woonkoh.capstonedraft.controller;

import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.woonkoh.capstonedraft.model.User;
import org.woonkoh.capstonedraft.model.UserDto;
import org.woonkoh.capstonedraft.repository.UserRepository;
import org.woonkoh.capstonedraft.service.UserService;
import java.util.List;

@Controller
public class AuthController {

    private UserRepository userRepository;
    private UserService userService;

    public AuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("index")
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }

    // handler method to handle user registration request
    @GetMapping("register")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle register user form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model){
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/register?success";
    }


    //Get all registered users from database
    @GetMapping("/users")
    public String listRegisteredUsers(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }


    //Delete user account
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);

            redirectAttributes.addFlashAttribute("message", "User with id=" + id + " has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/users";
    }


    //display edit page to edit user details
    @GetMapping("/editUser/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        UserDto userDto = convertToDto(user);
        model.addAttribute("userDto", userDto);

        return "editUser";
    }

    private UserDto convertToDto(User user) {
        //convert user to userDto, for the name since first and last name needs to be separated
        UserDto userDto = new UserDto();
        String[] nameParts = user.getName().split(" ", 2);
        userDto.setFirstName(nameParts[0]);
        if (nameParts.length > 1) {
            userDto.setLastName(nameParts[1]);
        } else {
            userDto.setLastName("");
        }
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());

        return userDto;
    }



    //update user details
    @PostMapping("/updateUser")
    public String updateUserDetails(@ModelAttribute("userDto") UserDto userDto, RedirectAttributes redirectAttributes) {
        User user = convertToEntity(userDto);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
        return "redirect:/users"; // Or wherever you'd like to redirect after updating
    }
    //after the update is made, convert dto back to entity
    private User convertToEntity(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userDto.getId()));
        // Update user details based on dto
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        // Handle other fields and possibly encode the password if it's being changed
        return user;
    }




}