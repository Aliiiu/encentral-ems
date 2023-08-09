package com.esl.internship.staffsync.authentication.model;

public class AuthEmployeeSlice {
    private String employeeId;

    private String firstName;

    private String lastName;

    private Boolean employeeActive;

    private String roleId;

    private String employeeEmail;

    private Integer loginAttempts;

    private String password;

    public AuthEmployeeSlice() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getEmployeeActive() {
        return employeeActive;
    }

    public void setEmployeeActive(Boolean employeeActive) {
        this.employeeActive = employeeActive;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public Integer getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(Integer loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
