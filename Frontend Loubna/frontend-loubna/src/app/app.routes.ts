import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login'; // Note le chemin sans .component

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: '', redirectTo: 'login', pathMatch: 'full' }
];