package com.esl.internship.staffsync.employee.management.dto;


import com.esl.internship.staffsync.employee.management.model.DepartmentHead;

import java.sql.Timestamp;


public class DepartmentDTO {

    private String departmentName;
    private String description;
    private Integer workingHours;
    private String departmentHeadEmployeeId;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Integer workingHours) {
        this.workingHours = workingHours;
    }

    public String getDepartmentHeadEmployeeId() {
        return departmentHeadEmployeeId;
    }

    public void setDepartmentHeadEmployeeId(String departmentHeadEmployeeId) {
        this.departmentHeadEmployeeId = departmentHeadEmployeeId;
    }

}
