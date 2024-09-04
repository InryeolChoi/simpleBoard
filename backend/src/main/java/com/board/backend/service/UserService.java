package com.board.backend.service;

import com.board.backend.dto.UserDto;
import com.board.backend.entity.Role;
import com.board.backend.entity.User;
import com.board.backend.repository.RoleRepository;
import com.board.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 사용자 등록 메서드
    public void registerUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());

        // 기본 Role 설정
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_USER");
                    return roleRepository.save(newRole);
                });
        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);
    }

    // 사용자 인증 메서드
    public boolean authenticateUser(UserDto userDto) {
        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return passwordEncoder.matches(userDto.getPassword(), user.getPassword());
        }
        return false;
    }

    // 사용자 ID로 사용자 정보 조회 메서드
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), null); // 비밀번호는 null로 설정
    }

    // 이미 있는 메서드들
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}