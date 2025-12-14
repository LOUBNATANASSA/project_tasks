import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

// Vérifie que ce port est bien celui de ton backend (ici 8081)
const AUTH_API = 'http://localhost:8081/api/auth/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  login(email: string, password: string): Observable<any> {
    return this.http.post(AUTH_API + 'signin', {
      email,
      password
    }, httpOptions);
  }

  register(name: string, email: string, password: string): Observable<any> {
    return this.http.post(AUTH_API + 'signup', {
      name,
      email,
      password
    }, httpOptions);
  }

  saveUser(user: any): void {
    if(typeof window !== 'undefined') {
        window.sessionStorage.setItem('auth-user', JSON.stringify(user));
    }
  }

  getUser(): any {
    if(typeof window !== 'undefined') {
        const user = window.sessionStorage.getItem('auth-user');
        if (user) {
            return JSON.parse(user);
        }
    }
    return null;
  }
  
  isLoggedIn(): boolean {
      return this.getUser() !== null;
  }

  logout(): void {
    if(typeof window !== 'undefined') {
        window.sessionStorage.clear();
    }
  }
}