import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProjectService } from '../../services/project';

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

  // id du projet en cours d'édition (null = création)
  editingId: number | null = null;

  constructor(private projectService: ProjectService) { }

  ngOnInit(): void {
    this.loadProjects();
  }

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

  onSubmit(): void {
    if (!this.form.title || !this.form.description) return;

    if (this.editingId == null) {
      // création
      this.projectService.createProject(this.form.title, this.form.description).subscribe({
        next: () => {
          this.form = { title: '', description: '' };
          this.showForm = false;
          this.loadProjects();
        },
        error: (err) => { console.error(err); this.errorMessage = 'Erreur lors de la création.'; }
      });
    } else {
      // mise à jour
      this.projectService.updateProject(this.editingId, this.form.title, this.form.description).subscribe({
        next: () => {
          this.form = { title: '', description: '' };
          this.showForm = false;
          this.editingId = null;
          this.loadProjects();
        },
        error: (err) => { console.error(err); this.errorMessage = 'Erreur lors de la mise à jour.'; }
      });
    }
  }

  deleteProject(id: number): void {
    if (!confirm('Supprimer ce projet ?')) return;
    this.projectService.deleteProject(id).subscribe({
      next: () => this.loadProjects(),
      error: (err) => { console.error(err); this.errorMessage = 'Erreur lors de la suppression.'; }
    });
  }

  // Passe le formulaire en mode édition
  editProject(project: any): void {
    this.showForm = true;
    this.editingId = project.id;
    this.form = {
      title: project.title || '',
      description: project.description || ''
    };
  }

  // Annuler l'édition / réinitialiser le formulaire
  cancelEdit(): void {
    this.showForm = false;
    this.editingId = null;
    this.form = { title: '', description: '' };
  }

  // Méthode pour basculer l'affichage du formulaire et annuler l'édition si on ferme
  toggleForm(): void {
    // on inverse l'état
    this.showForm = !this.showForm;
    // si on vient de fermer le formulaire, on annule l'édition
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