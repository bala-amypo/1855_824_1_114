

package com.example.demo.model;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
@Entity
public class User {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String employeeId;
      private String fullName;
      private String email;
     private String teamName;
     private String role;
    private Boolean active = true;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }
    

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }
    public String getEmployeeId(){
        return employeeId;
    }
    public void setEmployeeId(String employeeId){
        this.employeeId=employeeId;
    }
    public String getFullName(){
        return fullName;
    }
    public void setFullName(String fullName){
        this.fullName = fullName;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getTeamName(){
        return teamName;
    }
    public void setTeamName(String TeamName){
        this.teamName = teamName;
    }
    public String getRole(){
        return role;
    }
    public void setRole(String Role){
        this.role = role;
    }
    public boolean getActive(){
        return active;
    }
    public void setActive(Boolean active){
        this.active = active;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    } 
    public void setActiveLocalDateTime(LocalDateTime createdAt){
        this.createdAt=createdAt;
    } 
    public EmployeeProfile(Long id, String employeeId, String fullName, String email, String teamName, String role, Boolean active, LocalDateTime createdAt){
        this.id=id;
        this.employeeId=employeeId;
        this.fullName=fullName;
        this.email=email;
        this.teamName=teamName;
        this.role=role;
        this.active=active;
        this.createdAt=createdAt;
    }  
    public EmployeeProfile(){}
}