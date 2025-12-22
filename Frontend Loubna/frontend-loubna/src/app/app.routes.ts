import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { ProjectListComponent } from './components/project-list/project-list';
import { TaskListComponent } from './components/task-list/task-list';
import { LandingComponent } from './components/landing/landing.component';
import { RegisterComponent } from './components/register/register.component';

export const routes: Routes = [
    { path: '', component: LandingComponent }, // Page d'accueil avec Login et Register
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'landing', component: LandingComponent },
    { path: 'projects', component: ProjectListComponent },
    { path: 'projects/:id/tasks', component: TaskListComponent },
    { path: '**', redirectTo: '' } // Redirect unknown paths to login
];