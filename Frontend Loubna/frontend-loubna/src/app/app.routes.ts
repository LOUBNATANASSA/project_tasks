import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { ProjectListComponent } from './components/project-list/project-list';
// CORRECTION : On pointe vers 'task-list' (sans .component)
import { TaskListComponent } from './components/task-list/task-list'; 

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'projects', component: ProjectListComponent },
    { path: 'projects/:id/tasks', component: TaskListComponent }, 
    { path: '', redirectTo: 'login', pathMatch: 'full' }
];