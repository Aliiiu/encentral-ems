package com.esl.internship.staffsync.employee.management.api;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.document.management.dto.SaveInfo;
import com.esl.internship.staffsync.employee.management.dto.EmployeeDocumentDTO;
import com.esl.internship.staffsync.employee.management.model.EmployeeDocument;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface IEmployeeDocumentUploadApi {

    Response<EmployeeDocument> addEmployeeDocument(String employeeId, EmployeeDocumentDTO employeeDocumentDTO, SaveInfo saveInfo, Employee employee);

    Optional<EmployeeDocument> getEmployeeDocument(String employeeDocumentId);

    Optional<File> getEmployeeActualDocument(String employeeDocumentId);

    List<EmployeeDocument> getDocumentsOwnedByEmployee(String employeeId);

    List<EmployeeDocument> getAllEmployeeDocuments();

    boolean updateEmployeeDocument(String employeeDocumentId, EmployeeDocumentDTO employeeDocumentDTO, SaveInfo saveInfo, Employee Employee);

    boolean deleteEmployeeDocument(String employeeDocumentId);

    String getEmployeeDocumentUploadRoot();

}
