import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { ProjectRequest, ProjectResponse } from '../models';

@Injectable({ providedIn: 'root' })
export class ProjectService {
  private readonly api = `${environment.apiUrl}/projects`;

  constructor(private http: HttpClient) {}

  list() {
    return this.http.get<ProjectResponse[]>(this.api);
  }

  get(id: number) {
    return this.http.get<ProjectResponse>(`${this.api}/${id}`);
  }

  create(request: ProjectRequest) {
    return this.http.post<ProjectResponse>(this.api, request);
  }

  update(id: number, request: ProjectRequest) {
    return this.http.put<ProjectResponse>(`${this.api}/${id}`, request);
  }

  delete(id: number) {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
}
