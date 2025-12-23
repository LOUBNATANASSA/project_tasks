import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProjectService } from '../../services/project';
import { TaskService } from '../../services/task';

@Component({
  selector: 'app-project-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './project-list.html',
  styleUrls: ['./project-list.css']
})
export class ProjectListComponent implements OnInit {
  projects: any[] = [];
  errorMessage = '';
  showForm = false;
  form: any = { title: '', description: '' };
  searchTerm = '';

  // Stats storage: projectId -> { total, completed, completedNames }
  projectStats: { [key: number]: { total: number, completed: number, completedNames: string[] } } = {};

  // ID of the project being edited (null = creation)
  editingId: number | null = null;

  constructor(
    private projectService: ProjectService,
    private taskService: TaskService
  ) { }

  ngOnInit(): void {
    this.loadProjects();
  }

  loadProjects(): void {
    this.projectService.getAllProjects().subscribe({
      next: (data: any) => {
        this.projects = data || [];
        this.loadAllProjectStats();
      },
      error: (err: any) => {
        console.error(err);
        this.errorMessage = 'Unable to load projects.';
      }
    });
  }

  loadAllProjectStats(): void {
    this.projects.forEach(project => {
      console.log('Loading stats for project:', project.id);
      this.taskService.getTasksByProject(project.id).subscribe({
        next: (tasks: any[]) => {
          const total = tasks.length;
          const completedTasks = tasks.filter(t => t.isCompleted);
          const completed = completedTasks.length;
          const completedNames = completedTasks.map(t => t.title);

          console.log(`Stats for ${project.id}: ${completed}/${total}`);
          this.projectStats[project.id] = { total, completed, completedNames };
        },
        error: (err) => console.error(`Error loading stats for project ${project.id}`, err)
      });
    });
  }

  onSubmit(): void {
    if (!this.form.title || !this.form.description) return;

    if (this.editingId == null) {
      // creation
      this.projectService.createProject(this.form.title, this.form.description).subscribe({
        next: () => {
          this.form = { title: '', description: '' };
          this.showForm = false;
          this.loadProjects();
        },
        error: (err: any) => { console.error(err); this.errorMessage = 'Error during creation.'; }
      });
    } else {
      // update
      this.projectService.updateProject(this.editingId, this.form.title, this.form.description).subscribe({
        next: () => {
          this.form = { title: '', description: '' };
          this.showForm = false;
          this.editingId = null;
          this.loadProjects();
        },
        error: (err: any) => { console.error(err); this.errorMessage = 'Error during update.'; }
      });
    }
  }

  deleteProject(id: number): void {
    if (!confirm('Delete this project?')) return;
    this.projectService.deleteProject(id).subscribe({
      next: () => this.loadProjects(),
      error: (err: any) => { console.error(err); this.errorMessage = 'Error during deletion.'; }
    });
  }

  // Switch the form to edit mode
  editProject(project: any): void {
    this.showForm = true;
    this.editingId = project.id;
    this.form = {
      title: project.title || '',
      description: project.description || ''
    };
  }

  // Cancel editing / reset the form
  cancelEdit(): void {
    this.showForm = false;
    this.editingId = null;
    this.form = { title: '', description: '' };
  }

  // Method to toggle form display and cancel editing if closing
  toggleForm(): void {
    // on inverse l'état
    this.showForm = !this.showForm;
    // if we just closed the form, cancel editing
    if (!this.showForm) {
      this.cancelEdit();
    }
  }

  get filteredProjects(): any[] {
    const q = (this.searchTerm || '').trim().toLowerCase();
    if (!q) return this.projects;
    return this.projects.filter(p => {
      const title = (p.title || '').toString().toLowerCase();
      const desc = (p.description || '').toString().toLowerCase();
      return title.includes(q) || desc.includes(q);
    });
  }

  clearSearch(): void { this.searchTerm = ''; }
}