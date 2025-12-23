package com.example.loubna.repository;

import com.example.loubna.entity.Project;
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
@DisplayName("ProjectRepository Tests")
class ProjectRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProjectRepository projectRepository;

    private User testUser;
    private User anotherUser;

    @BeforeEach
    void setUp() {
        // Create test users
        testUser = new User("Test User", "testuser@example.com", "hashedPassword123");
        entityManager.persist(testUser);

        anotherUser = new User("Another User", "another@example.com", "hashedPassword456");
        entityManager.persist(anotherUser);

        entityManager.flush();
    }

    @Test
    @DisplayName("Should save and find project by id")
    void testSaveAndFindById() {
        Project project = new Project();
        project.setTitle("Test Project");
        project.setDescription("Test Description");
        project.setUser(testUser);

        Project savedProject = projectRepository.save(project);
        entityManager.flush();

        Optional<Project> found = projectRepository.findById(savedProject.getId());

        assertTrue(found.isPresent());
        assertEquals("Test Project", found.get().getTitle());
        assertEquals("Test Description", found.get().getDescription());
    }

    @Test
    @DisplayName("Should find all projects by user id")
    void testFindByUserId() {
        // Create projects for testUser
        Project project1 = new Project();
        project1.setTitle("Project 1");
        project1.setDescription("Description 1");
        project1.setUser(testUser);
        entityManager.persist(project1);

        Project project2 = new Project();
        project2.setTitle("Project 2");
        project2.setDescription("Description 2");
        project2.setUser(testUser);
        entityManager.persist(project2);

        // Create project for anotherUser
        Project project3 = new Project();
        project3.setTitle("Project 3");
        project3.setDescription("Description 3");
        project3.setUser(anotherUser);
        entityManager.persist(project3);

        entityManager.flush();

        List<Project> userProjects = projectRepository.findByUserId(testUser.getId());

        assertEquals(2, userProjects.size());
        assertTrue(userProjects.stream().allMatch(p -> p.getUser().getId().equals(testUser.getId())));
    }

    @Test
    @DisplayName("Should return empty list when user has no projects")
    void testFindByUserIdNoProjects() {
        List<Project> userProjects = projectRepository.findByUserId(testUser.getId());
        assertTrue(userProjects.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list for non-existent user id")
    void testFindByUserIdNonExistent() {
        List<Project> projects = projectRepository.findByUserId(99999L);
        assertTrue(projects.isEmpty());
    }

    @Test
    @DisplayName("Should delete project by id")
    void testDeleteById() {
        Project project = new Project();
        project.setTitle("To Delete");
        project.setDescription("Will be deleted");
        project.setUser(testUser);
        entityManager.persist(project);
        entityManager.flush();

        Long projectId = project.getId();
        assertTrue(projectRepository.existsById(projectId));

        projectRepository.deleteById(projectId);
        entityManager.flush();

        assertFalse(projectRepository.existsById(projectId));
    }

    @Test
    @DisplayName("Should check if project exists by id")
    void testExistsById() {
        Project project = new Project();
        project.setTitle("Existing Project");
        project.setUser(testUser);
        entityManager.persist(project);
        entityManager.flush();

        assertTrue(projectRepository.existsById(project.getId()));
        assertFalse(projectRepository.existsById(99999L));
    }

    @Test
    @DisplayName("Should update project")
    void testUpdateProject() {
        Project project = new Project();
        project.setTitle("Original Title");
        project.setDescription("Original Description");
        project.setUser(testUser);
        entityManager.persist(project);
        entityManager.flush();

        project.setTitle("Updated Title");
        project.setDescription("Updated Description");
        projectRepository.save(project);
        entityManager.flush();

        Optional<Project> updated = projectRepository.findById(project.getId());

        assertTrue(updated.isPresent());
        assertEquals("Updated Title", updated.get().getTitle());
        assertEquals("Updated Description", updated.get().getDescription());
    }

    @Test
    @DisplayName("Should find all projects")
    void testFindAll() {
        Project project1 = new Project();
        project1.setTitle("Project 1");
        project1.setUser(testUser);
        entityManager.persist(project1);

        Project project2 = new Project();
        project2.setTitle("Project 2");
        project2.setUser(anotherUser);
        entityManager.persist(project2);

        entityManager.flush();

        List<Project> allProjects = projectRepository.findAll();

        assertEquals(2, allProjects.size());
    }

    @Test
    @DisplayName("Should count projects")
    void testCount() {
        assertEquals(0, projectRepository.count());

        Project project = new Project();
        project.setTitle("Project");
        project.setUser(testUser);
        entityManager.persist(project);
        entityManager.flush();

        assertEquals(1, projectRepository.count());
    }

    @Test
    @DisplayName("Should handle project with null description")
    void testProjectWithNullDescription() {
        Project project = new Project();
        project.setTitle("No Description Project");
        project.setDescription(null);
        project.setUser(testUser);

        Project saved = projectRepository.save(project);
        entityManager.flush();

        Optional<Project> found = projectRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertNull(found.get().getDescription());
    }
}
