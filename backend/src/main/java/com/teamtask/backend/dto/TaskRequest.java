package com.teamtask.backend.dto;

import java.time.LocalDate;

import com.teamtask.backend.enums.TaskStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;

    @Size(max = 1000)
    private String description;

    private TaskStatus status;

    private LocalDate dueDate;

    private Long assigneeId;
}
