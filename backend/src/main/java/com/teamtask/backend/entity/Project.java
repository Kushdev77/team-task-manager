package com.teamtask.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "projects")
@Getter
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class Project 
{
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY) 
   private Long id;
  
   @Column(nullable = false , length = 150)
   private String name;
   
   
   @Column(length = 500) 
   private String description;
   

   @ManyToOne(fetch = FetchType.LAZY) 
   @JoinColumn(name = "created_by" , nullable = false)
   private User createdBy;
   
   @Column(nullable = false , updatable = false)
   private LocalDateTime createdAt; 
   
   private LocalDateTime updatedAt; 

   @PrePersist
   protected void onCreate()
   {
    this.createdAt = LocalDateTime.now();

    this.updatedAt = LocalDateTime.now();
   } 

   @PreUpdate 
   protected void onUpdate() 
   {
    this.updatedAt = LocalDateTime.now();
   }
}
