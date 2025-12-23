package com.example.loubna.controller;

import com.example.loubna.dto.request.TaskRequest;
import com.example.loubna.entity.Project;
import com.example.loubna.entity.Task;
import com.example.loubna.entity.User;
import com.example.loubna.repository.ProjectRepository;
import com.example.loubna.repository.TaskRepository;
import com.example.loubna.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("TaskController Tests")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private UserRepository userRepository;

    private User testUser;
    private Project testProject;
    private Task testTask;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        testUser = new User("Test User", "test@example.com", "hashedPassword");
        testUser.setId(1L);

        testProject = new Project();
        testProject.setId(1L);
        testProject.setTitle("Test Project");
        testProject.setUser(testUser);

        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Task Description");
        testTask.setDueDate(LocalDate.of(2024, 12, 31));
        testTask.setIsCompleted(false);
        testTask.setProject(testProject);
    }

    @Test
    @DisplayName("Should create task successfully")
    @WithMockUser(username = "test@example.com")
    void testCreateTaskSuccess() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("New Task");
        request.setDescription("New Description");
        request.setDueDate(LocalDate.of(2024, 12, 31));
        request.setProjectId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("succès")));
    }

    @Test
    @DisplayName("Should return error when project not found on task creation")
    @WithMockUser(username = "test@example.com")
    void testCreateTaskProjectNotFound() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("New Task");
        request.setProjectId(99L);

        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("introuvable")));
    }

    @Test
    @DisplayName("Should get tasks by project id")
    @WithMockUser(username = "test@example.com")
    void testGetTasksByProjectId() throws Exception {
        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setProject(testProject);

        List<Task> tasks = Arrays.asList(testTask, task2);

        when(taskRepository.findByProjectId(1L)).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks/project/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Test Task")))
                .andExpect(jsonPath("$[1].title", is("Task 2")));
    }

    @Test
    @DisplayName("Should return empty list when project has no tasks")
    @WithMockUser(username = "test@example.com")
    void testGetTasksByProjectIdEmpty() throws Exception {
        when(taskRepository.findByProjectId(1L)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/tasks/project/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Should toggle task status successfully")
    @WithMockUser(username = "test@example.com")
    void testToggleTaskStatus() throws Exception {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        mockMvc.perform(put("/api/tasks/1/toggle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("mis à jour")));

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should return error when toggling non-existent task")
    @WithMockUser(username = "test@example.com")
    void testToggleTaskStatusNotFound() throws Exception {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/tasks/99/toggle"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("introuvable")));
    }

    @Test
    @DisplayName("Should delete task successfully")
    @WithMockUser(username = "test@example.com")
    void testDeleteTaskSuccess() throws Exception {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("supprimée")));

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should return error when deleting non-existent task")
    @WithMockUser(username = "test@example.com")
    void testDeleteTaskNotFound() throws Exception {
        when(taskRepository.existsById(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/tasks/99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("introuvable")));
    }

    @Test
    @DisplayName("Should update task successfully")
    @WithMockUser(username = "test@example.com")
    void testUpdateTaskSuccess() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");
        request.setDueDate(LocalDate.of(2025, 1, 15));

        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("mise à jour")));
    }

    @Test
    @DisplayName("Should return error when updating non-existent task")
    @WithMockUser(username = "test@example.com")
    void testUpdateTaskNotFound() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("Updated Title");

        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/tasks/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("introuvable")));
    }

    @Test
    @DisplayName("Should require authentication for task endpoints")
    void testRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/tasks/project/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should handle task with null due date")
    @WithMockUser(username = "test@example.com")
    void testCreateTaskWithNullDueDate() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("Task without due date");
        request.setDescription("Description");
        request.setDueDate(null);
        request.setProjectId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
