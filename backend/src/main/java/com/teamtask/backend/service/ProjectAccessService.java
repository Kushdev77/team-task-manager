package com.teamtask.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.teamtask.backend.entity.ProjectMember;
import com.teamtask.backend.enums.Role;
import com.teamtask.backend.repository.ProjectMemberRepository;

@Service
public class ProjectAccessService {

    private final ProjectMemberRepository projectMemberRepository;

    public ProjectAccessService(ProjectMemberRepository projectMemberRepository) {
        this.projectMemberRepository = projectMemberRepository;
    }

    public ProjectMember requireMembership(Long projectId, Long userId) {
        return projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Not a member of this project"));
    }

    public ProjectMember requireAdmin(Long projectId, Long userId) {
        ProjectMember membership = requireMembership(projectId, userId);
        if (membership.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin access required");
        }
        return membership;
    }

    public Role getRole(Long projectId, Long userId) {
        return projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .map(ProjectMember::getRole)
                .orElse(null);
    }
}
