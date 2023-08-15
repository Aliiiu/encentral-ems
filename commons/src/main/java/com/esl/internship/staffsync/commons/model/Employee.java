package com.esl.internship.staffsync.commons.model;

import com.esl.internship.staffsync.entities.enums.EmployeeStatus;

import java.sql.Timestamp;
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

    private String lastName;

    private Integer leaveDays;

    private Timestamp lastLogin;

    private String phoneNumber;

//    private String profilePictureUrl;

    private String profilePictureId;

    private String modifiedBy;

    private String departmentId;

    private String countryOfOrigin;

    private String employeeGender;

    private String employeeMaritalStatus;

    private String highestCertification;

    private String stateOfOrigin;

    private String roleId;

    public Employee(String employeeId, String firstName) {
        this.employeeId = employeeId;
        this.firstName = firstName;
    }

    public Employee() {
    }

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

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePictureId() {
        return profilePictureId;
    }

    public void setProfilePictureId(String profilePictureId) {
        this.profilePictureId = profilePictureId;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getEmployeeGender() {
        return employeeGender;
    }

    public void setEmployeeGender(String employeeGender) {
        this.employeeGender = employeeGender;
    }

    public String getEmployeeMaritalStatus() {
        return employeeMaritalStatus;
    }

    public void setEmployeeMaritalStatus(String employeeMaritalStatus) {
        this.employeeMaritalStatus = employeeMaritalStatus;
    }

    public String getHighestCertification() {
        return highestCertification;
    }

    public void setHighestCertification(String highestCertification) {
        this.highestCertification = highestCertification;
    }

    public String getStateOfOrigin() {
        return stateOfOrigin;
    }

    public void setStateOfOrigin(String stateOfOrigin) {
        this.stateOfOrigin = stateOfOrigin;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId='" + employeeId + '\'' +
                ", currentStatus=" + currentStatus +
                ", employeeEmail='" + employeeEmail + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}
