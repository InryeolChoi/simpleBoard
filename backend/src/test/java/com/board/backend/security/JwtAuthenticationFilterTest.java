package com.board.backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthenticationFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoFilterInternalWithValidToken() throws Exception {
        // Given
        String token = "validToken";
        String username = "testUser";

        when(jwtUtil.extractUsername(token)).thenReturn(username);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtil.validateToken(token, username)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/secured-endpoint")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        verify(jwtUtil, times(1)).extractUsername(token);
        verify(jwtUtil, times(1)).validateToken(token, username);
        verify(userDetailsService, times(1)).loadUserByUsername(username);
    }

    @Test
    public void testDoFilterInternalWithInvalidToken() throws Exception {
        // Given
        String token = "invalidToken";
        when(jwtUtil.extractUsername(token)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/secured-endpoint")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());

        verify(jwtUtil, times(1)).extractUsername(token);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }
}