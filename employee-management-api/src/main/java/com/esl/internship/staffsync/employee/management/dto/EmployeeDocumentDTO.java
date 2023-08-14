package com.esl.internship.staffsync.employee.management.dto;

import javax.validation.constraints.NotNull;

public class EmployeeDocumentDTO {

    @NotNull
    private String employeeId;

    private String documentDescription;

    @NotNull
    private String documentName;

    @NotNull
    private String documentUploadPath;

    @NotNull
    private String documentTypeOptionId;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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

    public String getDocumentUploadPath() {
        return documentUploadPath;
    }

    public void setDocumentUploadPath(String documentUploadPath) {
        this.documentUploadPath = documentUploadPath;
    }

    public String getDocumentTypeOptionId() {
        return documentTypeOptionId;
    }

    public void setDocumentTypeOptionId(String documentTypeOptionId) {
        this.documentTypeOptionId = documentTypeOptionId;
    }

}
