package com.esl.internship.staffsync.employee.management.dto;

import com.esl.internship.staffsync.entities.enums.EmployeeStatus;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class EmployeeDTO {

    @NotNull
    private String address;

    @NotNull
    private EmployeeStatus currentStatus;

    @NotNull
    private Date dateHired;

    @NotNull
    private Date dateOfBirth;

    @NotNull
    private Boolean employeeActive;

    @NotNull
    private String employeeEmail;

    @NotNull
    private String firstName;

    @NotNull
    private String jobTitle;

    @NotNull
    private String lastName;
    @NotNull
    private String password;

    @NotNull
    private String phoneNumber;

    private String departmentId;

    @NotNull
    private String roleId;

    private Integer entitledYearlyLeaveDays;

    @NotNull
    private String countryOfOriginOptionId;

    @NotNull
    private String employeeGenderOptionId;

    private String employeeMaritalStatusOptionId;

    private String highestCertificationOptionId;

    private String stateOfOriginOptionId;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public EmployeeStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(EmployeeStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Date getDateHired() {
        return dateHired;
    }

    public void setDateHired(Date dateHired) {
        this.dateHired = dateHired;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getEmployeeActive() {
        return employeeActive;
    }

    public void setEmployeeActive(Boolean employeeActive) {
        this.employeeActive = employeeActive;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Integer getEntitledYearlyLeaveDays() {
        return entitledYearlyLeaveDays;
    }

    public void setEntitledYearlyLeaveDays(Integer entitledYearlyLeaveDays) {
        this.entitledYearlyLeaveDays = entitledYearlyLeaveDays;
    }

    public String getCountryOfOriginOptionId() {
        return countryOfOriginOptionId;
    }

    public void setCountryOfOriginOptionId(String countryOfOriginOptionId) {
        this.countryOfOriginOptionId = countryOfOriginOptionId;
    }

    public String getEmployeeGenderOptionId() {
        return employeeGenderOptionId;
    }

    public void setEmployeeGenderOptionId(String employeeGenderOptionId) {
        this.employeeGenderOptionId = employeeGenderOptionId;
    }

    public String getEmployeeMaritalStatusOptionId() {
        return employeeMaritalStatusOptionId;
    }

    public void setEmployeeMaritalStatusOptionId(String employeeMaritalStatusOptionId) {
        this.employeeMaritalStatusOptionId = employeeMaritalStatusOptionId;
    }

    public String getHighestCertificationOptionId() {
        return highestCertificationOptionId;
    }

    public void setHighestCertificationOptionId(String highestCertificationOptionId) {
        this.highestCertificationOptionId = highestCertificationOptionId;
    }

    public String getStateOfOriginOptionId() {
        return stateOfOriginOptionId;
    }

    public void setStateOfOriginOptionId(String stateOfOriginOptionId) {
        this.stateOfOriginOptionId = stateOfOriginOptionId;
    }

}
