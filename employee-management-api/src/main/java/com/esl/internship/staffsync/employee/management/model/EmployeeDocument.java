package com.esl.internship.staffsync.employee.management.model;


import com.encentral.scaffold.commons.model.Document;

import java.sql.Timestamp;


public class EmployeeDocument {

    private String employeeHasDocumentId;
    private String createdBy;
    private Timestamp dateCreated;
    private Document document;
    private String employeeId;
    private String documentType;

    public String getEmployeeHasDocumentId() {
        return employeeHasDocumentId;
    }

    public void setEmployeeHasDocumentId(String employeeHasDocumentId) {
        this.employeeHasDocumentId = employeeHasDocumentId;
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

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
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
                '}';
    }

}
