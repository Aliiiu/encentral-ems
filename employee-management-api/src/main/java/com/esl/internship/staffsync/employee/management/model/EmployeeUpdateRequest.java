package com.esl.internship.staffsync.employee.management.model;

import com.esl.internship.staffsync.entities.attribute.converter.EmployeeRequestStatusConverter;
import com.esl.internship.staffsync.entities.enums.EmployeeRequestStatus;

import javax.persistence.*;
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
    private String approverEmployeeId;
    private String employeeId;

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

    @Override
    public String toString() {
        return "EmployeeUpdateRequest{" +
                "employeeUpdateRequestId='" + employeeUpdateRequestId + '\'' +
                ", approvalStatus=" + approvalStatus +
                ", oldValue='" + oldValue + '\'' +
                ", updateFieldName='" + updateFieldName + '\'' +
                ", updateNewValue='" + updateNewValue + '\'' +
                ", employeeId='" + employeeId + '\'' +
                '}';
    }

}
