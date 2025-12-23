package com.example.loubna.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Task Entity Tests")
class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
    }

    @Test
    @DisplayName("Should create task with default values")
    void testDefaultConstructor() {
        assertNull(task.getId());
        assertNull(task.getTitle());
        assertNull(task.getDescription());
        assertNull(task.getDueDate());
        assertFalse(task.getIsCompleted()); // Default is false
        assertNull(task.getProject());
    }

    @Test
    @DisplayName("Should have isCompleted default to false")
    void testDefaultIsCompleted() {
        Task newTask = new Task();
        assertFalse(newTask.getIsCompleted());
    }

    @Test
    @DisplayName("Should set and get id correctly")
    void testIdGetterSetter() {
        Long expectedId = 1L;
        task.setId(expectedId);
        assertEquals(expectedId, task.getId());
    }

    @Test
    @DisplayName("Should set and get title correctly")
    void testTitleGetterSetter() {
        String expectedTitle = "Complete documentation";
        task.setTitle(expectedTitle);
        assertEquals(expectedTitle, task.getTitle());
    }

    @Test
    @DisplayName("Should set and get description correctly")
    void testDescriptionGetterSetter() {
        String expectedDescription = "Write comprehensive documentation for the API";
        task.setDescription(expectedDescription);
        assertEquals(expectedDescription, task.getDescription());
    }

    @Test
    @DisplayName("Should set and get dueDate correctly")
    void testDueDateGetterSetter() {
        LocalDate expectedDate = LocalDate.of(2024, 12, 31);
        task.setDueDate(expectedDate);
        assertEquals(expectedDate, task.getDueDate());
    }

    @Test
    @DisplayName("Should set and get isCompleted correctly")
    void testIsCompletedGetterSetter() {
        task.setIsCompleted(true);
        assertTrue(task.getIsCompleted());

        task.setIsCompleted(false);
        assertFalse(task.getIsCompleted());
    }

    @Test
    @DisplayName("Should set and get project correctly")
    void testProjectGetterSetter() {
        Project project = new Project();
        project.setTitle("Test Project");
        project.setDescription("Test Description");

        task.setProject(project);

        assertNotNull(task.getProject());
        assertEquals("Test Project", task.getProject().getTitle());
    }

    @Test
    @DisplayName("Should handle null title")
    void testNullTitle() {
        task.setTitle("Initial Title");
        task.setTitle(null);
        assertNull(task.getTitle());
    }

    @Test
    @DisplayName("Should handle null description")
    void testNullDescription() {
        task.setDescription("Initial Description");
        task.setDescription(null);
        assertNull(task.getDescription());
    }

    @Test
    @DisplayName("Should handle null dueDate")
    void testNullDueDate() {
        task.setDueDate(LocalDate.now());
        task.setDueDate(null);
        assertNull(task.getDueDate());
    }

    @Test
    @DisplayName("Should handle null isCompleted")
    void testNullIsCompleted() {
        task.setIsCompleted(null);
        assertNull(task.getIsCompleted());
    }

    @Test
    @DisplayName("Should handle null project")
    void testNullProject() {
        Project project = new Project();
        task.setProject(project);
        task.setProject(null);
        assertNull(task.getProject());
    }

    @Test
    @DisplayName("Should handle past due dates")
    void testPastDueDate() {
        LocalDate pastDate = LocalDate.of(2020, 1, 1);
        task.setDueDate(pastDate);
        assertEquals(pastDate, task.getDueDate());
    }

    @Test
    @DisplayName("Should handle future due dates")
    void testFutureDueDate() {
        LocalDate futureDate = LocalDate.of(2030, 12, 31);
        task.setDueDate(futureDate);
        assertEquals(futureDate, task.getDueDate());
    }

    @Test
    @DisplayName("Should handle empty string title")
    void testEmptyStringTitle() {
        task.setTitle("");
        assertEquals("", task.getTitle());
    }

    @Test
    @DisplayName("Should handle empty string description")
    void testEmptyStringDescription() {
        task.setDescription("");
        assertEquals("", task.getDescription());
    }

    @Test
    @DisplayName("Should handle very long title")
    void testVeryLongTitle() {
        String longTitle = "A".repeat(1000);
        task.setTitle(longTitle);
        assertEquals(longTitle, task.getTitle());
        assertEquals(1000, task.getTitle().length());
    }

    @Test
    @DisplayName("Should handle very long description")
    void testVeryLongDescription() {
        String longDescription = "B".repeat(5000);
        task.setDescription(longDescription);
        assertEquals(longDescription, task.getDescription());
        assertEquals(5000, task.getDescription().length());
    }

    @Test
    @DisplayName("Should toggle isCompleted status")
    void testToggleIsCompleted() {
        assertFalse(task.getIsCompleted());
        
        task.setIsCompleted(!task.getIsCompleted());
        assertTrue(task.getIsCompleted());
        
        task.setIsCompleted(!task.getIsCompleted());
        assertFalse(task.getIsCompleted());
    }
}
