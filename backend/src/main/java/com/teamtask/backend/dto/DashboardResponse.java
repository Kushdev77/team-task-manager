package com.teamtask.backend.dto;

import java.util.List;
import java.util.Map;

import com.teamtask.backend.enums.TaskStatus;

public record DashboardResponse(
        Long projectId,
        String projectName,
        long totalTasks,
        Map<TaskStatus, Long> statusCounts,
        long overdueCount,
        List<TaskResponse> overdueTasks
) {}
