package com.teamtask.backend.dto;

import com.teamtask.backend.enums.Role;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemberRoleRequest {

    @NotNull(message = "Role is required")
    private Role role;
}
