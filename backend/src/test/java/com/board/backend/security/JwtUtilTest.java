package com.board.backend.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    @BeforeEach
    public void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    public void testGenerateToken() {
        // Given
        String username = "testUser";

        // When
        String token = jwtUtil.generateToken(username);

        // Then
        assertNotNull(token);
    }

    @Test
    public void testExtractUsername() {
        // Given
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        // When
        String extractedUsername = jwtUtil.extractUsername(token);

        // Then
        assertEquals(username, extractedUsername);
    }

    @Test
    public void testIsTokenExpired() {
        // Given
        String token = jwtUtil.generateToken("testUser");

        // When
        boolean isExpired = jwtUtil.isTokenExpired(token);

        // Then
        assertFalse(isExpired);
    }

    @Test
    public void testValidateToken() {
        // Given
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        // When
        boolean isValid = jwtUtil.validateToken(token, username);

        // Then
        assertTrue(isValid);
    }

    @Test
    public void testExtractClaims() {
        // Given
        String token = jwtUtil.generateToken("testUser");

        // When
        Claims claims = jwtUtil.extractClaims(token);

        // Then
        assertNotNull(claims);
        assertEquals("testUser", claims.getSubject());
    }
}