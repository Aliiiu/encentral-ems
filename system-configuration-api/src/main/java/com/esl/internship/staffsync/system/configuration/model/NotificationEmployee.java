package com.esl.internship.staffsync.system.configuration.model;

import com.encentral.staffsync.entity.enums.EmployeeStatus;

public class NotificationEmployee {
    private String employeeId;
    private String firstName;
    private String lastName;

    public NotificationEmployee() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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
