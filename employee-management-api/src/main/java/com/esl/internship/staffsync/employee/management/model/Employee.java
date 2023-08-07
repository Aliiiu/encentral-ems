package com.esl.internship.staffsync.employee.management.model;

import com.encentral.staffsync.entity.enums.EmployeeStatus;

import java.security.Timestamp;
import java.util.Date;

public class Employee {
    private String employeeId;
    private String address;
    private String createdBy;
    private EmployeeStatus currentStatus;
    private Timestamp dateCreated;
    private Date dateHired;
    private Timestamp dateModified;
    private Date dateOfBirth;
    private Boolean employeeActive;
    private String employeeEmail;
    private String firstName;
    private String jobTitle;
    private Timestamp lastLogin;
    private String lastName;
    private Integer leaveDays;
    private Integer loginAttempts;
    private String modifiedBy;
    private String password;
    private String phoneNumber;
    private String profilePictureUrl;
    private Department department;

//    private Option countryOfOrigin;
//    private Option employeeGender;
//    private Option employeeMaritalStatus;
//    private Option highestCertification;
//    private Option stateOfOrigin;
//    private Role role;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public EmployeeStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(EmployeeStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateHired() {
        return dateHired;
    }

    public void setDateHired(Date dateHired) {
        this.dateHired = dateHired;
    }

    public Timestamp getDateModified() {
        return dateModified;
    }

    public void setDateModified(Timestamp dateModified) {
        this.dateModified = dateModified;
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

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
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

    public Integer getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(Integer loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

//    public Option getCountryOfOrigin() {
//        return countryOfOrigin;
//    }
//
//    public void setCountryOfOrigin(Option countryOfOrigin) {
//        this.countryOfOrigin = countryOfOrigin;
//    }
//
//    public Option getEmployeeGender() {
//        return employeeGender;
//    }
//
//    public void setEmployeeGender(Option employeeGender) {
//        this.employeeGender = employeeGender;
//    }
//
//    public Option getEmployeeMaritalStatus() {
//        return employeeMaritalStatus;
//    }
//
//    public void setEmployeeMaritalStatus(Option employeeMaritalStatus) {
//        this.employeeMaritalStatus = employeeMaritalStatus;
//    }
//
//    public Option getHighestCertification() {
//        return highestCertification;
//    }
//
//    public void setHighestCertification(Option highestCertification) {
//        this.highestCertification = highestCertification;
//    }
//
//    public Option getStateOfOrigin() {
//        return stateOfOrigin;
//    }
//
//    public void setStateOfOrigin(Option stateOfOrigin) {
//        this.stateOfOrigin = stateOfOrigin;
//    }
//
//    public Role getRole() {
//        return role;
//    }
//
//    public void setRole(Role role) {
//        this.role = role;
//    }


    @Override
    public String toString() {
        return "Employee{" +
                "dateHired=" + dateHired +
                ", employeeActive=" + employeeActive +
                ", employeeEmail='" + employeeEmail + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}
