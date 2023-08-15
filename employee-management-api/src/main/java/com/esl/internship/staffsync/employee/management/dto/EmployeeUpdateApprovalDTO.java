package com.esl.internship.staffsync.employee.management.dto;

import com.esl.internship.staffsync.entities.enums.EmployeeRequestStatus;

import javax.validation.constraints.NotNull;

public class EmployeeUpdateApprovalDTO {

    @NotNull
    private String employeeUpdateRequestId;

    @NotNull
    private EmployeeRequestStatus approvalStatus;

    private String approverEmployeeId;

    private String remarks;

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

    public String getApproverEmployeeId() {
        return approverEmployeeId;
    }

    public void setApproverEmployeeId(String approverEmployeeId) {
        this.approverEmployeeId = approverEmployeeId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
