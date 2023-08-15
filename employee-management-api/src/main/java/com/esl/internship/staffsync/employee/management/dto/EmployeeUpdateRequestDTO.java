package com.esl.internship.staffsync.employee.management.dto;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class EmployeeUpdateRequestDTO {

    private String employeeId;

    // Fields that can be updated;
    @NotNull
    private String address;

    @NotNull
    private Date dateOfBirth;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String countryOfOriginOptionId;

    @NotNull
    private String employeeGenderOptionId;

    private String employeeMaritalStatusOptionId;

    private String highestCertificationOptionId;

    private String stateOfOriginOptionId;

    @NotNull
    private String emergencyContactFullName;

    @NotNull
    private String emergencyContactRelationship;

    @NotNull
    private String emergencyContactPhoneNumber;

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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getEmergencyContactFullName() {
        return emergencyContactFullName;
    }

    public void setEmergencyContactFullName(String emergencyContactFullName) {
        this.emergencyContactFullName = emergencyContactFullName;
    }

    public String getEmergencyContactRelationship() {
        return emergencyContactRelationship;
    }

    public void setEmergencyContactRelationship(String emergencyContactRelationship) {
        this.emergencyContactRelationship = emergencyContactRelationship;
    }

    public String getEmergencyContactPhoneNumber() {
        return emergencyContactPhoneNumber;
    }

    public void setEmergencyContactPhoneNumber(String emergencyContactPhoneNumber) {
        this.emergencyContactPhoneNumber = emergencyContactPhoneNumber;
    }

    @Override
    public String toString() {
        return "EmployeeUpdateRequestDTO{" +
                "employeeId='" + employeeId + '\'' +
                '}';
    }
}
