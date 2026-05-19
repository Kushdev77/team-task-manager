import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProjectService } from '../../core/services/project.service';
import { MemberService } from '../../core/services/member.service';
import { TaskService } from '../../core/services/task.service';
import { DashboardService } from '../../core/services/dashboard.service';
import { AuthService } from '../../core/services/auth.service';
import { extractApiError } from '../../core/utils/api-error';
import {
  DashboardResponse,
  MemberResponse,
  ProjectResponse,
  TaskResponse
} from '../../core/models';
import { Role } from '../../core/models/role.enum';
import { TaskStatus } from '../../core/models/task-status.enum';

@Component({
  selector: 'app-project-detail',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './project-detail.component.html',
  styleUrl: './project-detail.component.css'
})
export class ProjectDetailComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly fb = inject(FormBuilder);
  private readonly projectService = inject(ProjectService);
  private readonly memberService = inject(MemberService);
  private readonly taskService = inject(TaskService);
  private readonly dashboardService = inject(DashboardService);
  readonly auth = inject(AuthService);

  project = signal<ProjectResponse | null>(null);
  members = signal<MemberResponse[]>([]);
  tasks = signal<TaskResponse[]>([]);
  dashboard = signal<DashboardResponse | null>(null);
  activeTab = signal<'tasks' | 'members' | 'overview'>('tasks');
  loading = signal(true);
  error = signal<string | null>(null);
  saving = signal(false);
  showEditProject = signal(false);

  readonly Role = Role;
  readonly TaskStatus = TaskStatus;
  readonly statusList = [TaskStatus.TODO, TaskStatus.IN_PROGRESS, TaskStatus.DONE];

  memberForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    role: [Role.MEMBER as Role, Validators.required]
  });

  taskForm = this.fb.nonNullable.group({
    title: ['', Validators.required],
    description: [''],
    status: [TaskStatus.TODO as TaskStatus],
    dueDate: [''],
    assigneeId: [null as number | null]
  });

  editProjectForm = this.fb.nonNullable.group({
    name: ['', Validators.required],
    description: ['']
  });

  projectId = 0;

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      this.projectId = Number(params.get('id'));
      this.loadAll();
    });
  }

  get isAdmin(): boolean {
    return this.project()?.currentUserRole === Role.ADMIN;
  }

  setTab(tab: 'tasks' | 'members' | 'overview'): void {
    this.activeTab.set(tab);
    this.error.set(null);
  }

  loadAll(): void {
    this.loading.set(true);
    this.error.set(null);
    this.projectService.get(this.projectId).subscribe({
      next: (p) => {
        this.project.set(p);
        this.editProjectForm.patchValue({ name: p.name, description: p.description ?? '' });
        this.loadMembers();
        this.loadTasks();
        this.loadDashboard();
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(extractApiError(err, 'Project not found'));
        this.loading.set(false);
      }
    });
  }

  loadMembers(): void {
    this.memberService.list(this.projectId).subscribe({
      next: (m) => this.members.set(m),
      error: () => {}
    });
  }

  loadTasks(): void {
    this.taskService.listByProject(this.projectId).subscribe({
      next: (t) => this.tasks.set(t),
      error: () => {}
    });
  }

  loadDashboard(): void {
    this.dashboardService.getByProject(this.projectId).subscribe({
      next: (d) => this.dashboard.set(d),
      error: () => {}
    });
  }

  toggleEditProject(): void {
    this.showEditProject.update((v) => !v);
  }

  saveProject(): void {
    if (this.editProjectForm.invalid || !this.isAdmin) return;
    this.saving.set(true);
    this.projectService.update(this.projectId, this.editProjectForm.getRawValue()).subscribe({
      next: (p) => {
        this.project.set(p);
        this.saving.set(false);
        this.showEditProject.set(false);
      },
      error: (err) => {
        this.saving.set(false);
        this.error.set(extractApiError(err, 'Failed to update project'));
      }
    });
  }

  addMember(): void {
    if (this.memberForm.invalid || !this.isAdmin) return;
    this.saving.set(true);
    this.error.set(null);
    this.memberService.add(this.projectId, this.memberForm.getRawValue()).subscribe({
      next: () => {
        this.saving.set(false);
        this.memberForm.reset({ email: '', role: Role.MEMBER });
        this.loadMembers();
      },
      error: (err) => {
        this.saving.set(false);
        this.error.set(extractApiError(err, 'Failed to add member'));
      }
    });
  }

  removeMember(memberId: number): void {
    if (!confirm('Remove this member?')) return;
    this.memberService.remove(this.projectId, memberId).subscribe({
      next: () => this.loadMembers(),
      error: (err) => this.error.set(extractApiError(err, 'Failed to remove member'))
    });
  }

  createTask(): void {
    if (this.taskForm.invalid) return;
    const raw = this.taskForm.getRawValue();
    const body = {
      title: raw.title,
      description: raw.description || undefined,
      status: raw.status,
      dueDate: raw.dueDate || null,
      assigneeId: this.isAdmin && raw.assigneeId ? raw.assigneeId : undefined
    };
    this.saving.set(true);
    this.error.set(null);
    this.taskService.create(this.projectId, body).subscribe({
      next: () => {
        this.saving.set(false);
        this.taskForm.reset({
          title: '',
          description: '',
          status: TaskStatus.TODO,
          dueDate: '',
          assigneeId: null
        });
        this.loadTasks();
        this.loadDashboard();
      },
      error: (err) => {
        this.saving.set(false);
        this.error.set(extractApiError(err, 'Failed to create task'));
      }
    });
  }

  canEditTask(task: TaskResponse): boolean {
    if (this.isAdmin) return true;
    const userId = this.auth.user()?.id;
    return userId != null && task.assigneeId === userId;
  }

  updateTaskStatus(task: TaskResponse, status: TaskStatus): void {
    if (!this.canEditTask(task)) {
      this.error.set('You can only update tasks assigned to you');
      this.loadTasks();
      return;
    }
    this.taskService
      .update(task.id, {
        title: task.title,
        description: task.description ?? '',
        status,
        dueDate: task.dueDate,
        assigneeId: task.assigneeId
      })
      .subscribe({
        next: () => {
          this.error.set(null);
          this.loadTasks();
          this.loadDashboard();
        },
        error: (err) => {
          this.error.set(extractApiError(err, 'Cannot update task'));
          this.loadTasks();
        }
      });
  }

  deleteTask(taskId: number): void {
    if (!confirm('Delete this task?')) return;
    this.taskService.delete(taskId).subscribe({
      next: () => {
        this.loadTasks();
        this.loadDashboard();
      },
      error: (err) => this.error.set(extractApiError(err, 'Delete failed'))
    });
  }

  tasksByStatus(status: TaskStatus): TaskResponse[] {
    return this.tasks().filter((t) => t.status === status);
  }

  statusCount(counts: Record<TaskStatus, number> | undefined, key: TaskStatus): number {
    if (!counts) return 0;
    return counts[key] ?? 0;
  }
}
