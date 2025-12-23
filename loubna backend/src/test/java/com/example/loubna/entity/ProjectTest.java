package com.example.loubna.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Project Entity Tests")
class ProjectTest {

    private Project project;

    @BeforeEach
    void setUp() {
        project = new Project();
    }

    @Test
    @DisplayName("Should create project with default values")
    void testDefaultConstructor() {
        assertNull(project.getId());
        assertNull(project.getTitle());
        assertNull(project.getDescription());
        assertNull(project.getCreatedAt());
        assertNull(project.getUpdatedAt());
        assertNull(project.getUser());
        assertNull(project.getTasks());
    }

    @Test
    @DisplayName("Should set and get id correctly")
    void testIdGetterSetter() {
        Long expectedId = 1L;
        project.setId(expectedId);
        assertEquals(expectedId, project.getId());
    }

    @Test
    @DisplayName("Should set and get title correctly")
    void testTitleGetterSetter() {
        String expectedTitle = "Project Alpha";
        project.setTitle(expectedTitle);
        assertEquals(expectedTitle, project.getTitle());
    }

    @Test
    @DisplayName("Should set and get description correctly")
    void testDescriptionGetterSetter() {
        String expectedDescription = "This is a test project description";
        project.setDescription(expectedDescription);
        assertEquals(expectedDescription, project.getDescription());
    }

    @Test
    @DisplayName("Should set and get createdAt correctly")
    void testCreatedAtGetterSetter() {
        LocalDateTime expectedTime = LocalDateTime.of(2024, 1, 15, 10, 30);
        project.setCreatedAt(expectedTime);
        assertEquals(expectedTime, project.getCreatedAt());
    }

    @Test
    @DisplayName("Should set and get updatedAt correctly")
    void testUpdatedAtGetterSetter() {
        LocalDateTime expectedTime = LocalDateTime.of(2024, 1, 20, 14, 45);
        project.setUpdatedAt(expectedTime);
        assertEquals(expectedTime, project.getUpdatedAt());
    }

    @Test
    @DisplayName("Should set and get user correctly")
    void testUserGetterSetter() {
        User user = new User("Test User", "test@example.com", "password");
        project.setUser(user);
        
        assertNotNull(project.getUser());
        assertEquals("Test User", project.getUser().getName());
        assertEquals("test@example.com", project.getUser().getEmail());
    }

    @Test
    @DisplayName("Should set and get tasks correctly")
    void testTasksGetterSetter() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task();
        task1.setTitle("Task 1");
        Task task2 = new Task();
        task2.setTitle("Task 2");
        tasks.add(task1);
        tasks.add(task2);

        project.setTasks(tasks);

        assertNotNull(project.getTasks());
        assertEquals(2, project.getTasks().size());
    }

    // Tests pour la méthode getProgress()
    @Test
    @DisplayName("Should return 0% progress when tasks list is null")
    void testProgressWithNullTasks() {
        project.setTasks(null);
        assertEquals(0.0, project.getProgress());
    }

    @Test
    @DisplayName("Should return 0% progress when tasks list is empty")
    void testProgressWithEmptyTasks() {
        project.setTasks(new ArrayList<>());
        assertEquals(0.0, project.getProgress());
    }

    @Test
    @DisplayName("Should return 0% progress when no tasks are completed")
    void testProgressWithNoCompletedTasks() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task();
        task1.setIsCompleted(false);
        Task task2 = new Task();
        task2.setIsCompleted(false);
        tasks.add(task1);
        tasks.add(task2);

        project.setTasks(tasks);

        assertEquals(0.0, project.getProgress());
    }

    @Test
    @DisplayName("Should return 100% progress when all tasks are completed")
    void testProgressWithAllTasksCompleted() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task();
        task1.setIsCompleted(true);
        Task task2 = new Task();
        task2.setIsCompleted(true);
        tasks.add(task1);
        tasks.add(task2);

        project.setTasks(tasks);

        assertEquals(100.0, project.getProgress());
    }

    @Test
    @DisplayName("Should return 50% progress when half of tasks are completed")
    void testProgressWithHalfTasksCompleted() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task();
        task1.setIsCompleted(true);
        Task task2 = new Task();
        task2.setIsCompleted(false);
        tasks.add(task1);
        tasks.add(task2);

        project.setTasks(tasks);

        assertEquals(50.0, project.getProgress());
    }

    @Test
    @DisplayName("Should calculate correct progress with multiple tasks")
    void testProgressWithMultipleTasks() {
        List<Task> tasks = new ArrayList<>();
        // 2 completed out of 4 = 50%
        for (int i = 0; i < 4; i++) {
            Task task = new Task();
            task.setIsCompleted(i < 2);
            tasks.add(task);
        }

        project.setTasks(tasks);

        assertEquals(50.0, project.getProgress());
    }

    @Test
    @DisplayName("Should handle tasks with null isCompleted value")
    void testProgressWithNullIsCompleted() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task();
        task1.setIsCompleted(null);
        Task task2 = new Task();
        task2.setIsCompleted(true);
        tasks.add(task1);
        tasks.add(task2);

        project.setTasks(tasks);

        // null is not equal to true, so only 1 task is completed out of 2 = 50%
        assertEquals(50.0, project.getProgress());
    }

    @Test
    @DisplayName("Should calculate progress correctly with single completed task")
    void testProgressWithSingleCompletedTask() {
        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setIsCompleted(true);
        tasks.add(task);

        project.setTasks(tasks);

        assertEquals(100.0, project.getProgress());
    }

    @Test
    @DisplayName("Should calculate progress correctly with single incomplete task")
    void testProgressWithSingleIncompleteTask() {
        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setIsCompleted(false);
        tasks.add(task);

        project.setTasks(tasks);

        assertEquals(0.0, project.getProgress());
    }

    @Test
    @DisplayName("Should handle null values in setters")
    void testNullValues() {
        project.setTitle("Initial Title");
        project.setDescription("Initial Description");

        project.setTitle(null);
        project.setDescription(null);
        project.setUser(null);
        project.setTasks(null);

        assertNull(project.getTitle());
        assertNull(project.getDescription());
        assertNull(project.getUser());
        assertNull(project.getTasks());
    }
}
