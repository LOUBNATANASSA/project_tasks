// Ajoutez l'import RouterModule
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router'; // <-- AJOUTÉ
import { ProjectService } from '../../services/project';

@Component({
  selector: 'app-project-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule], // <-- RouterModule ajouté ici
  templateUrl: './project-list.html',
  styleUrls: ['./project-list.css']
})
export class ProjectListComponent implements OnInit {
  projects: any[] = [];
  errorMessage = '';
  showForm = false;
  form: any = { title: '', description: '' };
  searchTerm = '';

  constructor(private projectService: ProjectService) { }

  ngOnInit(): void {
    this.loadProjects();
  }

  // Utiliser getAllProjects() (nom existant dans ProjectService)
  loadProjects(): void {
    this.projectService.getAllProjects().subscribe({
      next: (data: any) => {
        this.projects = data || [];
      },
      error: (err: any) => {
        console.error(err);
        this.errorMessage = 'Impossible de charger les projets.';
      }
    });
  }

  // Adapter l'appel à createProject pour passer title et description séparément
  onSubmit(): void {
    if (!this.form.title || !this.form.description) return;
    this.projectService.createProject(this.form.title, this.form.description).subscribe({
      next: () => {
        this.form = { title: '', description: '' };
        this.showForm = false;
        this.loadProjects();
      },
      error: (err) => { console.error(err); this.errorMessage = 'Erreur lors de la création.'; }
    });
  }

  // Appelle deleteProject du service (méthode à ajouter si manquante)
  deleteProject(id: number): void {
    if (!confirm('Supprimer ce projet ?')) return;
    this.projectService.deleteProject(id).subscribe({
      next: () => this.loadProjects(),
      error: (err) => { console.error(err); this.errorMessage = 'Erreur lors de la suppression.'; }
    });
  }

  // Getter de filtrage (si vous l'utilisez)
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