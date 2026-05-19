package com.teamtask.backend.dto;

import java.util.List;

public record UserDashboardResponse(
        long totalProjects,
        long totalTasks,
        long totalOverdue,
        List<DashboardResponse> projectDashboards
) {}
