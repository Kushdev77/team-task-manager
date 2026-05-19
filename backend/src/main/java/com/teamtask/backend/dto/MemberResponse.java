package com.teamtask.backend.dto;

import com.teamtask.backend.enums.Role;

public record MemberResponse(
        Long id,
        Long userId,
        String userName,
        String userEmail,
        Role role
) {}
