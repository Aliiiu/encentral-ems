package com.esl.internship.staffsync.employee.management.dto;


public class EmployeeUpdateRequestDTO {

    private String oldValue;
    private String reason;
    private String updateFieldName;
    private String updateNewValue;
    private String employeeId;

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUpdateFieldName() {
        return updateFieldName;
    }

    public void setUpdateFieldName(String updateFieldName) {
        this.updateFieldName = updateFieldName;
    }

    public String getUpdateNewValue() {
        return updateNewValue;
    }

    public void setUpdateNewValue(String updateNewValue) {
        this.updateNewValue = updateNewValue;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

}
