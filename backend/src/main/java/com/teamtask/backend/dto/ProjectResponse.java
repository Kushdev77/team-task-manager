package com.teamtask.backend.dto;

import java.time.LocalDateTime;

import com.teamtask.backend.enums.Role;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        Long createdById,
        String createdByName,
        Role currentUserRole,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
