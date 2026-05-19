package com.teamtask.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.teamtask.backend.dto.ProjectRequest;
import com.teamtask.backend.dto.ProjectResponse;
import com.teamtask.backend.entity.Project;
import com.teamtask.backend.entity.ProjectMember;
import com.teamtask.backend.entity.User;
import com.teamtask.backend.enums.Role;
import com.teamtask.backend.repository.ProjectMemberRepository;
import com.teamtask.backend.repository.ProjectRepository;
import com.teamtask.backend.repository.UserRepository;
import com.teamtask.backend.security.SecurityUtil;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;
    private final ProjectAccessService projectAccessService;

    public ProjectService(
            ProjectRepository projectRepository,
            ProjectMemberRepository projectMemberRepository,
            UserRepository userRepository,
            ProjectAccessService projectAccessService) {
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.userRepository = userRepository;
        this.projectAccessService = projectAccessService;
    }

    @Transactional
    public ProjectResponse createProject(ProjectRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setCreatedBy(creator);
        Project saved = projectRepository.save(project);

        ProjectMember membership = new ProjectMember();
        membership.setProject(saved);
        membership.setUser(creator);
        membership.setRole(Role.ADMIN);
        projectMemberRepository.save(membership);

        return toResponse(saved, Role.ADMIN);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> listMyProjects() {
        Long userId = SecurityUtil.getCurrentUserId();
        return projectRepository.findAllByMemberUserId(userId).stream()
                .map(project -> {
                    Role role = projectAccessService.getRole(project.getId(), userId);
                    return toResponse(project, role);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProject(Long projectId) {
        Long userId = SecurityUtil.getCurrentUserId();
        projectAccessService.requireMembership(projectId, userId);
        Project project = findProjectOrThrow(projectId);
        Role role = projectAccessService.getRole(projectId, userId);
        return toResponse(project, role);
    }

    @Transactional
    public ProjectResponse updateProject(Long projectId, ProjectRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        projectAccessService.requireAdmin(projectId, userId);

        Project project = findProjectOrThrow(projectId);
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        Project saved = projectRepository.save(project);
        return toResponse(saved, Role.ADMIN);
    }

    @Transactional
    public void deleteProject(Long projectId) {
        Long userId = SecurityUtil.getCurrentUserId();
        projectAccessService.requireAdmin(projectId, userId);
        projectRepository.delete(findProjectOrThrow(projectId));
    }

    private Project findProjectOrThrow(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
    }

    private ProjectResponse toResponse(Project project, Role currentUserRole) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedBy().getId(),
                project.getCreatedBy().getName(),
                currentUserRole,
                project.getCreatedAt(),
                project.getUpdatedAt());
    }
}
