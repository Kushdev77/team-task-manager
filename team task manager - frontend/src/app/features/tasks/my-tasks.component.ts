import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TaskService } from '../../core/services/task.service';
import { extractApiError } from '../../core/utils/api-error';
import { TaskResponse } from '../../core/models';
import { TaskStatus } from '../../core/models/task-status.enum';

@Component({
  selector: 'app-my-tasks',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './my-tasks.component.html',
  styleUrl: './my-tasks.component.css'
})
export class MyTasksComponent implements OnInit {
  private readonly taskService = inject(TaskService);

  tasks = signal<TaskResponse[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);
  readonly TaskStatus = TaskStatus;

  ngOnInit(): void {
    this.taskService.listMine().subscribe({
      next: (t) => {
        this.tasks.set(t);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(extractApiError(err, 'Failed to load tasks'));
        this.loading.set(false);
      }
    });
  }

  updateStatus(task: TaskResponse, status: TaskStatus): void {
    this.taskService
      .update(task.id, {
        title: task.title,
        description: task.description,
        status,
        dueDate: task.dueDate,
        assigneeId: task.assigneeId
      })
      .subscribe({
        next: (updated) => {
          this.tasks.update((list) => list.map((t) => (t.id === updated.id ? updated : t)));
        },
        error: (err) => this.error.set(extractApiError(err, 'Update failed'))
      });
  }
}
