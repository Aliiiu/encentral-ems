package com.esl.internship.staffsync.employee.management.api;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.employee.management.dto.EmployeeUpdateApprovalDTO;
import com.esl.internship.staffsync.employee.management.dto.EmployeeUpdateRequestDTO;
import com.esl.internship.staffsync.employee.management.model.EmployeeUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface IEmployeeUpdateRequestApi {

    EmployeeUpdateRequest createEmployeeUpdateRequest(String employeeId, EmployeeUpdateRequestDTO employeeUpdateRequestDTO, Employee employee);

    Optional<EmployeeUpdateRequest> getEmployeeUpdateRequest(String employeeRequestUpdateId);

    List<EmployeeUpdateRequest> getUpdateRequestsOfEmployee(String employeeId);

    List<EmployeeUpdateRequest> getAllEmployeeUpdateRequests();

    boolean reviewEmployeeUpdateRequest(String employeeUpdateRequestId, String approverEmployeeId, EmployeeUpdateApprovalDTO employeeUpdateApprovalDTO);

    boolean deleteEmployeeUpdateRequest(String employeeUpdateRequestId);

}
