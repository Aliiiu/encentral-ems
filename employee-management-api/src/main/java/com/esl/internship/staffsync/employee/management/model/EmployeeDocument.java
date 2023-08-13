package com.esl.internship.staffsync.employee.management.model;

import com.esl.internship.staffsync.document.management.model.Document;

import java.sql.Timestamp;

public class EmployeeDocument extends Document {

    private String employeeHasDocumentId;

    private String employeeId;

    private String documentType;

    public String getEmployeeHasDocumentId() {
        return employeeHasDocumentId;
    }

    public void setEmployeeHasDocumentId(String employeeHasDocumentId) {
        this.employeeHasDocumentId = employeeHasDocumentId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    @Override
    public String toString() {
        return "EmployeeDocument{" +
                "employeeHasDocumentId='" + employeeHasDocumentId + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", documentType='" + documentType + '\'' +
                ", dateCreated" + getDateCreated() + '\'' +
                '}';
    }

}
