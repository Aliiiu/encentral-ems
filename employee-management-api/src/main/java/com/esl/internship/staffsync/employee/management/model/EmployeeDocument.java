package com.esl.internship.staffsync.employee.management.model;

import java.sql.Timestamp;

public class EmployeeDocument {
    private String documentId;
    private String createdBy;
    private Timestamp dateCreated;
    private String employeeHasDocumentId;
    private Employee employee;

//    private Document document;
//    private Option documentType;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
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

    public String getEmployeeHasDocumentId() {
        return employeeHasDocumentId;
    }

    public void setEmployeeHasDocumentId(String employeeHasDocumentId) {
        this.employeeHasDocumentId = employeeHasDocumentId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

//    public Document getDocument() {
//        return document;
//    }
//
//    public void setDocument(Document document) {
//        this.document = document;
//    }
//
//    public Option getDocumentType() {
//        return documentType;
//    }
//
//    public void setDocumentType(Option documentType) {
//        this.documentType = documentType;
//    }
}
