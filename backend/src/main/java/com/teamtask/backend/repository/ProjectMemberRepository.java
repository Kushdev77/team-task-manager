package com.teamtask.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamtask.backend.entity.ProjectMember;
import com.teamtask.backend.enums.Role;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long>
{
  List<ProjectMember> findByProjectId(Long projectId);
  
  Optional<ProjectMember> findByProjectIdAndUserId(Long projectId , Long userId); 
  
  boolean existsByProjectIdAndUserId(Long projectId , Long userId);

  List<ProjectMember> findByUserId(Long userId);

  Optional<ProjectMember> findByProjectIdAndUserIdAndRole(Long projectId , Long userId , Role role);
  
}
