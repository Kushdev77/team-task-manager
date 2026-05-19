import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DashboardService } from '../../core/services/dashboard.service';
import { extractApiError } from '../../core/utils/api-error';
import { UserDashboardResponse } from '../../core/models';
import { TaskStatus } from '../../core/models/task-status.enum';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  private readonly dashboardService = inject(DashboardService);

  data = signal<UserDashboardResponse | null>(null);
  loading = signal(true);
  error = signal<string | null>(null);
  readonly TaskStatus = TaskStatus;
  readonly statusList = [TaskStatus.TODO, TaskStatus.IN_PROGRESS, TaskStatus.DONE];

  ngOnInit(): void {
    this.dashboardService.getMine().subscribe({
      next: (res) => {
        this.data.set(res);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(extractApiError(err, 'Failed to load dashboard'));
        this.loading.set(false);
      }
    });
  }

  statusCount(counts: Record<TaskStatus, number> | undefined, key: TaskStatus): number {
    if (!counts) return 0;
    return counts[key] ?? 0;
  }
}
