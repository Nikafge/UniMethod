package com.example.unimethod.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Логін є обов'язковим")
    @Size(min = 3, max = 100, message = "Логін має містити від 3 до 100 символів")
    private String username;

    @NotBlank(message = "Email є обов'язковим")
    @Email(message = "Некоректний email")
    private String email;

    @NotBlank(message = "Пароль є обов'язковим")
    @Size(min = 6, message = "Пароль має містити щонайменше 6 символів")
    private String password;

    @NotBlank(message = "Ім'я є обов'язковим")
    private String firstName;

    @NotBlank(message = "Прізвище є обов'язковим")
    private String lastName;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}