package com.esl.internship.staffsync.employee.management.dto;

import com.esl.internship.staffsync.document.management.dto.DocumentDTO;

import javax.validation.constraints.NotNull;

public class EmployeeDocumentDTO extends DocumentDTO {

    private String employeeId;

    @NotNull(message = "A document type required")
    private String documentTypeOptionId;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDocumentTypeOptionId() {
        return documentTypeOptionId;
    }

    public void setDocumentTypeOptionId(String documentTypeOptionId) {
        this.documentTypeOptionId = documentTypeOptionId;
    }

}
