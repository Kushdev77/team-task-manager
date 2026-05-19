package com.teamtask.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.teamtask.backend.enums.TaskStatus;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        LocalDate dueDate,
        Long projectId,
        Long assigneeId,
        String assigneeName,
        Long createdById,
        String createdByName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean overdue
) {}
