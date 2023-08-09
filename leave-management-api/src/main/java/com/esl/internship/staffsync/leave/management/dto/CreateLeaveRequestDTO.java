package com.esl.internship.staffsync.leave.management.dto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;

public class CreateLeaveRequestDTO {
    @NotNull(message = "Employee id field cannot be null")
    private String employeeId;

    @NotNull(message = "Leave type id field cannot be null ")
    private String leaveTypeId;

    @NotNull(message = "Start date cannot be null")
    private Date startDate;

    @Future(message = "Leave date must be greater than current date")
    private Date endDate;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Optional<Date> getEndDate() {
        return Optional.ofNullable(endDate);
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
