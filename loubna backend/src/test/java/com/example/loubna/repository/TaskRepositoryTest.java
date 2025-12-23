package com.example.loubna.repository;

import com.example.loubna.entity.Project;
import com.example.loubna.entity.Task;
import com.example.loubna.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("TaskRepository Tests")
class TaskRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepository taskRepository;

    private User testUser;
    private Project project1;
    private Project project2;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new User("Test User", "testuser@example.com", "hashedPassword");
        entityManager.persist(testUser);

        // Create test projects
        project1 = new Project();
        project1.setTitle("Project 1");
        project1.setDescription("Description 1");
        project1.setUser(testUser);
        entityManager.persist(project1);

        project2 = new Project();
        project2.setTitle("Project 2");
        project2.setDescription("Description 2");
        project2.setUser(testUser);
        entityManager.persist(project2);

        entityManager.flush();
    }

    @Test
    @DisplayName("Should save and find task by id")
    void testSaveAndFindById() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Task Description");
        task.setDueDate(LocalDate.of(2024, 12, 31));
        task.setIsCompleted(false);
        task.setProject(project1);

        Task savedTask = taskRepository.save(task);
        entityManager.flush();

        Optional<Task> found = taskRepository.findById(savedTask.getId());

        assertTrue(found.isPresent());
        assertEquals("Test Task", found.get().getTitle());
        assertEquals("Task Description", found.get().getDescription());
        assertFalse(found.get().getIsCompleted());
    }

    @Test
    @DisplayName("Should find all tasks by project id")
    void testFindByProjectId() {
        // Create tasks for project1
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setProject(project1);
        entityManager.persist(task1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setProject(project1);
        entityManager.persist(task2);

        // Create task for project2
        Task task3 = new Task();
        task3.setTitle("Task 3");
        task3.setProject(project2);
        entityManager.persist(task3);

        entityManager.flush();

        List<Task> project1Tasks = taskRepository.findByProjectId(project1.getId());

        assertEquals(2, project1Tasks.size());
        assertTrue(project1Tasks.stream().allMatch(t -> 
            t.getProject().getId().equals(project1.getId())));
    }

    @Test
    @DisplayName("Should return empty list when project has no tasks")
    void testFindByProjectIdNoTasks() {
        List<Task> tasks = taskRepository.findByProjectId(project1.getId());
        assertTrue(tasks.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list for non-existent project id")
    void testFindByProjectIdNonExistent() {
        List<Task> tasks = taskRepository.findByProjectId(99999L);
        assertTrue(tasks.isEmpty());
    }

    @Test
    @DisplayName("Should delete task by id")
    void testDeleteById() {
        Task task = new Task();
        task.setTitle("To Delete");
        task.setProject(project1);
        entityManager.persist(task);
        entityManager.flush();

        Long taskId = task.getId();
        assertTrue(taskRepository.existsById(taskId));

        taskRepository.deleteById(taskId);
        entityManager.flush();

        assertFalse(taskRepository.existsById(taskId));
    }

    @Test
    @DisplayName("Should check if task exists by id")
    void testExistsById() {
        Task task = new Task();
        task.setTitle("Existing Task");
        task.setProject(project1);
        entityManager.persist(task);
        entityManager.flush();

        assertTrue(taskRepository.existsById(task.getId()));
        assertFalse(taskRepository.existsById(99999L));
    }

    @Test
    @DisplayName("Should update task")
    void testUpdateTask() {
        Task task = new Task();
        task.setTitle("Original Title");
        task.setDescription("Original Description");
        task.setIsCompleted(false);
        task.setProject(project1);
        entityManager.persist(task);
        entityManager.flush();

        task.setTitle("Updated Title");
        task.setDescription("Updated Description");
        task.setIsCompleted(true);
        taskRepository.save(task);
        entityManager.flush();

        Optional<Task> updated = taskRepository.findById(task.getId());

        assertTrue(updated.isPresent());
        assertEquals("Updated Title", updated.get().getTitle());
        assertEquals("Updated Description", updated.get().getDescription());
        assertTrue(updated.get().getIsCompleted());
    }

    @Test
    @DisplayName("Should toggle task completion status")
    void testToggleCompletion() {
        Task task = new Task();
        task.setTitle("Toggle Task");
        task.setIsCompleted(false);
        task.setProject(project1);
        entityManager.persist(task);
        entityManager.flush();

        // Toggle to true
        task.setIsCompleted(!task.getIsCompleted());
        taskRepository.save(task);
        entityManager.flush();

        Optional<Task> toggled = taskRepository.findById(task.getId());
        assertTrue(toggled.isPresent());
        assertTrue(toggled.get().getIsCompleted());

        // Toggle back to false
        toggled.get().setIsCompleted(!toggled.get().getIsCompleted());
        taskRepository.save(toggled.get());
        entityManager.flush();

        Optional<Task> toggledBack = taskRepository.findById(task.getId());
        assertTrue(toggledBack.isPresent());
        assertFalse(toggledBack.get().getIsCompleted());
    }

    @Test
    @DisplayName("Should find all tasks")
    void testFindAll() {
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setProject(project1);
        entityManager.persist(task1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setProject(project2);
        entityManager.persist(task2);

        entityManager.flush();

        List<Task> allTasks = taskRepository.findAll();

        assertEquals(2, allTasks.size());
    }

    @Test
    @DisplayName("Should handle task with due date")
    void testTaskWithDueDate() {
        LocalDate dueDate = LocalDate.of(2024, 6, 15);
        
        Task task = new Task();
        task.setTitle("Task with due date");
        task.setDueDate(dueDate);
        task.setProject(project1);

        Task saved = taskRepository.save(task);
        entityManager.flush();

        Optional<Task> found = taskRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(dueDate, found.get().getDueDate());
    }

    @Test
    @DisplayName("Should handle task without due date")
    void testTaskWithoutDueDate() {
        Task task = new Task();
        task.setTitle("Task without due date");
        task.setDueDate(null);
        task.setProject(project1);

        Task saved = taskRepository.save(task);
        entityManager.flush();

        Optional<Task> found = taskRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertNull(found.get().getDueDate());
    }

    @Test
    @DisplayName("Should count tasks")
    void testCount() {
        assertEquals(0, taskRepository.count());

        Task task = new Task();
        task.setTitle("Task");
        task.setProject(project1);
        entityManager.persist(task);
        entityManager.flush();

        assertEquals(1, taskRepository.count());
    }

    @Test
    @DisplayName("Should handle default isCompleted value")
    void testDefaultIsCompleted() {
        Task task = new Task();
        task.setTitle("Default isCompleted");
        task.setProject(project1);

        Task saved = taskRepository.save(task);
        entityManager.flush();

        Optional<Task> found = taskRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertFalse(found.get().getIsCompleted());
    }
}
