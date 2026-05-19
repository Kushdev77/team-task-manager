import { TaskStatus } from './task-status.enum';
import { TaskResponse } from './task.model';

export interface DashboardResponse {
  projectId: number;
  projectName: string;
  totalTasks: number;
  statusCounts: Record<TaskStatus, number>;
  overdueCount: number;
  overdueTasks: TaskResponse[];
}

export interface UserDashboardResponse {
  totalProjects: number;
  totalTasks: number;
  totalOverdue: number;
  projectDashboards: DashboardResponse[];
}
