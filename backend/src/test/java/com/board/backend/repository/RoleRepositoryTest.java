package com.board.backend.repository;

import com.board.backend.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByName() {
        // Given
        Role role = new Role();
        role.setName("ROLE_USER");
        roleRepository.save(role);

        // When
        Optional<Role> foundRole = roleRepository.findByName("ROLE_USER");

        // Then
        assertTrue(foundRole.isPresent());
        assertEquals("ROLE_USER", foundRole.get().getName());
    }
}