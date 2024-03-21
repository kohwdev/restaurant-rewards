package org.woonkoh.capstonedraft;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.woonkoh.capstonedraft.model.Reward;
import org.woonkoh.capstonedraft.model.Role;
import org.woonkoh.capstonedraft.model.User;
import org.woonkoh.capstonedraft.model.UserDto;
import org.woonkoh.capstonedraft.repository.RoleRepository;
import org.woonkoh.capstonedraft.repository.UserRepository;
import org.woonkoh.capstonedraft.service.UserServiceImpl;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @InjectMocks // Automatically injects the mocks above into this service
    private UserServiceImpl userService;

    @Test
    public void testSaveUser() {
        // Given
        UserDto userDto = new UserDto();
        userDto.setFirstName("Woon");
        userDto.setLastName("Koh");
        userDto.setEmail("test.test@example.com");
        userDto.setPassword("password");

        Role roleAdmin = new Role();
        roleAdmin.setName("ROLE_ADMIN");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(roleAdmin);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        userService.saveUser(userDto);

        // Then
        verify(passwordEncoder, times(1)).encode("password");
        verify(roleRepository, times(1)).findByName("ROLE_ADMIN");
        verify(userRepository, times(1)).save(any(User.class));
    }


}


