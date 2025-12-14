import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProjectService } from '../../services/project';
import { Router, RouterModule } from '@angular/router'; // <--- 1. AJOUTE RouterModule ICI

@Component({
  selector: 'app-project-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule], // <--- 2. ET AJOUTE-LE ICI
  templateUrl: './project-list.html',
  styleUrl: './project-list.css'
})
export class ProjectListComponent implements OnInit {
  projects: any[] = [];
  errorMessage = '';
  
  showForm = false;
  form: any = {
    title: '',
    description: ''
  };

  constructor(private projectService: ProjectService, private router: Router) { }

  ngOnInit(): void {
    this.fetchProjects();
  }

  fetchProjects(): void {
    this.projectService.getAllProjects().subscribe({
      next: (data: any) => {
        this.projects = data;
      },
      error: (err: any) => {
        this.errorMessage = "Impossible de charger les projets.";
        console.error(err);
      }
    });
  }

  onSubmit(): void {
    const { title, description } = this.form;
    
    this.projectService.createProject(title, description).subscribe({
      next: (data: any) => {
        console.log("Projet créé !", data);
        this.showForm = false;
        this.form = { title: '', description: '' };
        this.fetchProjects();
      },
      error: (err: any) => {
        this.errorMessage = "Erreur lors de la création du projet";
      }
    });
  }
}