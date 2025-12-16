package com.example.loubna.controller;

import com.example.loubna.dto.request.ProjectRequest;
import com.example.loubna.dto.response.MessageResponse;
import com.example.loubna.entity.Project;
import com.example.loubna.entity.User;
import com.example.loubna.repository.ProjectRepository;
import com.example.loubna.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;


    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody ProjectRequest projectRequest) {

        User currentUser = getCurrentUser();

        if (currentUser == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Erreur : Utilisateur non trouvé."));
        }

        Project project = new Project();
        project.setTitle(projectRequest.getTitle());
        project.setDescription(projectRequest.getDescription());
        project.setUser(currentUser);

        projectRepository.save(project);

        return ResponseEntity.ok(new MessageResponse("Projet créé avec succès !"));
    }

    @GetMapping
    public List<Project> getUserProjects() {
        User currentUser = getCurrentUser();

        return projectRepository.findByUserId(currentUser.getId());
    }


    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            return userRepository.findByEmail(email).orElse(null);
        }
        return null;
    }
}