package com.esl.internship.staffsync.employee.management.dto;


import com.encentral.staffsync.entity.enums.EmployeeStatus;

import java.util.Date;


public class EmployeeDTO {

    private String address;
    private EmployeeStatus currentStatus;
    private Date dateHired;
    private Date dateOfBirth;
    private Boolean employeeActive;
    private String employeeEmail;
    private String firstName;
    private String jobTitle;
    private String lastName;
    private Integer leaveDays;
    private String password;
    private String phoneNumber;
    private String profilePictureUrl;
    private String departmentId;
    private String roleId;
    private String countryOfOriginOptionId;
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

    public Integer getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(Integer leaveDays) {
        this.leaveDays = leaveDays;
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

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
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
