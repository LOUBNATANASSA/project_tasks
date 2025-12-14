package com.example.loubna.repository;

import com.example.loubna.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Récupérer tous les projets d'un utilisateur donné
    List<Project> findByUserId(Long userId);
}