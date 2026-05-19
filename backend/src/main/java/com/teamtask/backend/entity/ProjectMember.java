package com.teamtask.backend.entity;

import com.teamtask.backend.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "project_members")
@Getter
@Setter 
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMember 
{
  @Id 
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  
  @ManyToOne(fetch = FetchType.LAZY) 
  @JoinColumn(name = "project_id" , nullable = false) 
  private Project project; 


  @ManyToOne(fetch = FetchType.LAZY) 
  @JoinColumn(name = "user_id" , nullable = false)
  private User user; 


  @Enumerated(EnumType.STRING)
  @Column(nullable = false , length = 20) 
  private Role role; 

  
  
}
