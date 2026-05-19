package com.teamtask.backend.service;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamtask.backend.dto.DashboardResponse;
import com.teamtask.backend.dto.TaskResponse;
import com.teamtask.backend.dto.UserDashboardResponse;
import com.teamtask.backend.entity.Project;
import com.teamtask.backend.entity.Task;
import com.teamtask.backend.enums.TaskStatus;
import com.teamtask.backend.repository.ProjectRepository;
import com.teamtask.backend.repository.TaskRepository;
import com.teamtask.backend.security.SecurityUtil;

@Service
public class DashboardService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ProjectAccessService projectAccessService;
    private final TaskService taskService;

    public DashboardService(
            ProjectRepository projectRepository,
            TaskRepository taskRepository,
            ProjectAccessService projectAccessService,
            TaskService taskService) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.projectAccessService = projectAccessService;
        this.taskService = taskService;
    }

    @Transactional(readOnly = true)
    public DashboardResponse getProjectDashboard(Long projectId) {
        Long userId = SecurityUtil.getCurrentUserId();
        projectAccessService.requireMembership(projectId, userId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Project not found"));

        List<Task> tasks = taskRepository.findByProjectId(projectId);
        Map<TaskStatus, Long> statusCounts = buildStatusCounts(tasks);
        List<TaskResponse> overdueTasks = tasks.stream()
                .filter(TaskService::isOverdue)
                .map(taskService::toResponse)
                .toList();

        return new DashboardResponse(
                project.getId(),
                project.getName(),
                tasks.size(),
                statusCounts,
                overdueTasks.size(),
                overdueTasks);
    }

    @Transactional(readOnly = true)
    public UserDashboardResponse getMyDashboard() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<Project> projects = projectRepository.findAllByMemberUserId(userId);

        List<DashboardResponse> projectDashboards = projects.stream()
                .map(p -> buildDashboardForProject(p.getId()))
                .toList();

        long totalTasks = projectDashboards.stream().mapToLong(DashboardResponse::totalTasks).sum();
        long totalOverdue = projectDashboards.stream().mapToLong(DashboardResponse::overdueCount).sum();

        return new UserDashboardResponse(projects.size(), totalTasks, totalOverdue, projectDashboards);
    }

    private DashboardResponse buildDashboardForProject(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        Map<TaskStatus, Long> statusCounts = buildStatusCounts(tasks);
        List<TaskResponse> overdueTasks = tasks.stream()
                .filter(TaskService::isOverdue)
                .map(taskService::toResponse)
                .toList();

        return new DashboardResponse(
                project.getId(),
                project.getName(),
                tasks.size(),
                statusCounts,
                overdueTasks.size(),
                overdueTasks);
    }

    private Map<TaskStatus, Long> buildStatusCounts(List<Task> tasks) {
        Map<TaskStatus, Long> counts = new EnumMap<>(TaskStatus.class);
        for (TaskStatus status : TaskStatus.values()) {
            counts.put(status, 0L);
        }
        for (Task task : tasks) {
            counts.merge(task.getStatus(), 1L, Long::sum);
        }
        return counts;
    }
}
