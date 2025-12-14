import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth'; // Cela va chercher auth.ts
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  // CORRECTION ICI : On pointe vers login.html et login.css (sans .component)
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  form: any = {
    email: '',
    password: ''
  };
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.isLoggedIn = true;
    }
  }

  onSubmit(): void {
    const { email, password } = this.form;

    this.authService.login(email, password).subscribe({
      next: data => {
        this.authService.saveUser(data);
        this.isLoginFailed = false;
        this.isLoggedIn = true;
        console.log("Login Success!", data);
      },
      error: err => {
        this.errorMessage = err.error.message || "Échec de la connexion";
        this.isLoginFailed = true;
      }
    });
  }
}