package com.board.backend.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorResponseTest {

    @Test
    public void testErrorResponse() {
        // Given
        String errorMessage = "Custom error occurred";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // When
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, status);

        // Then
        assertEquals(errorMessage, errorResponse.getMessage());
        assertEquals(status, errorResponse.getStatus());
    }
}