package com.esl.internship.staffsync.employee.management.model;

import java.sql.Timestamp;

public class EmployeeDocument {

    private String employeeHasDocumentId;

    private String employeeId;

    private String documentType;

    private String createdBy;

    private Timestamp dateCreated;

    private Timestamp dateModified;

    private String documentDescription;

    private String documentName;

    private String modifiedBy;

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

    public String getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
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
