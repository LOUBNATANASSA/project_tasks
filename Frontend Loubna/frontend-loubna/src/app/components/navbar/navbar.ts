import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class NavbarComponent {

  // On injecte le AuthService pour savoir si on est connecté
  constructor(public authService: AuthService, private router: Router) { }

  getUserName(): string {
    const user = this.authService.getUser();
    if (!user) return '';

    // Debug: log the user object to see what fields are available
    console.log('User object:', user);

    // Prioritize name/username fields over email
    return user.name || user.username || user.nom || user.prenom || user.firstName || user.lastName || user.displayName || 'User';
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}