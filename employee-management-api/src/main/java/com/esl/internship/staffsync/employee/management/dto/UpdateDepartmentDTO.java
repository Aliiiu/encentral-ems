package com.esl.internship.staffsync.employee.management.dto;

import javax.validation.constraints.NotNull;

public class UpdateDepartmentDTO {

    @NotNull
    private String departmentName;

    @NotNull
    private String description;

    @NotNull
    private Integer workingHours;

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
}
