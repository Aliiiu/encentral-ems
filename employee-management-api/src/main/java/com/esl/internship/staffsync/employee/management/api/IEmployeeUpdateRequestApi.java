package com.esl.internship.staffsync.employee.management.api;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.employee.management.dto.EmployeeUpdateApprovalDTO;
import com.esl.internship.staffsync.employee.management.dto.EmployeeUpdateRequestDTO;
import com.esl.internship.staffsync.employee.management.model.EmployeeUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface IEmployeeUpdateRequestApi {

    Response<EmployeeUpdateRequest> createEmployeeUpdateRequest(String employeeId, EmployeeUpdateRequestDTO employeeUpdateRequestDTO, Employee employee);

    Optional<EmployeeUpdateRequest> getEmployeeUpdateRequest(String employeeRequestUpdateId);

    List<EmployeeUpdateRequest> getUpdateRequestsOfEmployee(String employeeId);

    List<EmployeeUpdateRequest> getPendingUpdateRequestsOfEmployee(String employeeId);

    List<EmployeeUpdateRequest> getCompletedUpdateRequestsOfEmployee(String employeeId);

    List<EmployeeUpdateRequest> getAllEmployeeUpdateRequests();

    List<EmployeeUpdateRequest> getAllApprovedUpdateRequests();

    List<EmployeeUpdateRequest> getAllApprovedUpdateRequestsByEmployee(String approverEmployeeId);

    List<EmployeeUpdateRequest> getAllPendingUpdateRequests();

    List<EmployeeUpdateRequest> getAllCompletedUpdateRequests();

    Response<Boolean> reviewEmployeeUpdateRequest(String employeeUpdateRequestId, String approverEmployeeId, EmployeeUpdateApprovalDTO employeeUpdateApprovalDTO);

    Response<Boolean> cancelEmployeeUpdateRequest(String employeeUpdateRequestId, String employeeId);

    boolean deleteEmployeeUpdateRequest(String employeeUpdateRequestId);

}
