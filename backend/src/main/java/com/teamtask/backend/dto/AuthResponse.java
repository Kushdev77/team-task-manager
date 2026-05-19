package com.teamtask.backend.dto;

public record AuthResponse(
        Long id,
        String name,
        String email,
        String token
) {}
