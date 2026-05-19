import { TaskStatus } from './task-status.enum';

export interface TaskResponse {
  id: number;
  title: string;
  description: string;
  status: TaskStatus;
  dueDate: string | null;
  projectId: number;
  assigneeId: number | null;
  assigneeName: string | null;
  createdById: number;
  createdByName: string;
  createdAt: string;
  updatedAt: string;
  overdue: boolean;
}

export interface TaskRequest {
  title: string;
  description?: string;
  status?: TaskStatus;
  dueDate?: string | null;
  assigneeId?: number | null;
}
