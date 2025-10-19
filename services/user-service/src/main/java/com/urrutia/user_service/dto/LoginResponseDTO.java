package com.urrutia.user_service.dto;

public class LoginResponseDTO {
    private String email;
    private String token;

    public LoginResponseDTO() {}

    public LoginResponseDTO(String email, String token) {
        this.email = email;
        this.token = token;
    }

    //getetrs y setters

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
