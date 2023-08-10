package com.esl.internship.staffsync.employee.management.model;

import com.esl.internship.staffsync.commons.model.Employee;

import java.sql.Timestamp;

public class DepartmentHead {

    private String departmentHeadId;

    private String createdBy;

    private Timestamp dateCreated;

    private Timestamp dateModified;

    private String modifiedBy;

    private Department department;

    private Employee employee;

    public String getDepartmentHeadId() {
        return departmentHeadId;
    }

    public void setDepartmentHeadId(String departmentHeadId) {
        this.departmentHeadId = departmentHeadId;
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

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "DepartmentHead{" +
                "departmentHeadId='" + departmentHeadId + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
