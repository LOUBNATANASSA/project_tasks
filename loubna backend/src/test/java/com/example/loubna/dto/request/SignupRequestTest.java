package com.example.loubna.dto.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SignupRequest DTO Tests")
class SignupRequestTest {

    private SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequest();
    }

    @Test
    @DisplayName("Should create SignupRequest with default null values")
    void testDefaultConstructor() {
        assertNull(signupRequest.getName());
        assertNull(signupRequest.getEmail());
        assertNull(signupRequest.getPassword());
    }

    @Test
    @DisplayName("Should set and get name correctly")
    void testNameGetterSetter() {
        String expectedName = "John Doe";
        signupRequest.setName(expectedName);
        assertEquals(expectedName, signupRequest.getName());
    }

    @Test
    @DisplayName("Should set and get email correctly")
    void testEmailGetterSetter() {
        String expectedEmail = "john.doe@example.com";
        signupRequest.setEmail(expectedEmail);
        assertEquals(expectedEmail, signupRequest.getEmail());
    }

    @Test
    @DisplayName("Should set and get password correctly")
    void testPasswordGetterSetter() {
        String expectedPassword = "SecureP@ssword123";
        signupRequest.setPassword(expectedPassword);
        assertEquals(expectedPassword, signupRequest.getPassword());
    }

    @Test
    @DisplayName("Should handle null values in all fields")
    void testNullValues() {
        signupRequest.setName("Name");
        signupRequest.setEmail("email@test.com");
        signupRequest.setPassword("password");

        signupRequest.setName(null);
        signupRequest.setEmail(null);
        signupRequest.setPassword(null);

        assertNull(signupRequest.getName());
        assertNull(signupRequest.getEmail());
        assertNull(signupRequest.getPassword());
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyStrings() {
        signupRequest.setName("");
        signupRequest.setEmail("");
        signupRequest.setPassword("");

        assertEquals("", signupRequest.getName());
        assertEquals("", signupRequest.getEmail());
        assertEquals("", signupRequest.getPassword());
    }

    @Test
    @DisplayName("Should set all fields together")
    void testAllFieldsTogether() {
        String name = "Jane Smith";
        String email = "jane.smith@example.com";
        String password = "MyP@ssword456";

        signupRequest.setName(name);
        signupRequest.setEmail(email);
        signupRequest.setPassword(password);

        assertEquals(name, signupRequest.getName());
        assertEquals(email, signupRequest.getEmail());
        assertEquals(password, signupRequest.getPassword());
    }

    @Test
    @DisplayName("Should handle name with accents and special characters")
    void testNameWithAccents() {
        String name = "José García-López";
        signupRequest.setName(name);
        assertEquals(name, signupRequest.getName());
    }

    @Test
    @DisplayName("Should handle international email formats")
    void testInternationalEmail() {
        String email = "用户@例子.广告";
        signupRequest.setEmail(email);
        assertEquals(email, signupRequest.getEmail());
    }

    @Test
    @DisplayName("Should handle password with unicode characters")
    void testUnicodePassword() {
        String password = "пароль密码🔒";
        signupRequest.setPassword(password);
        assertEquals(password, signupRequest.getPassword());
    }

    @Test
    @DisplayName("Should handle very long name")
    void testVeryLongName() {
        String longName = "A".repeat(255);
        signupRequest.setName(longName);
        assertEquals(255, signupRequest.getName().length());
    }

    @Test
    @DisplayName("Should handle very long email")
    void testVeryLongEmail() {
        String longEmail = "a".repeat(64) + "@" + "b".repeat(63) + ".com";
        signupRequest.setEmail(longEmail);
        assertEquals(longEmail, signupRequest.getEmail());
    }

    @Test
    @DisplayName("Should handle password with special characters")
    void testPasswordWithSpecialCharacters() {
        String password = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
        signupRequest.setPassword(password);
        assertEquals(password, signupRequest.getPassword());
    }

    @Test
    @DisplayName("Should handle whitespace in name")
    void testWhitespaceName() {
        String name = "  John   Doe  ";
        signupRequest.setName(name);
        assertEquals(name, signupRequest.getName());
    }
}
