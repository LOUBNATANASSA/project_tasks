package com.example.loubna.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Entity Tests")
class UserTest {

    @Test
    @DisplayName("Should create user with default constructor")
    void testDefaultConstructor() {
        User user = new User();
        
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getPasswordHash());
        assertNull(user.getProjects());
    }

    @Test
    @DisplayName("Should create user with parameterized constructor")
    void testParameterizedConstructor() {
        String name = "John Doe";
        String email = "john@example.com";
        String password = "hashedPassword123";

        User user = new User(name, email, password);

        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPasswordHash());
    }

    @Test
    @DisplayName("Should set and get id correctly")
    void testIdGetterSetter() {
        User user = new User();
        Long expectedId = 1L;

        user.setId(expectedId);

        assertEquals(expectedId, user.getId());
    }

    @Test
    @DisplayName("Should set and get name correctly")
    void testNameGetterSetter() {
        User user = new User();
        String expectedName = "Jane Doe";

        user.setName(expectedName);

        assertEquals(expectedName, user.getName());
    }

    @Test
    @DisplayName("Should set and get email correctly")
    void testEmailGetterSetter() {
        User user = new User();
        String expectedEmail = "jane@example.com";

        user.setEmail(expectedEmail);

        assertEquals(expectedEmail, user.getEmail());
    }

    @Test
    @DisplayName("Should set and get password hash correctly")
    void testPasswordHashGetterSetter() {
        User user = new User();
        String expectedHash = "secureHashedPassword";

        user.setPasswordHash(expectedHash);

        assertEquals(expectedHash, user.getPasswordHash());
    }

    @Test
    @DisplayName("Should set and get projects correctly")
    void testProjectsGetterSetter() {
        User user = new User();
        List<Project> projects = new ArrayList<>();
        Project project = new Project();
        project.setTitle("Test Project");
        projects.add(project);

        user.setProjects(projects);

        assertNotNull(user.getProjects());
        assertEquals(1, user.getProjects().size());
        assertEquals("Test Project", user.getProjects().get(0).getTitle());
    }

    @Test
    @DisplayName("Should handle null values in setters")
    void testNullValues() {
        User user = new User("Test", "test@test.com", "password");

        user.setName(null);
        user.setEmail(null);
        user.setPasswordHash(null);
        user.setProjects(null);

        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getPasswordHash());
        assertNull(user.getProjects());
    }

    @Test
    @DisplayName("Should handle empty projects list")
    void testEmptyProjectsList() {
        User user = new User();
        List<Project> emptyProjects = new ArrayList<>();

        user.setProjects(emptyProjects);

        assertNotNull(user.getProjects());
        assertTrue(user.getProjects().isEmpty());
    }
}
