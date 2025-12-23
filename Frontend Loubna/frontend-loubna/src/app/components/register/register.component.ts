import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterModule],
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css']
})
export class RegisterComponent {
    form: any = {
        username: '',
        email: '',
        password: ''
    };
    isSuccessful = false;
    isSignUpFailed = false;
    errorMessage = '';

    constructor(
        private authService: AuthService,
        private router: Router
    ) { }

    onSubmit(): void {
        const { username, email, password } = this.form;

        this.authService.register(username, email, password).subscribe({
            next: (data) => {
                this.isSuccessful = true;
                this.isSignUpFailed = false;

                // Automatic login after registration
                this.authService.login(email, password).subscribe({
                    next: (loginData) => {
                        // Add the username to login data
                        const userData = { ...loginData, name: username };
                        this.authService.saveUser(userData);
                        this.router.navigate(['/projects']);
                    },
                    error: (err) => {
                        // If auto login fails, redirect to login
                        this.router.navigate(['/login']);
                    }
                });
            },
            error: (err) => {
                this.errorMessage = err.error.message || 'Error during registration';
                this.isSignUpFailed = true;
            }
        });
    }
}
