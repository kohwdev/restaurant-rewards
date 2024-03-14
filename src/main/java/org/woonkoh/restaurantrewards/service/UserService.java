package org.woonkoh.restaurantrewards.service;

import org.woonkoh.restaurantrewards.model.User;
import org.woonkoh.restaurantrewards.model.UserDto;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();
}
