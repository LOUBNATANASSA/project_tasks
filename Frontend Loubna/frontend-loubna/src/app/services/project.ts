import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth'; // On importe auth.ts

const API_URL = 'http://localhost:8081/api/projects';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  constructor(private http: HttpClient, private authService: AuthService) { }

  private getHeaders(): HttpHeaders {
    const user = this.authService.getUser();
    // Vérifie bien si ton backend renvoie 'accessToken' ou 'token' dans la réponse login
    const token = user ? (user.token || user.accessToken) : '';
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  getAllProjects(): Observable<any> {
    return this.http.get(API_URL, { headers: this.getHeaders() });
  }

  createProject(title: string, description: string): Observable<any> {
    return this.http.post(API_URL, {
      title,
      description
    }, { headers: this.getHeaders() });
  }

  updateProject(id: number, title: string, description: string): Observable<any> {
    return this.http.put(`${API_URL}/${id}`, {
      title,
      description
    }, { headers: this.getHeaders() });
  }

  deleteProject(id: number): Observable<any> {
    return this.http.delete(`${API_URL}/${id}`, { headers: this.getHeaders() });
  }

  // ... à l'intérieur de la classe ProjectService ...
  getProjectById(id: number): Observable<any> {
    return this.http.get(`${API_URL}/${id}`, { headers: this.getHeaders() });
  }

}