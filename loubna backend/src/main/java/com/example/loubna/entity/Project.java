package com.example.loubna.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.example.loubna.entity.Task;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;


    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    public Double getProgress() {
        if (tasks == null || tasks.isEmpty()) {
            return 0.0;
        }
        long completedCount = tasks.stream().filter(t -> Boolean.TRUE.equals(t.getIsCompleted())).count();
        return (double) completedCount / tasks.size() * 100;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
}