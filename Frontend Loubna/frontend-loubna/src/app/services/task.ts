import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth';

const API_URL = 'http://localhost:8081/api/tasks';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  constructor(private http: HttpClient, private authService: AuthService) { }

  private getHeaders(): HttpHeaders {
    const user = this.authService.getUser();
    const token = user ? (user.token || user.accessToken) : '';
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  getTasksByProject(projectId: number): Observable<any> {
    return this.http.get(`${API_URL}/project/${projectId}`, { headers: this.getHeaders() });
  }

  createTask(task: any): Observable<any> {
    return this.http.post(API_URL, task, { headers: this.getHeaders() });
  }

  toggleTask(taskId: number): Observable<any> {
    return this.http.put(`${API_URL}/${taskId}/toggle`, {}, { headers: this.getHeaders() });
  }

  // Nouvelle méthode pour la modification
  updateTask(taskId: number, task: any): Observable<any> {
    return this.http.put(`${API_URL}/${taskId}`, task, { headers: this.getHeaders() });
  }

  deleteTask(taskId: number): Observable<any> {
    return this.http.delete(`${API_URL}/${taskId}`, { headers: this.getHeaders() });
  }
}