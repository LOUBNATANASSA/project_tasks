import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router'; // Ajout de RouterModule
import { TaskService } from '../../services/task'; // Vérifie que task.ts existe bien dans services

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule], // Ajout important pour routerLink
  templateUrl: './task-list.html', 
  styleUrl: './task-list.css'
})
export class TaskListComponent implements OnInit { // <--- C'EST ICI QUE CA BLOQUAIT (vérifie le 'export')
  tasks: any[] = [];
  projectId: any;
  newTaskTitle = '';

  constructor(
    private taskService: TaskService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.projectId = this.route.snapshot.paramMap.get('id');
    this.loadTasks();
  }

  loadTasks(): void {
    this.taskService.getTasksByProject(this.projectId).subscribe({
      next: (data: any) => {
        this.tasks = data;
      },
      error: (err: any) => console.error(err)
    });
  }

  addTask(): void {
    if (!this.newTaskTitle) return;

    const task = {
      title: this.newTaskTitle,
      description: '',
      projectId: this.projectId
    };

    this.taskService.createTask(task).subscribe({
      next: () => {
        this.newTaskTitle = '';
        this.loadTasks();
      }
    });
  }

  toggleTask(taskId: number): void {
    this.taskService.toggleTask(taskId).subscribe({
      next: () => this.loadTasks()
    });
  }

  deleteTask(taskId: number): void {
    if(confirm("Supprimer cette tâche ?")) {
      this.taskService.deleteTask(taskId).subscribe({
        next: () => this.loadTasks()
      });
    }
  }
}