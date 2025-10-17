package com.urrutia.user_service.dto;

public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private String role;
    private String status;

    // Constructor vacío
    public UserResponseDTO() {
    }

    // Constructor con parámetros
    public UserResponseDTO(Long id,
                           String firstName,
                           String lastName,
                           String email,
                           String role,
                           String status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    // gettes y setters
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getStatus() { return status; }


    public void setId(Long id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
    public void setStatus(String status) { this.status = status; }
}
