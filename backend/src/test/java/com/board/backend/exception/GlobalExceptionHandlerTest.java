package com.board.backend.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GlobalExceptionHandlerTest.TestController.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void setUp() {
        // GlobalExceptionHandler는 자동으로 컨텍스트에 로드됨.
    }

    @Test
    public void testHandleCustomException() throws Exception {
        mockMvc.perform(get("/test/custom-exception"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Custom exception occurred"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }

    @Test
    public void testHandleGlobalException() throws Exception {
        mockMvc.perform(get("/test/global-exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Internal Server Error"))
                .andExpect(jsonPath("$.status").value("INTERNAL_SERVER_ERROR"));
    }

    @RestController
    @RequestMapping("/test")
    static class TestController {

        @GetMapping("/custom-exception")
        public void throwCustomException() {
            throw new CustomerException("Custom exception occurred");
        }

        @GetMapping("/global-exception")
        public void throwGlobalException() {
            throw new RuntimeException("Unexpected error");
        }
    }
}