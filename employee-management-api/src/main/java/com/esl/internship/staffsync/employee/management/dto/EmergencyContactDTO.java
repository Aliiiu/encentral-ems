package com.esl.internship.staffsync.employee.management.dto;

import javax.validation.constraints.NotNull;

public class EmergencyContactDTO {

    private String address;

    private String email;

    @NotNull
    private String fullName;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String relationship;

    private String employeeId;

    private String contactGenderOptionId;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getContactGenderOptionId() {
        return contactGenderOptionId;
    }

    public void setContactGenderOptionId(String contactGenderOptionId) {
        this.contactGenderOptionId = contactGenderOptionId;
    }
}
