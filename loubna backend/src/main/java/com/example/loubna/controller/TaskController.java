package com.example.loubna.controller;

import com.example.loubna.dto.request.TaskRequest;
import com.example.loubna.dto.response.MessageResponse;
import com.example.loubna.entity.Project;
import com.example.loubna.entity.Task;
import com.example.loubna.entity.User;
import com.example.loubna.repository.ProjectRepository;
import com.example.loubna.repository.TaskRepository;
import com.example.loubna.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskRequest taskRequest) {

        Optional<Project> projectOpt = projectRepository.findById(taskRequest.getProjectId());

        if (projectOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Erreur : Projet introuvable."));
        }

        Project project = projectOpt.get();

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(taskRequest.getDueDate());
        task.setProject(project);

        taskRepository.save(task);

        return ResponseEntity.ok(new MessageResponse("Tâche ajoutée avec succès !"));
    }


    @GetMapping("/project/{projectId}")
    public List<Task> getTasksByProject(@PathVariable Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }


    @PutMapping("/{taskId}/toggle")
    public ResponseEntity<?> toggleTaskStatus(@PathVariable Long taskId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);

        if (taskOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Erreur : Tâche introuvable."));
        }

        Task task = taskOpt.get();

        task.setIsCompleted(!task.getIsCompleted());

        taskRepository.save(task);

        return ResponseEntity.ok(new MessageResponse("Statut de la tâche mis à jour !"));
    }


    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Erreur : Tâche introuvable."));
        }

        taskRepository.deleteById(taskId);
        return ResponseEntity.ok(new MessageResponse("Tâche supprimée avec succès !"));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskRequest taskRequest) {

        Optional<Task> taskOpt = taskRepository.findById(taskId);

        if (taskOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Erreur : Tâche introuvable."));
        }

        Task task = taskOpt.get();

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(taskRequest.getDueDate());

        taskRepository.save(task);

        return ResponseEntity.ok(
                new MessageResponse("Tâche mise à jour avec succès !")
        );
    }



}