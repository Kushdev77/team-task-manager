package com.teamtask.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.teamtask.backend.dto.AddMemberRequest;
import com.teamtask.backend.dto.MemberResponse;
import com.teamtask.backend.dto.UpdateMemberRoleRequest;
import com.teamtask.backend.entity.Project;
import com.teamtask.backend.entity.ProjectMember;
import com.teamtask.backend.entity.User;
import com.teamtask.backend.enums.Role;
import com.teamtask.backend.repository.ProjectMemberRepository;
import com.teamtask.backend.repository.ProjectRepository;
import com.teamtask.backend.repository.UserRepository;
import com.teamtask.backend.security.SecurityUtil;

@Service
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectAccessService projectAccessService;

    public ProjectMemberService(
            ProjectMemberRepository projectMemberRepository,
            ProjectRepository projectRepository,
            UserRepository userRepository,
            ProjectAccessService projectAccessService) {
        this.projectMemberRepository = projectMemberRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectAccessService = projectAccessService;
    }

    @Transactional
    public MemberResponse addMember(Long projectId, AddMemberRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        projectAccessService.requireAdmin(projectId, userId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        String email = request.getEmail() == null ? "" : request.getEmail().trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with this email"));

        if (projectMemberRepository.existsByProjectIdAndUserId(projectId, user.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already a member");
        }

        if (request.getRole() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role is required");
        }

        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(user);
        member.setRole(request.getRole());
        ProjectMember saved = projectMemberRepository.save(member);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> listMembers(Long projectId) {
        Long userId = SecurityUtil.getCurrentUserId();
        projectAccessService.requireMembership(projectId, userId);

        return projectMemberRepository.findByProjectId(projectId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        projectAccessService.requireAdmin(projectId, userId);

        ProjectMember member = projectMemberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        if (!member.getProject().getId().equals(projectId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member does not belong to this project");
        }

        member.setRole(request.getRole());
        return toResponse(projectMemberRepository.save(member));
    }

    @Transactional
    public void removeMember(Long projectId, Long memberId) {
        Long userId = SecurityUtil.getCurrentUserId();
        projectAccessService.requireAdmin(projectId, userId);

        ProjectMember member = projectMemberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        if (!member.getProject().getId().equals(projectId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member does not belong to this project");
        }

        if (member.getUser().getId().equals(userId) && member.getRole() == Role.ADMIN) {
            long adminCount = projectMemberRepository.findByProjectId(projectId).stream()
                    .filter(m -> m.getRole() == Role.ADMIN)
                    .count();
            if (adminCount <= 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot remove the only admin");
            }
        }

        projectMemberRepository.delete(member);
    }

    private MemberResponse toResponse(ProjectMember member) {
        return new MemberResponse(
                member.getId(),
                member.getUser().getId(),
                member.getUser().getName(),
                member.getUser().getEmail(),
                member.getRole());
    }
}
