package com.esl.internship.staffsync.authentication.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class LoginDTO {

    @NotNull(message = "Email field cannot be empty ")
    @Email(message = "Invalid email format")
    private String employeeEmail;

    @NotNull(message = "Password field cannot be empty")
    private String password;

    public LoginDTO() {
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
