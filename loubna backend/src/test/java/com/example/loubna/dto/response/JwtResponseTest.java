package com.example.loubna.dto.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtResponse DTO Tests")
class JwtResponseTest {

    @Test
    @DisplayName("Should create JwtResponse with constructor and default Bearer type")
    void testConstructor() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test";
        Long id = 1L;
        String name = "John Doe";
        String email = "john@example.com";

        JwtResponse response = new JwtResponse(token, id, name, email);

        assertEquals(token, response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals(id, response.getId());
        assertEquals(name, response.getName());
        assertEquals(email, response.getEmail());
    }

    @Test
    @DisplayName("Should set and get token correctly")
    void testTokenGetterSetter() {
        JwtResponse response = new JwtResponse("initial", 1L, "name", "email@test.com");
        String newToken = "newToken123";
        
        response.setToken(newToken);
        
        assertEquals(newToken, response.getToken());
    }

    @Test
    @DisplayName("Should set and get type correctly")
    void testTypeGetterSetter() {
        JwtResponse response = new JwtResponse("token", 1L, "name", "email@test.com");
        String newType = "CustomType";
        
        response.setType(newType);
        
        assertEquals(newType, response.getType());
    }

    @Test
    @DisplayName("Should set and get id correctly")
    void testIdGetterSetter() {
        JwtResponse response = new JwtResponse("token", 1L, "name", "email@test.com");
        Long newId = 999L;
        
        response.setId(newId);
        
        assertEquals(newId, response.getId());
    }

    @Test
    @DisplayName("Should set and get name correctly")
    void testNameGetterSetter() {
        JwtResponse response = new JwtResponse("token", 1L, "name", "email@test.com");
        String newName = "Jane Smith";
        
        response.setName(newName);
        
        assertEquals(newName, response.getName());
    }

    @Test
    @DisplayName("Should set and get email correctly")
    void testEmailGetterSetter() {
        JwtResponse response = new JwtResponse("token", 1L, "name", "email@test.com");
        String newEmail = "jane@newdomain.com";
        
        response.setEmail(newEmail);
        
        assertEquals(newEmail, response.getEmail());
    }

    @Test
    @DisplayName("Should have Bearer as default type")
    void testDefaultBearerType() {
        JwtResponse response = new JwtResponse("token", 1L, "name", "email");
        assertEquals("Bearer", response.getType());
    }

    @Test
    @DisplayName("Should handle null values in constructor")
    void testNullValuesInConstructor() {
        JwtResponse response = new JwtResponse(null, null, null, null);

        assertNull(response.getToken());
        assertNull(response.getId());
        assertNull(response.getName());
        assertNull(response.getEmail());
        assertEquals("Bearer", response.getType()); // Type still has default
    }

    @Test
    @DisplayName("Should handle null values in setters")
    void testNullValuesInSetters() {
        JwtResponse response = new JwtResponse("token", 1L, "name", "email");

        response.setToken(null);
        response.setType(null);
        response.setId(null);
        response.setName(null);
        response.setEmail(null);

        assertNull(response.getToken());
        assertNull(response.getType());
        assertNull(response.getId());
        assertNull(response.getName());
        assertNull(response.getEmail());
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyStrings() {
        JwtResponse response = new JwtResponse("", 1L, "", "");

        assertEquals("", response.getToken());
        assertEquals("", response.getName());
        assertEquals("", response.getEmail());
    }

    @Test
    @DisplayName("Should handle special characters in token")
    void testSpecialCharactersInToken() {
        String complexToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        JwtResponse response = new JwtResponse(complexToken, 1L, "name", "email");
        
        assertEquals(complexToken, response.getToken());
    }

    @Test
    @DisplayName("Should handle large id values")
    void testLargeIdValues() {
        Long largeId = Long.MAX_VALUE;
        JwtResponse response = new JwtResponse("token", largeId, "name", "email");
        
        assertEquals(largeId, response.getId());
    }

    @Test
    @DisplayName("Should handle zero id")
    void testZeroId() {
        JwtResponse response = new JwtResponse("token", 0L, "name", "email");
        assertEquals(0L, response.getId());
    }
}
