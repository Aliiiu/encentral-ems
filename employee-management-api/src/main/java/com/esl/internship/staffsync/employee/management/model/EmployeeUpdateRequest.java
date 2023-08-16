package com.esl.internship.staffsync.employee.management.model;

import com.esl.internship.staffsync.entities.attribute.converter.EmployeeRequestStatusConverter;
import com.esl.internship.staffsync.entities.enums.EmployeeRequestStatus;
import com.esl.internship.staffsync.entities.enums.EmployeeStatus;
import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

public class EmployeeUpdateRequest {

    private String employeeUpdateRequestId;

    private EmployeeRequestStatus approvalStatus;

    private Timestamp dateCreated;

    private Timestamp dateModified;

    private String remarks;

    private String approverEmployeeId;

    private String employeeId;

    // Fields that can be updated;
    private String address;

    private Date dateOfBirth;

    private String employeeEmail;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String countryOfOrigin;

    private String employeeGender;

    private String employeeMaritalStatus;

    private String highestCertification;

    private String stateOfOrigin;

    public String getEmployeeUpdateRequestId() {
        return employeeUpdateRequestId;
    }

    public void setEmployeeUpdateRequestId(String employeeUpdateRequestId) {
        this.employeeUpdateRequestId = employeeUpdateRequestId;
    }

    public EmployeeRequestStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(EmployeeRequestStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getApproverEmployeeId() {
        return approverEmployeeId;
    }

    public void setApproverEmployeeId(String approverEmployeeId) {
        this.approverEmployeeId = approverEmployeeId;
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("employeeUpdateRequestId", employeeUpdateRequestId)
                .add("approvalStatus", approvalStatus)
                .add("dateRequested", dateCreated)
                .add("employeeId", employeeId)
                .toString();
    }
}
