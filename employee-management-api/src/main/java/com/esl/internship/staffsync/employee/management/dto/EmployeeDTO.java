package com.esl.internship.staffsync.employee.management.dto;

import com.encentral.staffsync.entity.enums.EmployeeStatus;
import com.esl.internship.staffsync.employee.management.model.Department;

import java.security.Timestamp;
import java.util.Date;

public class EmployeeDTO {
    private String address;
    private EmployeeStatus currentStatus;
    private Date dateHired;
    private Date dateOfBirth;
    private Boolean employeeActive;
    private String employeeEmail;
    private String firstName;
    private String jobTitle;
    private String lastName;
    private Integer leaveDays;
    private Integer loginAttempts;
    private String password;
    private String phoneNumber;
    private String profilePictureUrl;
    private String departmentId;
}
