package com.esl.internship.staffsync.leave.management.dto;

import com.esl.internship.staffsync.commons.util.DateRangeDTO;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class CreateLeaveRequestDTO extends DateRangeDTO {
    @NotNull(message = "Employee id field cannot be null")
    private String employeeId;

    @NotNull(message = "Leave type id field cannot be null ")
    private String leaveTypeId;

    private String reason = "";

    public CreateLeaveRequestDTO() {
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(String leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }
}