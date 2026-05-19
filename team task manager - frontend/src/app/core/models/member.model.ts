import { Role } from './role.enum';

export interface MemberResponse {
  id: number;
  userId: number;
  userName: string;
  userEmail: string;
  role: Role;
}

export interface AddMemberRequest {
  email: string;
  role: Role;
}

export interface UpdateMemberRoleRequest {
  role: Role;
}
