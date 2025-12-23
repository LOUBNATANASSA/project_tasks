package com.example.loubna.security.jwt;

import com.example.loubna.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtUtils Tests")
class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    private Authentication authentication;
    private UserDetailsImpl userDetails;

    // Base64 encoded secret (at least 32 bytes for HS256)
    private static final String TEST_SECRET = "dGVzdFNlY3JldEtleUZvclVuaXRUZXN0aW5nVGhhdElzVmVyeUxvbmdBbmRTZWN1cmU=";
    private static final int TEST_EXPIRATION_MS = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        // Set the private fields using reflection
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", TEST_SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", TEST_EXPIRATION_MS);

        userDetails = new UserDetailsImpl(1L, "test@example.com", "password");
        authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, Collections.emptyList()
        );
    }

    @Test
    @DisplayName("Should generate JWT token successfully")
    void testGenerateJwtToken() {
        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
    }

    @Test
    @DisplayName("Should extract username from valid JWT token")
    void testGetUserNameFromJwtToken() {
        String token = jwtUtils.generateJwtToken(authentication);

        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals("test@example.com", extractedUsername);
    }

    @Test
    @DisplayName("Should validate valid JWT token")
    void testValidateJwtTokenValid() {
        String token = jwtUtils.generateJwtToken(authentication);

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return false for invalid JWT token")
    void testValidateJwtTokenInvalid() {
        String invalidToken = "invalid.jwt.token";

        boolean isValid = jwtUtils.validateJwtToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false for malformed JWT token")
    void testValidateJwtTokenMalformed() {
        String malformedToken = "not-a-jwt";

        boolean isValid = jwtUtils.validateJwtToken(malformedToken);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false for empty JWT token")
    void testValidateJwtTokenEmpty() {
        boolean isValid = jwtUtils.validateJwtToken("");

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false for null JWT token")
    void testValidateJwtTokenNull() {
        boolean isValid = jwtUtils.validateJwtToken(null);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false for expired JWT token")
    void testValidateJwtTokenExpired() {
        // Create a JwtUtils with very short expiration
        JwtUtils shortExpirationJwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(shortExpirationJwtUtils, "jwtSecret", TEST_SECRET);
        ReflectionTestUtils.setField(shortExpirationJwtUtils, "jwtExpirationMs", 1); // 1ms expiration

        String token = shortExpirationJwtUtils.generateJwtToken(authentication);

        // Wait for token to expire
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean isValid = shortExpirationJwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should generate different tokens for different users")
    void testGenerateDifferentTokensForDifferentUsers() {
        UserDetailsImpl user1 = new UserDetailsImpl(1L, "user1@example.com", "password1");
        UserDetailsImpl user2 = new UserDetailsImpl(2L, "user2@example.com", "password2");

        Authentication auth1 = new UsernamePasswordAuthenticationToken(user1, null, Collections.emptyList());
        Authentication auth2 = new UsernamePasswordAuthenticationToken(user2, null, Collections.emptyList());

        String token1 = jwtUtils.generateJwtToken(auth1);
        String token2 = jwtUtils.generateJwtToken(auth2);

        assertNotEquals(token1, token2);
        assertEquals("user1@example.com", jwtUtils.getUserNameFromJwtToken(token1));
        assertEquals("user2@example.com", jwtUtils.getUserNameFromJwtToken(token2));
    }

    @Test
    @DisplayName("Should handle token with special characters in username")
    void testTokenWithSpecialCharsInUsername() {
        UserDetailsImpl specialUser = new UserDetailsImpl(1L, "user+tag@sub.domain.com", "password");
        Authentication specialAuth = new UsernamePasswordAuthenticationToken(
                specialUser, null, Collections.emptyList()
        );

        String token = jwtUtils.generateJwtToken(specialAuth);
        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals("user+tag@sub.domain.com", extractedUsername);
        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    @DisplayName("Should return false for token signed with different key")
    void testValidateTokenWithDifferentKey() {
        String token = jwtUtils.generateJwtToken(authentication);

        // Create another JwtUtils with different secret
        JwtUtils otherJwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(otherJwtUtils, "jwtSecret", 
                "b3RoZXJTZWNyZXRLZXlUaGF0SXNEaWZmZXJlbnRGcm9tVGhlT3JpZ2luYWxPbmU=");
        ReflectionTestUtils.setField(otherJwtUtils, "jwtExpirationMs", TEST_EXPIRATION_MS);

        boolean isValid = otherJwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }
}
