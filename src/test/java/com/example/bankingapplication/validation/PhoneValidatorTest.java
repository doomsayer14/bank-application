package com.example.bankingapplication.validation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PhoneValidatorTest {

    private PhoneValidator phoneValidator; // Class under test

    @BeforeEach
    void setUp() {
        phoneValidator = new PhoneValidator(); // Initialize PhoneValidator
    }

    @Test
    void testValidPhoneNumbers() {
        // Arrange: Set up valid phone numbers
        String[] validPhoneNumbers = {
                "123-456-7890",
                "(123) 456-7890",
                "123 456 7890",
                "+1 123 456 7890",
                "1234567890"
        };

        // Act & Assert: Validate each phone number and assert that it's valid
        for (String phoneNumber : validPhoneNumbers) {
            assertTrue(phoneValidator.isValid(phoneNumber, null),
                    "Expected valid phone number: " + phoneNumber);
        }
    }

    @Test
    void testInvalidPhoneNumbers() {
        // Arrange: Set up invalid phone numbers
        String[] invalidPhoneNumbers = {
                "123-45-67890",   // Too short
                "+123 456 789012345",  // Too long
                "(12) 345-67890",  // Invalid area code length
                "abc-def-ghij",    // Letters instead of numbers
                "++1234567890",    // Double plus sign
                "123.456.78901"    // Too long with extra numbers
        };

        // Act & Assert: Validate each phone number and assert that it's invalid
        for (String phoneNumber : invalidPhoneNumbers) {
            assertFalse(phoneValidator.isValid(phoneNumber, null),
                    "Expected invalid phone number: " + phoneNumber);
        }
    }
}

