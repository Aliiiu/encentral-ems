package com.esl.internship.staffsync.leave.management.api;

import com.encentral.scaffold.commons.model.Employee;
import com.esl.internship.staffsync.leave.management.dto.CreateLeaveRequestDTO;
import com.esl.internship.staffsync.leave.management.model.LeaveRequest;

import java.util.List;
import java.util.Optional;

public interface ILeaveRequest {

    LeaveRequest addLeaveRequest(CreateLeaveRequestDTO createLeaveRequestDTO);

    Optional<LeaveRequest> getLeaveRequest(String leaveRequestId);

    List<LeaveRequest> getEmployeeLeaveRequests(String employeeId);

    boolean checkIfEmployeeHasOpenLeaveRequest(String employeeId);

    boolean isOnLeave(String employeeId);

    List<LeaveRequest> getAllOngoingLeave();

    boolean approveLeaveRequest(String employeeId, Employee employee);

    boolean cancelLeaveRequest(String employeeId, Employee employee);

    boolean rejectLeaveRequest(Employee employee);

    boolean acceptLeaveRequest(Employee employee);

    boolean markLeaveRequestAsComplete(Employee employee);

    boolean deleteLeaveRequest(String leaveRequestId);

    boolean getDuedate(String employeeId);
}
