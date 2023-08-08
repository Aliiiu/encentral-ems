package com.esl.internship.staffsync.employee.management.dto;

public class EmergencyContactDTO {

    private String address;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
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
