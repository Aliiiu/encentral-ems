package com.esl.internship.staffsync.employee.management.model;

import com.google.common.base.MoreObjects;

import java.sql.Timestamp;

public class EmergencyContact {

    private String emergencyContactId;

    private String address;

    private String createdBy;

    private Timestamp dateCreated;

    private Timestamp dateModified;

    private String email;

    private String fullName;

    private String modifiedBy;

    private String phoneNumber;

    private String relationship;

    private String employeeId;

    private String contactGender;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getContactGender() {
        return contactGender;
    }

    public void setContactGender(String contactGender) {
        this.contactGender = contactGender;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("emergencyContactId", emergencyContactId)
                .add("fullName", fullName)
                .add("relationship", relationship)
                .add("employeeId", employeeId)
                .toString();
    }
}
