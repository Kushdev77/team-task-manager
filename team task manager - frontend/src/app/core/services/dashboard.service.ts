import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { DashboardResponse, UserDashboardResponse } from '../models';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly api = `${environment.apiUrl}/dashboard`;

  constructor(private http: HttpClient) {}

  getMine() {
    return this.http.get<UserDashboardResponse>(this.api);
  }

  getByProject(projectId: number) {
    return this.http.get<DashboardResponse>(`${this.api}/projects/${projectId}`);
  }
}
