package com.esl.internship.staffsync.employee.management.dto;

import com.encentral.staffsync.entity.enums.EmployeeStatus;

public class EmployeeStatusUpdateDTO {
    private EmployeeStatus currentStatus;

    public EmployeeStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(EmployeeStatus currentStatus) {
        this.currentStatus = currentStatus;
    }
}
