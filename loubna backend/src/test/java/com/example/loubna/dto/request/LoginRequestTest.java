package com.example.loubna.dto.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LoginRequest DTO Tests")
class LoginRequestTest {

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
    }

    @Test
    @DisplayName("Should create LoginRequest with default null values")
    void testDefaultConstructor() {
        assertNull(loginRequest.getEmail());
        assertNull(loginRequest.getPassword());
    }

    @Test
    @DisplayName("Should set and get email correctly")
    void testEmailGetterSetter() {
        String expectedEmail = "user@example.com";
        loginRequest.setEmail(expectedEmail);
        assertEquals(expectedEmail, loginRequest.getEmail());
    }

    @Test
    @DisplayName("Should set and get password correctly")
    void testPasswordGetterSetter() {
        String expectedPassword = "securePassword123";
        loginRequest.setPassword(expectedPassword);
        assertEquals(expectedPassword, loginRequest.getPassword());
    }

    @Test
    @DisplayName("Should handle null email")
    void testNullEmail() {
        loginRequest.setEmail("test@test.com");
        loginRequest.setEmail(null);
        assertNull(loginRequest.getEmail());
    }

    @Test
    @DisplayName("Should handle null password")
    void testNullPassword() {
        loginRequest.setPassword("password");
        loginRequest.setPassword(null);
        assertNull(loginRequest.getPassword());
    }

    @Test
    @DisplayName("Should handle empty email")
    void testEmptyEmail() {
        loginRequest.setEmail("");
        assertEquals("", loginRequest.getEmail());
    }

    @Test
    @DisplayName("Should handle empty password")
    void testEmptyPassword() {
        loginRequest.setPassword("");
        assertEquals("", loginRequest.getPassword());
    }

    @Test
    @DisplayName("Should handle email with special characters")
    void testEmailWithSpecialCharacters() {
        String email = "user+tag@sub.domain.example.com";
        loginRequest.setEmail(email);
        assertEquals(email, loginRequest.getEmail());
    }

    @Test
    @DisplayName("Should handle password with special characters")
    void testPasswordWithSpecialCharacters() {
        String password = "P@ssw0rd!#$%^&*()";
        loginRequest.setPassword(password);
        assertEquals(password, loginRequest.getPassword());
    }

    @Test
    @DisplayName("Should set both fields together")
    void testBothFieldsTogether() {
        String email = "john.doe@example.com";
        String password = "MySecureP@ss123";

        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        assertEquals(email, loginRequest.getEmail());
        assertEquals(password, loginRequest.getPassword());
    }

    @Test
    @DisplayName("Should handle unicode characters in password")
    void testUnicodePassword() {
        String unicodePassword = "пароль密码🔐";
        loginRequest.setPassword(unicodePassword);
        assertEquals(unicodePassword, loginRequest.getPassword());
    }

    @Test
    @DisplayName("Should handle whitespace in credentials")
    void testWhitespaceCredentials() {
        String emailWithSpaces = " user@example.com ";
        String passwordWithSpaces = " pass word ";

        loginRequest.setEmail(emailWithSpaces);
        loginRequest.setPassword(passwordWithSpaces);

        assertEquals(emailWithSpaces, loginRequest.getEmail());
        assertEquals(passwordWithSpaces, loginRequest.getPassword());
    }
}
