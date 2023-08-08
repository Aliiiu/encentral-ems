package com.esl.internship.staffsync.leave.management.api;

import com.encentral.scaffold.commons.model.Employee;
import com.esl.internship.staffsync.leave.management.dto.CreateLeaveRequestDTO;
import com.esl.internship.staffsync.leave.management.dto.EditLeaveRequestDTO;
import com.esl.internship.staffsync.leave.management.model.LeaveRequest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ILeaveRequest {

    LeaveRequest addLeaveRequest(CreateLeaveRequestDTO createLeaveRequestDTO);

    Optional<LeaveRequest> getLeaveRequest(String leaveRequestId);

    List<LeaveRequest> getAllLeaveRequests();

    List<LeaveRequest> getEmployeeLeaveRequests(String employeeId);

    List<LeaveRequest> getEmployeeApprovedLeaveRequests(String employeeId);

    boolean checkIfEmployeeHasOpenLeaveRequest(String employeeId);

    boolean isOnLeave(String employeeId);

    List<LeaveRequest> getAllPendingRequests();

    List<LeaveRequest> getEmployeeLeaveHistory(String employeeId);

    List<LeaveRequest> getAllCompletedLeave();

    List<LeaveRequest> getAllOngoingLeave();

    boolean approveLeaveRequest(EditLeaveRequestDTO editLeaveRequestDTO, Employee employee);

    boolean cancelLeaveRequest(String employeeId);

    boolean rejectLeaveRequest(EditLeaveRequestDTO editLeaveRequestDTO, Employee employee);

    boolean acceptLeaveRequest(Employee employee);

    boolean markLeaveRequestAsComplete(int days, String employeeId, Employee employee);

    boolean deleteLeaveRequest(String leaveRequestId);

    Integer getNumberOfLeaveDays(String employeeId);

    Optional<LeaveRequest> getOngoingLeaveRequestByEmployeeId(String employeeId);

    long getActualLeaveDuration(Date date);
}
