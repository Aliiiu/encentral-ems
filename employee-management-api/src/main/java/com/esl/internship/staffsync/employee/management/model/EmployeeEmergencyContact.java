package com.esl.internship.staffsync.employee.management.model;

import java.sql.Timestamp;

public class EmployeeEmergencyContact {
    private String emergencyContactId;
    private String address;
    private String createdBy;
    private Timestamp dateCreated;
    private Timestamp dateModified;
    private String email;
    private String firstName;
    private String lastName;
    private String modifiedBy;
    private String phoneNumber;
    private String relationship;
    private Employee employee;
//    private Option contactGender;

    public String getEmergencyContactId() {
        return emergencyContactId;
    }

    public void setEmergencyContactId(String emergencyContactId) {
        this.emergencyContactId = emergencyContactId;
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

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateModified() {
        return dateModified;
    }

    public void setDateModified(Timestamp dateModified) {
        this.dateModified = dateModified;
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

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

//    public Option getContactGender() {
//        return contactGender;
//    }
//
//    public void setContactGender(Option contactGender) {
//        this.contactGender = contactGender;
//    }
}
