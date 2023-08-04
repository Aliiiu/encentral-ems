package com.esl.internship.staffsync.employee.management.model;

import java.sql.Timestamp;

public class Department {
    private String departmentId;
    private String createdBy;
    private Timestamp dateCreated;
    private Timestamp dateModified;
    private String departmentName;
    private String description;
    private String modifiedBy;
    private Integer workingHours;
    private DepartmentHead departmentHead;
//    private List<Employee> employees;


    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Integer getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Integer workingHours) {
        this.workingHours = workingHours;
    }

    public DepartmentHead getDepartmentHead() {
        return departmentHead;
    }

    public void setDepartmentHead(DepartmentHead departmentHead) {
        this.departmentHead = departmentHead;
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentName='" + departmentName + '\'' +
                ", description='" + description + '\'' +
                ", workingHours=" + workingHours +
                ", departmentHead=" + departmentHead +
                '}';
    }
}
