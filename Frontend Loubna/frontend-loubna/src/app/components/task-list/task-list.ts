import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { TaskService } from '../../services/task';
import { PopupService } from '../../services/popup.service';
import { ProjectService } from '../../services/project'; // 1. IMPORT

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './task-list.html',
  styleUrl: './task-list.css'
})
export class TaskListComponent implements OnInit {
  tasks: any[] = [];
  projectId: any;
  projectTitle: string = ''; // 2. VARIABLE FOR THE TITLE
  newTaskTitle = '';
  editingTaskId: number | null = null;
  editingTaskTitle = '';
  editingTaskDescription = '';
  editingTaskDueDate = '';

  constructor(
    private taskService: TaskService,
    private projectService: ProjectService,
    private route: ActivatedRoute,
    private popupService: PopupService
  ) { }

  ngOnInit(): void {
    this.projectId = this.route.snapshot.paramMap.get('id');
    this.loadProjectInfo();
    this.loadTasks();
  }

  loadProjectInfo(): void {
    this.projectService.getProjectById(this.projectId).subscribe({
      next: (project: any) => {
        this.projectTitle = project.title;
      },
      error: (err: any) => console.error('Error while retrieving project', err)
    });
  }

  loadTasks(): void {
    this.taskService.getTasksByProject(this.projectId).subscribe({
      next: (data: any) => this.tasks = data,
      error: (err: any) => console.error(err)
    });
  }

  addTask(): void {
    if (!this.newTaskTitle) return;
    const task = { title: this.newTaskTitle, description: '', projectId: this.projectId };
    this.taskService.createTask(task).subscribe({
      next: () => {
        this.newTaskTitle = '';
        this.loadTasks();
      }
    });
  }

  startEdit(task: any): void {
    this.editingTaskId = task.id;
    this.editingTaskTitle = task.title;
    this.editingTaskDescription = task.description;
    this.editingTaskDueDate = task.dueDate;
  }

  cancelEdit(): void {
    this.editingTaskId = null;
    this.editingTaskTitle = '';
    this.editingTaskDescription = '';
    this.editingTaskDueDate = '';
  }

  saveEdit(task: any): void {
    if (!this.editingTaskTitle.trim()) return;
    const updatedTask = {
      ...task,
      title: this.editingTaskTitle,
      description: this.editingTaskDescription,
      dueDate: this.editingTaskDueDate
    };
    this.taskService.updateTask(task.id, updatedTask).subscribe({
      next: () => {
        this.editingTaskId = null;
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
    this.popupService.confirm('Delete Task', 'Do you want to delete this task?').subscribe(res => {
      if (res.confirmed) {
        this.taskService.deleteTask(taskId).subscribe({ next: () => this.loadTasks() });
      }
    });
  }
}