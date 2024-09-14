package com.board.backend.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPublicEndpoints() throws Exception {
        // 회원가입 및 로그인 페이지는 인증 없이 접근 가능해야 함
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/login"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testSecuredEndpointsWithAuthentication() throws Exception {
        // 인증된 사용자만 접근 가능해야 함
        mockMvc.perform(get("/secured-endpoint"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSecuredEndpointsWithoutAuthentication() throws Exception {
        // 인증되지 않은 사용자는 접근 불가
        mockMvc.perform(get("/secured-endpoint"))
                .andExpect(status().isForbidden());
    }
}