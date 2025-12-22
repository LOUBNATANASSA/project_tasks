import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent implements OnInit {
  form: any = {
    email: '',
    password: ''
  };
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    // 1. SI DÉJÀ CONNECTÉ -> HOP, ON REDIRIGE DIRECT
    if (this.authService.isLoggedIn()) {
      this.isLoggedIn = true;
      this.router.navigate(['/projects']);
    }
  }

  onSubmit(): void {
    const { email, password } = this.form;

    this.authService.login(email, password).subscribe({
      next: data => {
        this.authService.saveUser(data);
        this.isLoginFailed = false;
        this.isLoggedIn = true;

        // 2. CONNEXION RÉUSSIE -> HOP, ON REDIRIGE
        this.router.navigate(['/projects']);
      },
      error: err => {
        this.errorMessage = err.error.message || "Échec de la connexion";
        this.isLoginFailed = true;
      }
    });
  }
}