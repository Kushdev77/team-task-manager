package com.teamtask.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamtask.backend.dto.AddMemberRequest;
import com.teamtask.backend.dto.MemberResponse;
import com.teamtask.backend.dto.UpdateMemberRoleRequest;
import com.teamtask.backend.service.ProjectMemberService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/projects/{projectId}/members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    public ProjectMemberController(ProjectMemberService projectMemberService) {
        this.projectMemberService = projectMemberService;
    }

    @PostMapping
    public ResponseEntity<MemberResponse> addMember(
            @PathVariable Long projectId,
            @Valid @RequestBody AddMemberRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectMemberService.addMember(projectId, request));
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> listMembers(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectMemberService.listMembers(projectId));
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponse> updateRole(
            @PathVariable Long projectId,
            @PathVariable Long memberId,
            @Valid @RequestBody UpdateMemberRoleRequest request) {
        return ResponseEntity.ok(projectMemberService.updateMemberRole(projectId, memberId, request));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long projectId,
            @PathVariable Long memberId) {
        projectMemberService.removeMember(projectId, memberId);
        return ResponseEntity.noContent().build();
    }
}
