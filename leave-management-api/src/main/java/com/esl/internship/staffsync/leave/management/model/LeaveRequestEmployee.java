package com.esl.internship.staffsync.leave.management.model;

import com.encentral.staffsync.entity.enums.EmployeeStatus;

public class LeaveRequestEmployee {
    private String employeeId;
    private EmployeeStatus currentStatus;
    private String firstName;
    private String lastName;

    public LeaveRequestEmployee() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public EmployeeStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(EmployeeStatus currentStatus) {
        this.currentStatus = currentStatus;
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

}
