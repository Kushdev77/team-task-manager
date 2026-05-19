import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { AddMemberRequest, MemberResponse, UpdateMemberRoleRequest } from '../models';

@Injectable({ providedIn: 'root' })
export class MemberService {
  constructor(private http: HttpClient) {}

  list(projectId: number) {
    return this.http.get<MemberResponse[]>(
      `${environment.apiUrl}/projects/${projectId}/members`
    );
  }

  add(projectId: number, request: AddMemberRequest) {
    return this.http.post<MemberResponse>(
      `${environment.apiUrl}/projects/${projectId}/members`,
      request
    );
  }

  updateRole(projectId: number, memberId: number, request: UpdateMemberRoleRequest) {
    return this.http.put<MemberResponse>(
      `${environment.apiUrl}/projects/${projectId}/members/${memberId}`,
      request
    );
  }

  remove(projectId: number, memberId: number) {
    return this.http.delete<void>(
      `${environment.apiUrl}/projects/${projectId}/members/${memberId}`
    );
  }
}
