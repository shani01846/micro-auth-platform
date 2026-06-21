package com.example.demo.Dto;

import java.util.List;

public class UserAuthDto {
    private String username;
    private String password; 
    private String role;
    private List<String> roles; 

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public List<String> getRoles() {
        if (roles != null) return roles;
        if (role != null) return List.of(role);
        return List.of("ROLE_USER");
    }
    public void setRoles(List<String> roles) { this.roles = roles; }
}