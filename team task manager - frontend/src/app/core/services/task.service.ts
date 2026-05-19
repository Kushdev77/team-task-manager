import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { TaskRequest, TaskResponse } from '../models';

@Injectable({ providedIn: 'root' })
export class TaskService {
  constructor(private http: HttpClient) {}

  listByProject(projectId: number) {
    return this.http.get<TaskResponse[]>(
      `${environment.apiUrl}/projects/${projectId}/tasks`
    );
  }

  listMine() {
    return this.http.get<TaskResponse[]>(`${environment.apiUrl}/tasks/mine`);
  }

  get(taskId: number) {
    return this.http.get<TaskResponse>(`${environment.apiUrl}/tasks/${taskId}`);
  }

  create(projectId: number, request: TaskRequest) {
    return this.http.post<TaskResponse>(
      `${environment.apiUrl}/projects/${projectId}/tasks`,
      request
    );
  }

  update(taskId: number, request: TaskRequest) {
    return this.http.put<TaskResponse>(`${environment.apiUrl}/tasks/${taskId}`, request);
  }

  delete(taskId: number) {
    return this.http.delete<void>(`${environment.apiUrl}/tasks/${taskId}`);
  }
}
