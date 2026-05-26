package com.smartqueue.dto;

import com.smartqueue.model.enums.Category;
import com.smartqueue.model.enums.Role;

public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
    private Category specialization;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Category getSpecialization() { return specialization; }
    public void setSpecialization(Category s) { this.specialization = s; }
}
