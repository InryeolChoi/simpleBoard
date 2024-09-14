package com.board.backend.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerExceptionTest {

    @Test
    public void testCustomerExceptionMessage() {
        // Given
        String errorMessage = "Custom error occurred";

        // When
        CustomerException exception = new CustomerException(errorMessage);

        // Then
        assertEquals(errorMessage, exception.getMessage());
    }
}