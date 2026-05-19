package com.teamtask.backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.teamtask.backend.dto.TaskRequest;
import com.teamtask.backend.dto.TaskResponse;
import com.teamtask.backend.entity.Project;
import com.teamtask.backend.entity.ProjectMember;
import com.teamtask.backend.entity.Task;
import com.teamtask.backend.entity.User;
import com.teamtask.backend.enums.Role;
import com.teamtask.backend.enums.TaskStatus;
import com.teamtask.backend.repository.ProjectRepository;
import com.teamtask.backend.repository.TaskRepository;
import com.teamtask.backend.repository.UserRepository;
import com.teamtask.backend.security.SecurityUtil;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectAccessService projectAccessService;

    public TaskService(
            TaskRepository taskRepository,
            ProjectRepository projectRepository,
            UserRepository userRepository,
            ProjectAccessService projectAccessService) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectAccessService = projectAccessService;
    }

    @Transactional
    public TaskResponse createTask(Long projectId, TaskRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        projectAccessService.requireMembership(projectId, userId);

        Project project = findProjectOrThrow(projectId);
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO);
        task.setDueDate(request.getDueDate());
        task.setProject(project);
        task.setCreatedBy(creator);

        if (request.getAssigneeId() != null) {
            ProjectMember membership = projectAccessService.requireMembership(projectId, userId);
            if (membership.getRole() != Role.ADMIN) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can assign tasks");
            }
            task.setAssignee(resolveAssignee(projectId, request.getAssigneeId()));
        }

        return toResponse(taskRepository.save(task));
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> listProjectTasks(Long projectId) {
        Long userId = SecurityUtil.getCurrentUserId();
        projectAccessService.requireMembership(projectId, userId);
        return taskRepository.findByProjectId(projectId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> listMyTasks() {
        Long userId = SecurityUtil.getCurrentUserId();
        return taskRepository.findByAssigneeId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getTask(Long taskId) {
        Task task = findTaskOrThrow(taskId);
        projectAccessService.requireMembership(task.getProject().getId(), SecurityUtil.getCurrentUserId());
        return toResponse(task);
    }

    @Transactional
    public TaskResponse updateTask(Long taskId, TaskRequest request) {
        Task task = findTaskOrThrow(taskId);
        Long userId = SecurityUtil.getCurrentUserId();
        ProjectMember membership = projectAccessService.requireMembership(task.getProject().getId(), userId);

        assertCanModifyTask(task, membership, userId);

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        task.setDueDate(request.getDueDate());

        if (request.getAssigneeId() != null) {
            Long currentAssigneeId = task.getAssignee() != null ? task.getAssignee().getId() : null;
            if (!request.getAssigneeId().equals(currentAssigneeId)) {
                if (membership.getRole() != Role.ADMIN) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can change assignee");
                }
                task.setAssignee(resolveAssignee(task.getProject().getId(), request.getAssigneeId()));
            }
        }

        return toResponse(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(Long taskId) {
        Task task = findTaskOrThrow(taskId);
        Long userId = SecurityUtil.getCurrentUserId();
        projectAccessService.requireAdmin(task.getProject().getId(), userId);
        taskRepository.delete(task);
    }

    private void assertCanModifyTask(Task task, ProjectMember membership, Long userId) {
        if (membership.getRole() == Role.ADMIN) {
            return;
        }
        if (task.getAssignee() != null && task.getAssignee().getId().equals(userId)) {
            return;
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Members can only update tasks assigned to them");
    }

    private User resolveAssignee(Long projectId, Long assigneeId) {
        projectAccessService.requireMembership(projectId, assigneeId);
        return userRepository.findById(assigneeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignee not found"));
    }

    private Project findProjectOrThrow(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
    }

    private Task findTaskOrThrow(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    TaskResponse toResponse(Task task) {
        boolean overdue = isOverdue(task);
        String assigneeName = task.getAssignee() != null ? task.getAssignee().getName() : null;
        Long assigneeId = task.getAssignee() != null ? task.getAssignee().getId() : null;
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDueDate(),
                task.getProject().getId(),
                assigneeId,
                assigneeName,
                task.getCreatedBy().getId(),
                task.getCreatedBy().getName(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                overdue);
    }

    static boolean isOverdue(Task task) {
        return task.getDueDate() != null
                && task.getStatus() != TaskStatus.DONE
                && task.getDueDate().isBefore(LocalDate.now());
    }
}
