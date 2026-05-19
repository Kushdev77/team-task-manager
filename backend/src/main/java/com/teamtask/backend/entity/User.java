package com.teamtask.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "users")
@Getter
@Setter 
@NoArgsConstructor
@AllArgsConstructor

public class User 
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) 
  private Long id; 

  @Column(nullable = false , length = 100) 
  private String name;


  @Column(nullable = false , unique = true , length = 150) 
  private String email;

  @Column(nullable = false , length = 255) 
  private String password; 

  @Column(nullable = false , updatable = false) 
   private LocalDateTime createdAt; 

   
   @PrePersist 
   protected void onCreate() 
   {
    this.createdAt = LocalDateTime.now();
   }
}
