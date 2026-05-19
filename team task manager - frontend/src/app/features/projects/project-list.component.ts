import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProjectService } from '../../core/services/project.service';
import { extractApiError } from '../../core/utils/api-error';
import { ProjectResponse } from '../../core/models';
import { Role } from '../../core/models/role.enum';

@Component({
  selector: 'app-project-list',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './project-list.component.html',
  styleUrl: './project-list.component.css'
})
export class ProjectListComponent implements OnInit {
  private readonly projectService = inject(ProjectService);
  private readonly fb = inject(FormBuilder);

  projects = signal<ProjectResponse[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);
  showForm = signal(false);
  saving = signal(false);
  readonly Role = Role;

  form = this.fb.nonNullable.group({
    name: ['', Validators.required],
    description: ['']
  });

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.projectService.list().subscribe({
      next: (list) => {
        this.projects.set(list);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(extractApiError(err, 'Failed to load projects'));
        this.loading.set(false);
      }
    });
  }

  toggleForm(): void {
    this.showForm.update((v) => !v);
    this.form.reset({ name: '', description: '' });
  }

  create(): void {
    if (this.form.invalid) return;
    this.saving.set(true);
    this.projectService.create(this.form.getRawValue()).subscribe({
      next: () => {
        this.saving.set(false);
        this.showForm.set(false);
        this.form.reset({ name: '', description: '' });
        this.load();
      },
      error: (err) => {
        this.saving.set(false);
        this.error.set(extractApiError(err, 'Failed to create project'));
      }
    });
  }

  deleteProject(id: number, event: Event): void {
    event.preventDefault();
    event.stopPropagation();
    if (!confirm('Delete this project?')) return;
    this.projectService.delete(id).subscribe({
      next: () => this.load(),
      error: (err) => this.error.set(extractApiError(err, 'Delete failed'))
    });
  }
}
