package com.teamtask.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teamtask.backend.entity.Project;
import com.teamtask.backend.entity.User;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByCreatedBy(User createdBy);

    List<Project> findByCreatedById(Long userId);

    @Query("SELECT pm.project FROM ProjectMember pm WHERE pm.user.id = :userId")
    List<Project> findAllByMemberUserId(@Param("userId") Long userId);
}