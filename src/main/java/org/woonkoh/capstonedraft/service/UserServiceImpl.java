package org.woonkoh.capstonedraft.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.woonkoh.capstonedraft.model.*;
import org.woonkoh.capstonedraft.repository.RewardRepository;
import org.woonkoh.capstonedraft.repository.RoleRepository;
import org.woonkoh.capstonedraft.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final RewardRepository rewardRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, RewardRepository rewardRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.rewardRepository = rewardRepository;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());

        //encrypt the password once we integrate spring security
        //user.setPassword(userDto.getPassword());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role role = roleRepository.findByName("ROLE_ADMIN");
        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        // Attempt to find the user by ID
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Remove the user's association with roles
            user.getRoles().clear();
            user.getRewards().clear();
            user.getOrders().clear();
            userRepository.saveAndFlush(user); // Sync changes

            // Delete the user
            userRepository.delete(user);
        } else {
            throw new NoSuchElementException("No user found with id " + id);
        }
    }


    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }

    private UserDto convertEntityToDto(User user){
        UserDto userDto = new UserDto();
        String[] name = user.getName().split(" ");
        userDto.setFirstName(name[0]);
        userDto.setLastName(name[1]);
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        userDto.setPoints(user.getPoints());

        String rewardStatus = user.getRewards().stream()
                .anyMatch(reward -> reward.getStatus() == Reward.RewardStatus.AVAILABLE)
                ? "Available" : "Not Available";
        userDto.setRewardStatus(rewardStatus);
        return userDto;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }
}