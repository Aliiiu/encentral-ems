package com.esl.internship.staffsync.employee.management.model;

import com.encentral.staffsync.entity.enums.EmployeeRequestStatus;

import java.sql.Timestamp;

public class EmployeeUpdateRequest {
    private String employeeUpdateRequestId;
    private EmployeeRequestStatus approvalStatus;
    private Timestamp dateCreated;
    private Timestamp dateModified;
    private String oldValue;
    private String reason;
    private String remarks;
    private String updateFieldName;
    private String updateNewValue;
    private Employee approver;
    private Employee employee;

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

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUpdateFieldName() {
        return updateFieldName;
    }

    public void setUpdateFieldName(String updateFieldName) {
        this.updateFieldName = updateFieldName;
    }

    public String getUpdateNewValue() {
        return updateNewValue;
    }

    public void setUpdateNewValue(String updateNewValue) {
        this.updateNewValue = updateNewValue;
    }

    public Employee getApprover() {
        return approver;
    }

    public void setApprover(Employee approver) {
        this.approver = approver;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
