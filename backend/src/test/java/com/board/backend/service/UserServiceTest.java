package com.board.backend.service;

import com.board.backend.dto.UserDto;
import com.board.backend.entity.Role;
import com.board.backend.entity.User;
import com.board.backend.repository.RoleRepository;
import com.board.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegisterUser() {
        // UserDto 생성자: (Long id, String username, String email, String password)
        UserDto userDto = new UserDto(null, "testUser", "test@example.com", "password");

        Role role = new Role();
        role.setName("ROLE_USER");

        when(roleRepository.findByName(any())).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        userService.registerUser(userDto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testAuthenticateUser_Success() {
        // UserDto 생성자: (Long id, String username, String email, String password)
        UserDto userDto = new UserDto(null, "testUser", null, "password");

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        boolean result = userService.authenticateUser(userDto);
        assertTrue(result);
    }

    @Test
    public void testAuthenticateUser_Failure() {
        // UserDto 생성자: (Long id, String username, String email, String password)
        UserDto userDto = new UserDto(null, "wrongUser", null, "password");

        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        boolean result = userService.authenticateUser(userDto);
        assertFalse(result);
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1L);
        assertEquals("testUser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
    }
}