package com.example.loubna.dto.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskRequest DTO Tests")
class TaskRequestTest {

    private TaskRequest taskRequest;

    @BeforeEach
    void setUp() {
        taskRequest = new TaskRequest();
    }

    @Test
    @DisplayName("Should create TaskRequest with default null values")
    void testDefaultConstructor() {
        assertNull(taskRequest.getTitle());
        assertNull(taskRequest.getDescription());
        assertNull(taskRequest.getDueDate());
        assertNull(taskRequest.getProjectId());
    }

    @Test
    @DisplayName("Should set and get title correctly")
    void testTitleGetterSetter() {
        String expectedTitle = "Implement feature X";
        taskRequest.setTitle(expectedTitle);
        assertEquals(expectedTitle, taskRequest.getTitle());
    }

    @Test
    @DisplayName("Should set and get description correctly")
    void testDescriptionGetterSetter() {
        String expectedDescription = "Detailed task description";
        taskRequest.setDescription(expectedDescription);
        assertEquals(expectedDescription, taskRequest.getDescription());
    }

    @Test
    @DisplayName("Should set and get dueDate correctly")
    void testDueDateGetterSetter() {
        LocalDate expectedDate = LocalDate.of(2024, 6, 15);
        taskRequest.setDueDate(expectedDate);
        assertEquals(expectedDate, taskRequest.getDueDate());
    }

    @Test
    @DisplayName("Should set and get projectId correctly")
    void testProjectIdGetterSetter() {
        Long expectedProjectId = 42L;
        taskRequest.setProjectId(expectedProjectId);
        assertEquals(expectedProjectId, taskRequest.getProjectId());
    }

    @Test
    @DisplayName("Should handle null values in all fields")
    void testNullValues() {
        taskRequest.setTitle("Title");
        taskRequest.setDescription("Description");
        taskRequest.setDueDate(LocalDate.now());
        taskRequest.setProjectId(1L);

        taskRequest.setTitle(null);
        taskRequest.setDescription(null);
        taskRequest.setDueDate(null);
        taskRequest.setProjectId(null);

        assertNull(taskRequest.getTitle());
        assertNull(taskRequest.getDescription());
        assertNull(taskRequest.getDueDate());
        assertNull(taskRequest.getProjectId());
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testEmptyStrings() {
        taskRequest.setTitle("");
        taskRequest.setDescription("");

        assertEquals("", taskRequest.getTitle());
        assertEquals("", taskRequest.getDescription());
    }

    @Test
    @DisplayName("Should handle today's date")
    void testTodayDueDate() {
        LocalDate today = LocalDate.now();
        taskRequest.setDueDate(today);
        assertEquals(today, taskRequest.getDueDate());
    }

    @Test
    @DisplayName("Should handle past due dates")
    void testPastDueDate() {
        LocalDate pastDate = LocalDate.of(2020, 1, 1);
        taskRequest.setDueDate(pastDate);
        assertEquals(pastDate, taskRequest.getDueDate());
    }

    @Test
    @DisplayName("Should handle future due dates")
    void testFutureDueDate() {
        LocalDate futureDate = LocalDate.of(2030, 12, 31);
        taskRequest.setDueDate(futureDate);
        assertEquals(futureDate, taskRequest.getDueDate());
    }

    @Test
    @DisplayName("Should handle zero projectId")
    void testZeroProjectId() {
        taskRequest.setProjectId(0L);
        assertEquals(0L, taskRequest.getProjectId());
    }

    @Test
    @DisplayName("Should handle negative projectId")
    void testNegativeProjectId() {
        taskRequest.setProjectId(-1L);
        assertEquals(-1L, taskRequest.getProjectId());
    }

    @Test
    @DisplayName("Should handle large projectId")
    void testLargeProjectId() {
        Long largeId = Long.MAX_VALUE;
        taskRequest.setProjectId(largeId);
        assertEquals(largeId, taskRequest.getProjectId());
    }

    @Test
    @DisplayName("Should set all fields together")
    void testAllFieldsTogether() {
        String title = "Complete Task";
        String description = "Task description";
        LocalDate dueDate = LocalDate.of(2024, 12, 25);
        Long projectId = 100L;

        taskRequest.setTitle(title);
        taskRequest.setDescription(description);
        taskRequest.setDueDate(dueDate);
        taskRequest.setProjectId(projectId);

        assertEquals(title, taskRequest.getTitle());
        assertEquals(description, taskRequest.getDescription());
        assertEquals(dueDate, taskRequest.getDueDate());
        assertEquals(projectId, taskRequest.getProjectId());
    }

    @Test
    @DisplayName("Should handle special characters in strings")
    void testSpecialCharacters() {
        String specialTitle = "Task: émoji 🎯 & symbols @#$%";
        String specialDescription = "Description avec <html> & \"quotes\"";

        taskRequest.setTitle(specialTitle);
        taskRequest.setDescription(specialDescription);

        assertEquals(specialTitle, taskRequest.getTitle());
        assertEquals(specialDescription, taskRequest.getDescription());
    }
}
