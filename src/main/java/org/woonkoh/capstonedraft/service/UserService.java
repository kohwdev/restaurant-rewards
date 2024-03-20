package org.woonkoh.capstonedraft.service;

import org.woonkoh.capstonedraft.model.User;
import org.woonkoh.capstonedraft.model.UserDto;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    List<UserDto> findAllUsers();

    User findByEmail(String email);

    void deleteUser(Long id);
}
