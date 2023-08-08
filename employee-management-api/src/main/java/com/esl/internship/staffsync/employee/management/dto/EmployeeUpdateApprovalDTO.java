package com.esl.internship.staffsync.employee.management.dto;


import com.encentral.staffsync.entity.enums.EmployeeRequestStatus;


public class EmployeeUpdateApprovalDTO {

    private String employeeUpdateRequestId;
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
