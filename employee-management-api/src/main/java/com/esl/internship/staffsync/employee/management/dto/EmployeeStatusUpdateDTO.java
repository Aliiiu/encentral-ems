package com.esl.internship.staffsync.employee.management.dto;

import com.esl.internship.staffsync.entities.enums.EmployeeStatus;

import javax.validation.constraints.NotNull;

public class EmployeeStatusUpdateDTO {

    @NotNull
    private EmployeeStatus currentStatus;

    public EmployeeStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(EmployeeStatus currentStatus) {
        this.currentStatus = currentStatus;
    }
}
