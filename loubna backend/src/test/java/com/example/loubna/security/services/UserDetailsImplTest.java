package com.example.loubna.security.services;

import com.example.loubna.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserDetailsImpl Tests")
class UserDetailsImplTest {

    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        userDetails = new UserDetailsImpl(1L, "test@example.com", "hashedPassword");
    }

    @Test
    @DisplayName("Should create UserDetailsImpl with constructor")
    void testConstructor() {
        assertEquals(1L, userDetails.getId());
        assertEquals("test@example.com", userDetails.getEmail());
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("hashedPassword", userDetails.getPassword());
    }

    @Test
    @DisplayName("Should build UserDetailsImpl from User entity")
    void testBuildFromUser() {
        User user = new User("Test User", "user@example.com", "password123");
        user.setId(42L);

        UserDetailsImpl built = UserDetailsImpl.build(user);

        assertEquals(42L, built.getId());
        assertEquals("user@example.com", built.getEmail());
        assertEquals("user@example.com", built.getUsername());
        assertEquals("password123", built.getPassword());
    }

    @Test
    @DisplayName("Should return empty authorities collection")
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    @DisplayName("Should return id correctly")
    void testGetId() {
        assertEquals(1L, userDetails.getId());
    }

    @Test
    @DisplayName("Should return email correctly")
    void testGetEmail() {
        assertEquals("test@example.com", userDetails.getEmail());
    }

    @Test
    @DisplayName("Should return username as email")
    void testGetUsername() {
        assertEquals("test@example.com", userDetails.getUsername());
    }

    @Test
    @DisplayName("Should return password correctly")
    void testGetPassword() {
        assertEquals("hashedPassword", userDetails.getPassword());
    }

    @Test
    @DisplayName("Should return true for isAccountNonExpired")
    void testIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    @DisplayName("Should return true for isAccountNonLocked")
    void testIsAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    @DisplayName("Should return true for isCredentialsNonExpired")
    void testIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("Should return true for isEnabled")
    void testIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }

    @Test
    @DisplayName("Should handle null values in constructor")
    void testNullValues() {
        UserDetailsImpl nullDetails = new UserDetailsImpl(null, null, null);
        
        assertNull(nullDetails.getId());
        assertNull(nullDetails.getEmail());
        assertNull(nullDetails.getUsername());
        assertNull(nullDetails.getPassword());
    }

    @Test
    @DisplayName("Should handle user with null id in build")
    void testBuildWithNullId() {
        User user = new User("Test", "test@test.com", "password");
        // id is null by default
        
        UserDetailsImpl built = UserDetailsImpl.build(user);
        
        assertNull(built.getId());
        assertEquals("test@test.com", built.getEmail());
    }

    @Test
    @DisplayName("Should handle different user ids")
    void testDifferentIds() {
        UserDetailsImpl user1 = new UserDetailsImpl(1L, "user1@test.com", "pass1");
        UserDetailsImpl user2 = new UserDetailsImpl(2L, "user2@test.com", "pass2");
        
        assertNotEquals(user1.getId(), user2.getId());
        assertEquals(1L, user1.getId());
        assertEquals(2L, user2.getId());
    }

    @Test
    @DisplayName("Should handle zero id")
    void testZeroId() {
        UserDetailsImpl zeroIdUser = new UserDetailsImpl(0L, "zero@test.com", "pass");
        assertEquals(0L, zeroIdUser.getId());
    }

    @Test
    @DisplayName("Should handle email with special characters")
    void testEmailWithSpecialCharacters() {
        UserDetailsImpl specialEmail = new UserDetailsImpl(1L, "user+tag@sub.domain.com", "pass");
        assertEquals("user+tag@sub.domain.com", specialEmail.getEmail());
        assertEquals("user+tag@sub.domain.com", specialEmail.getUsername());
    }
}
