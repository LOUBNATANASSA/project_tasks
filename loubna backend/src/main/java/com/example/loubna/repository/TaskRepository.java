package com.example.loubna.repository;

import com.example.loubna.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    // Récupérer toutes les tâches liées à un ID de projet
    List<Task> findByProjectId(Long projectId);
}