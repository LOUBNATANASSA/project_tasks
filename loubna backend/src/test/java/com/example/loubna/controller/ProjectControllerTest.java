package com.example.loubna.controller;

import com.example.loubna.dto.request.ProjectRequest;
import com.example.loubna.dto.response.MessageResponse;
import com.example.loubna.entity.Project;
import com.example.loubna.entity.User;
import com.example.loubna.repository.ProjectRepository;
import com.example.loubna.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@DisplayName("ProjectController Tests")
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private UserRepository userRepository;

    private User testUser;
    private Project testProject;

    @BeforeEach
    void setUp() {
        testUser = new User("Test User", "test@example.com", "hashedPassword");
        testUser.setId(1L);

        testProject = new Project();
        testProject.setId(1L);
        testProject.setTitle("Test Project");
        testProject.setDescription("Test Description");
        testProject.setUser(testUser);
    }

    @Test
    @DisplayName("Should get project by id when exists")
    @WithMockUser(username = "test@example.com")
    void testGetProjectByIdSuccess() throws Exception {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test Project")))
                .andExpect(jsonPath("$.description", is("Test Description")));
    }

    @Test
    @DisplayName("Should return 404 when project not found")
    @WithMockUser(username = "test@example.com")
    void testGetProjectByIdNotFound() throws Exception {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/projects/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should get user projects")
    @WithMockUser(username = "test@example.com")
    void testGetUserProjects() throws Exception {
        Project project2 = new Project();
        project2.setId(2L);
        project2.setTitle("Project 2");
        project2.setUser(testUser);

        List<Project> projects = Arrays.asList(testProject, project2);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(projectRepository.findByUserId(testUser.getId())).thenReturn(projects);

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Test Project")))
                .andExpect(jsonPath("$[1].title", is("Project 2")));
    }

    @Test
    @DisplayName("Should create project successfully")
    @WithMockUser(username = "test@example.com")
    void testCreateProjectSuccess() throws Exception {
        ProjectRequest request = new ProjectRequest();
        request.setTitle("New Project");
        request.setDescription("New Description");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("succès")));
    }

    @Test
    @DisplayName("Should delete project successfully")
    @WithMockUser(username = "test@example.com")
    void testDeleteProjectSuccess() throws Exception {
        when(projectRepository.existsById(1L)).thenReturn(true);
        doNothing().when(projectRepository).deleteById(1L);

        mockMvc.perform(delete("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("supprimé")));

        verify(projectRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should return error when deleting non-existent project")
    @WithMockUser(username = "test@example.com")
    void testDeleteProjectNotFound() throws Exception {
        when(projectRepository.existsById(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/projects/99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("introuvable")));
    }

    @Test
    @DisplayName("Should update project with PUT")
    @WithMockUser(username = "test@example.com")
    void testUpdateProjectPut() throws Exception {
        ProjectRequest request = new ProjectRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        mockMvc.perform(put("/api/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should update project with PATCH")
    @WithMockUser(username = "test@example.com")
    void testUpdateProjectPatch() throws Exception {
        String patchContent = "{\"title\": \"Patched Title\"}";

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        mockMvc.perform(patch("/api/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchContent))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent project")
    @WithMockUser(username = "test@example.com")
    void testUpdateProjectNotFound() throws Exception {
        ProjectRequest request = new ProjectRequest();
        request.setTitle("Updated");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/projects/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 403 when user is not project owner")
    @WithMockUser(username = "other@example.com")
    void testUpdateProjectForbidden() throws Exception {
        User otherUser = new User("Other", "other@example.com", "password");
        otherUser.setId(2L);

        ProjectRequest request = new ProjectRequest();
        request.setTitle("Updated");

        when(userRepository.findByEmail("other@example.com")).thenReturn(Optional.of(otherUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));

        mockMvc.perform(put("/api/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should reject empty title on update")
    @WithMockUser(username = "test@example.com")
    void testUpdateProjectEmptyTitle() throws Exception {
        ProjectRequest request = new ProjectRequest();
        request.setTitle("   ");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));

        mockMvc.perform(put("/api/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("vide")));
    }

    @Test
    @DisplayName("Should require authentication")
    void testRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isUnauthorized());
    }
}
