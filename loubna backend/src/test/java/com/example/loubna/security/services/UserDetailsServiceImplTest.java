package com.example.loubna.security.services;

import com.example.loubna.entity.User;
import com.example.loubna.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserDetailsServiceImpl Tests")
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("Test User", "test@example.com", "hashedPassword");
        testUser.setId(1L);
    }

    @Test
    @DisplayName("Should load user by username (email) successfully")
    void testLoadUserByUsernameSuccess() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("hashedPassword", userDetails.getPassword());
        
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not found")
    void testLoadUserByUsernameNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("notfound@example.com")
        );

        assertTrue(exception.getMessage().contains("notfound@example.com"));
        verify(userRepository, times(1)).findByEmail("notfound@example.com");
    }

    @Test
    @DisplayName("Should return UserDetailsImpl from loadUserByUsername")
    void testLoadUserByUsernameReturnsUserDetailsImpl() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        assertInstanceOf(UserDetailsImpl.class, userDetails);
        
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
        assertEquals(1L, userDetailsImpl.getId());
        assertEquals("test@example.com", userDetailsImpl.getEmail());
    }

    @Test
    @DisplayName("Should handle case-sensitive email")
    void testLoadUserByUsernameCaseSensitive() {
        when(userRepository.findByEmail("Test@Example.COM")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername("Test@Example.COM");

        assertNotNull(userDetails);
        verify(userRepository, times(1)).findByEmail("Test@Example.COM");
    }

    @Test
    @DisplayName("Should handle null email")
    void testLoadUserByUsernameNull() {
        when(userRepository.findByEmail(null)).thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(null)
        );
    }

    @Test
    @DisplayName("Should handle empty email")
    void testLoadUserByUsernameEmpty() {
        when(userRepository.findByEmail("")).thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("")
        );
    }

    @Test
    @DisplayName("Should properly map user properties to UserDetails")
    void testUserPropertiesMapping() {
        User user = new User("John Doe", "john@example.com", "secureHash123");
        user.setId(42L);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("john@example.com");

        assertEquals("john@example.com", userDetails.getUsername());
        assertEquals("secureHash123", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("Should verify repository is called only once")
    void testRepositoryCalledOnce() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        userDetailsService.loadUserByUsername("test@example.com");

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verifyNoMoreInteractions(userRepository);
    }
}
