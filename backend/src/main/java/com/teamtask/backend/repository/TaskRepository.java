package com.teamtask.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamtask.backend.entity.Task;
import com.teamtask.backend.enums.TaskStatus;

public interface TaskRepository extends JpaRepository<Task , Long>
{
    List<Task> findByProjectId(Long projectId);
    List<Task> findByAssigneeId(Long assigneeId);
    List<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status);
    List<Task> findByAssigneeIdAndStatus(Long assigneeId, TaskStatus status);
    List<Task> findByProjectIdAndDueDateBeforeAndStatusNot(
            Long projectId,
            LocalDate date,
            TaskStatus status);
}
