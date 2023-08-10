package com.esl.internship.staffsync.employee.management.api;


import com.esl.internship.staffsync.employee.management.dto.EmployeeDocumentDTO;
import com.esl.internship.staffsync.employee.management.model.EmployeeDocument;

import java.util.List;
import java.util.Optional;


public interface IEmployeeDocumentUploadApi {

    EmployeeDocument addEmployeeDocument(String employeeId, EmployeeDocumentDTO employeeDocumentDTO, com.esl.internship.staffsync.commons.model.Employee employee);

    Optional<EmployeeDocument> getEmployeeDocument(String employeeDocumentId);

    List<EmployeeDocument> getDocumentsOwnedByEmployee(String employeeId);

    List<EmployeeDocument> getAllEmployeeDocuments();

    boolean updateEmployeeDocument(String employeeDocumentId, EmployeeDocumentDTO employeeDocumentDTO, com.esl.internship.staffsync.commons.model.Employee Employee);

    boolean deleteEmployeeDocument(String employeeDocumentId);

}
