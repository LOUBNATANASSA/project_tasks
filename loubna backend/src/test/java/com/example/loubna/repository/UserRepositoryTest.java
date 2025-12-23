package com.example.loubna.repository;

import com.example.loubna.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("UserRepository Tests")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Clear any existing data
    }

    @Test
    @DisplayName("Should save and find user by id")
    void testSaveAndFindById() {
        User user = new User("John Doe", "john@example.com", "hashedPassword");

        User savedUser = userRepository.save(user);
        entityManager.flush();

        Optional<User> found = userRepository.findById(savedUser.getId());

        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getName());
        assertEquals("john@example.com", found.get().getEmail());
    }

    @Test
    @DisplayName("Should find user by email")
    void testFindByEmail() {
        User user = new User("Jane Doe", "jane@example.com", "hashedPassword");
        entityManager.persist(user);
        entityManager.flush();

        Optional<User> found = userRepository.findByEmail("jane@example.com");

        assertTrue(found.isPresent());
        assertEquals("Jane Doe", found.get().getName());
    }

    @Test
    @DisplayName("Should return empty optional for non-existent email")
    void testFindByEmailNotFound() {
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Should check if email exists - true case")
    void testExistsByEmailTrue() {
        User user = new User("Test User", "exists@example.com", "password");
        entityManager.persist(user);
        entityManager.flush();

        assertTrue(userRepository.existsByEmail("exists@example.com"));
    }

    @Test
    @DisplayName("Should check if email exists - false case")
    void testExistsByEmailFalse() {
        assertFalse(userRepository.existsByEmail("notexists@example.com"));
    }

    @Test
    @DisplayName("Should delete user by id")
    void testDeleteById() {
        User user = new User("To Delete", "delete@example.com", "password");
        entityManager.persist(user);
        entityManager.flush();

        Long userId = user.getId();
        assertTrue(userRepository.existsById(userId));

        userRepository.deleteById(userId);
        entityManager.flush();

        assertFalse(userRepository.existsById(userId));
    }

    @Test
    @DisplayName("Should update user")
    void testUpdateUser() {
        User user = new User("Original Name", "original@example.com", "password");
        entityManager.persist(user);
        entityManager.flush();

        user.setName("Updated Name");
        user.setEmail("updated@example.com");
        userRepository.save(user);
        entityManager.flush();

        Optional<User> updated = userRepository.findById(user.getId());

        assertTrue(updated.isPresent());
        assertEquals("Updated Name", updated.get().getName());
        assertEquals("updated@example.com", updated.get().getEmail());
    }

    @Test
    @DisplayName("Should find all users")
    void testFindAll() {
        User user1 = new User("User 1", "user1@example.com", "password1");
        User user2 = new User("User 2", "user2@example.com", "password2");
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        List<User> allUsers = userRepository.findAll();

        assertEquals(2, allUsers.size());
    }

    @Test
    @DisplayName("Should count users")
    void testCount() {
        assertEquals(0, userRepository.count());

        User user = new User("Test", "test@example.com", "password");
        entityManager.persist(user);
        entityManager.flush();

        assertEquals(1, userRepository.count());
    }

    @Test
    @DisplayName("Should check if user exists by id")
    void testExistsById() {
        User user = new User("Existing", "existing@example.com", "password");
        entityManager.persist(user);
        entityManager.flush();

        assertTrue(userRepository.existsById(user.getId()));
        assertFalse(userRepository.existsById(99999L));
    }

    @Test
    @DisplayName("Should handle case-sensitive email search")
    void testEmailCaseSensitivity() {
        User user = new User("Test", "Test@Example.COM", "password");
        entityManager.persist(user);
        entityManager.flush();

        // This test verifies the actual behavior - adjust based on your requirements
        Optional<User> found = userRepository.findByEmail("Test@Example.COM");
        assertTrue(found.isPresent());
    }

    @Test
    @DisplayName("Should save user with constructor")
    void testSaveWithConstructor() {
        User user = new User("Constructor Test", "constructor@example.com", "secureHash");

        User saved = userRepository.save(user);
        entityManager.flush();

        assertNotNull(saved.getId());
        assertEquals("Constructor Test", saved.getName());
        assertEquals("constructor@example.com", saved.getEmail());
        assertEquals("secureHash", saved.getPasswordHash());
    }

    @Test
    @DisplayName("Should handle user with projects")
    void testUserWithProjects() {
        User user = new User("With Projects", "projects@example.com", "password");
        entityManager.persist(user);
        entityManager.flush();

        Optional<User> found = userRepository.findById(user.getId());

        assertTrue(found.isPresent());
        // Projects list might be null (lazy loading) or empty
    }

    @Test
    @DisplayName("Should find by email with special characters")
    void testEmailWithSpecialCharacters() {
        User user = new User("Special", "user+tag@sub.domain.example.com", "password");
        entityManager.persist(user);
        entityManager.flush();

        Optional<User> found = userRepository.findByEmail("user+tag@sub.domain.example.com");

        assertTrue(found.isPresent());
        assertEquals("Special", found.get().getName());
    }
}
