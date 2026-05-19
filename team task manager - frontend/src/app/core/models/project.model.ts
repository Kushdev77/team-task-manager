import { Role } from './role.enum';

export interface ProjectResponse {
  id: number;
  name: string;
  description: string;
  createdById: number;
  createdByName: string;
  currentUserRole: Role;
  createdAt: string;
  updatedAt: string;
}

export interface ProjectRequest {
  name: string;
  description?: string;
}
