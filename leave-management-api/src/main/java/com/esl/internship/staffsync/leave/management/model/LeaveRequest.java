package com.esl.internship.staffsync.leave.management.model;

import com.encentral.staffsync.entity.enums.LeaveRequestStatus;

import java.sql.Timestamp;
import java.util.Date;

public class LeaveRequest {

    private String leaveRequestId;
    private LeaveRequestStatus approvalStatus;
    private Timestamp dateCreated;
    private Timestamp dateModified;
    private Integer duration;
    private String reason;
    private String remarks;
    private Date startDate;
    private LeaveRequestEmployee approver;
    private LeaveRequestEmployee employee;
    private LeaveOptionType leaveType;

    public LeaveRequest() {
    }

    public String getLeaveRequestId() {
        return leaveRequestId;
    }

    public void setLeaveRequestId(String leaveRequestId) {
        this.leaveRequestId = leaveRequestId;
    }

    public LeaveRequestStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(LeaveRequestStatus approvalStatus) {
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public LeaveRequestEmployee getApprover() {
        return approver;
    }

    public void setApprover(LeaveRequestEmployee approver) {
        this.approver = approver;
    }

    public LeaveRequestEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(LeaveRequestEmployee employee) {
        this.employee = employee;
    }

    public LeaveOptionType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveOptionType leaveType) {
        this.leaveType = leaveType;
    }
}
