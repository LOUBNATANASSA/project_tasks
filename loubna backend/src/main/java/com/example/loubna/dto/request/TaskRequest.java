package com.example.loubna.dto.request;

import java.time.LocalDate;

public class TaskRequest {
    private String title;
    private String description;
    private LocalDate dueDate;
    private Long projectId; // Important pour lier la tâche au projet

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
}