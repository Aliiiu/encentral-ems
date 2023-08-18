package com.esl.internship.staffsync.leave.management.impl;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.util.DateUtility;
import com.esl.internship.staffsync.employee.management.api.IEmployeeApi;
import com.esl.internship.staffsync.entities.JpaEmployee;
import com.esl.internship.staffsync.entities.JpaLeaveRequest;
import com.esl.internship.staffsync.entities.QJpaEmployee;
import com.esl.internship.staffsync.entities.QJpaLeaveRequest;
import com.esl.internship.staffsync.entities.enums.EmployeeStatus;
import com.esl.internship.staffsync.entities.enums.LeaveRequestStatus;
import com.esl.internship.staffsync.leave.management.api.ILeaveRequest;
import com.esl.internship.staffsync.leave.management.dto.CreateLeaveRequestDTO;
import com.esl.internship.staffsync.leave.management.dto.EditLeaveRequestDTO;
import com.esl.internship.staffsync.leave.management.model.LeaveRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.esl.internship.staffsync.commons.util.Utility.stringifyEmployee;
import static com.esl.internship.staffsync.leave.management.model.LeaveManagementMapper.INSTANCE;

/**
 * @author DEMILADE
 * @dateCreated 06/08/2023
 * @description General implementation of ILeaveRequest
 */
public class DefaultLeaveRequestImpl implements ILeaveRequest {

    private static final QJpaLeaveRequest qJpaLeaveRequest = QJpaLeaveRequest.jpaLeaveRequest;

    private static final QJpaEmployee qJpaEmployee = QJpaEmployee.jpaEmployee;

    @Inject
    JPAApi jpaApi;

    @Inject
    IEmployeeApi iEmployeeApi;

    //TODO: Check if leave type option is of option type leave type


    /**
     * @author DEMILADE
     * @dateCreated 06/08/2023
     * @description A method to create a leave request
     *
     * @param createLeaveRequestDTO DTO for creating a leave request
     *
     * @return Newly created leave request object
     */
    @Override
    public LeaveRequest addLeaveRequest(CreateLeaveRequestDTO createLeaveRequestDTO) {
        LeaveRequest leaveRequest = INSTANCE.creationDtoToLeaveRequest(createLeaveRequestDTO);
        JpaLeaveRequest jpaLeaveRequest = INSTANCE.leaveRequestToJpaLeaveRequest(leaveRequest);

        jpaLeaveRequest.setLeaveRequestId(UUID.randomUUID().toString());
        jpaLeaveRequest.setApprovalStatus(LeaveRequestStatus.PENDING);
        jpaLeaveRequest.setDateCreated(Timestamp.from(Instant.now()));
        jpaApi.em().persist(jpaLeaveRequest);
        return INSTANCE.jpaLeaveRequestToLeaveRequest(jpaLeaveRequest);
    }


    /**
     * @author DEMILADE
     * @dateCreated 07/08/2023
     * @description Get all leave requests in the system
     *
     * @return List of leave requests
     */
    @Override
    public List<LeaveRequest> getAllLeaveRequests() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        return queryFactory.selectFrom(qJpaLeaveRequest)
                .orderBy(qJpaLeaveRequest.dateCreated.desc())
                .fetch()
                .stream()
                .map(INSTANCE::jpaLeaveRequestToLeaveRequest)
                .collect(Collectors.toList());
    }

    /**
     * @author DEMILADE
     * @dateCreated 06/08/2023
     * @description Method finds and returns an Optional containing a leave request by id
     *
     * @param leaveRequestId Leave request id
     *
     * @return Optional containing a leave request
     */
    @Override
    public Optional<LeaveRequest> getLeaveRequest(String leaveRequestId) {
        return Optional.ofNullable(INSTANCE.jpaLeaveRequestToLeaveRequest(getJpaLeaveRequestById(leaveRequestId)));
    }

    /**
     * @author DEMILADE
     * @dateCreated 06/08/2023
     * @description Method gets all Leave requests created by an employee
     *
     * @param employeeId Employee id
     *
     * @return List of leave request objects
     */
    @Override
    public List<LeaveRequest> getEmployeeLeaveRequests(String employeeId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        return queryFactory.selectFrom(qJpaLeaveRequest)
                .where(qJpaLeaveRequest.employee.employeeId.eq(employeeId.toString()))
                .orderBy(qJpaLeaveRequest.dateCreated.desc())
                .fetch()
                .stream()
                .map(INSTANCE::jpaLeaveRequestToLeaveRequest)
                .collect(Collectors.toList());
    }

    /**
     * @author DEMILADE
     * @dateCreated 08/08/2023
     * @description Method gets all Leave requests approvedd by an employee
     *
     * @param employeeId Employee id
     *
     * @return List of leave request objects
     */
    @Override
    public List<LeaveRequest> getEmployeeApprovedLeaveRequests(String employeeId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        return queryFactory.selectFrom(qJpaLeaveRequest)
                .where(qJpaLeaveRequest.approver.employeeId.eq(employeeId.toString()))
                .orderBy(qJpaLeaveRequest.dateCreated.desc())
                .fetch()
                .stream()
                .map(INSTANCE::jpaLeaveRequestToLeaveRequest)
                .collect(Collectors.toList());
    }

    /**
     * @author DEMILADE
     * @dateCreated 06/08/2023
     * @description Method to check if an employee has an open (in_progress, approved or pending) leave request
     *
     * @param employeeId Employee id
     *
     * @return boolean indicating if employee has an open request
     */
    @Override
    public boolean checkIfEmployeeHasOpenLeaveRequest(String employeeId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        LeaveRequestStatus[] requestStatusesToCheck = {
                LeaveRequestStatus.IN_PROGRESS,
                LeaveRequestStatus.APPROVED,
                LeaveRequestStatus.PENDING
        };

        return queryFactory.selectFrom(qJpaLeaveRequest)
                .where(qJpaLeaveRequest.employee.employeeId.eq(employeeId)
                        .and(qJpaLeaveRequest.approvalStatus.in(requestStatusesToCheck)))
                .orderBy(qJpaLeaveRequest.dateCreated.desc())
                .fetch().size() !=0;
    }

    /**
     * @author DEMILADE
     * @dateCreated 06/08/2023
     * @description Method to check if an employee is on leave
     *
     * @param employeeId
     *
     * @return boolean indicating leave status
     */
    @Override
    public boolean isOnLeave(String employeeId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        return queryFactory.selectFrom(qJpaLeaveRequest)
                .where(qJpaLeaveRequest.employee.employeeId.eq(employeeId.toString())
                        .and(qJpaLeaveRequest.approvalStatus.eq(LeaveRequestStatus.IN_PROGRESS)))
                .orderBy(qJpaLeaveRequest.dateCreated.desc())
                .fetch() != null;
    }

    /**
     * @author DEMILADE
     * @dateCreated 06/08/2023
     * @description Get all in progress leave requests
     *
     * @return List of ongoing leave requests
     */
    @Override
    public List<LeaveRequest> getAllOngoingLeave() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        return queryFactory.selectFrom(qJpaLeaveRequest)
                .where(qJpaLeaveRequest.approvalStatus.eq(LeaveRequestStatus.IN_PROGRESS))
                .orderBy(qJpaLeaveRequest.dateCreated.desc())
                .fetch()
                .stream()
                .map(INSTANCE::jpaLeaveRequestToLeaveRequest)
                .collect(Collectors.toList());
    }

    /**
     * @author DEMILADE
     * @dateCreated 06/08/2023
     * @description Get all pending leave requests
     *
     * @return List of all pending leave requests
     */
    @Override
    public List<LeaveRequest> getAllPendingRequests() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        return queryFactory.selectFrom(qJpaLeaveRequest)
                .where(qJpaLeaveRequest.approvalStatus.eq(LeaveRequestStatus.PENDING))
                .orderBy(qJpaLeaveRequest.dateCreated.asc())
                .fetch()
                .stream()
                .map(INSTANCE::jpaLeaveRequestToLeaveRequest)
                .collect(Collectors.toList());

    }

    /**
     * @author DEMILADE
     * @dateCreated 06/08/2023
     * @description Get all ongoing and completed leave requests for an employee
     *
     * @param employeeId Employee id
     *
     * @return List of leave requests
     */
    @Override
    public List<LeaveRequest> getEmployeeLeaveHistory(String employeeId) {
        LeaveRequestStatus[] requestStatusesToCheck = {
                LeaveRequestStatus.IN_PROGRESS,
                LeaveRequestStatus.COMPLETED
        };
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        return queryFactory.selectFrom(qJpaLeaveRequest)
                .where(qJpaLeaveRequest.approvalStatus.in(requestStatusesToCheck)
                        .and(qJpaLeaveRequest.employee.employeeId.eq(employeeId)))
                .orderBy(qJpaLeaveRequest.dateCreated.asc())
                .fetch()
                .stream()
                .map(INSTANCE::jpaLeaveRequestToLeaveRequest)
                .collect(Collectors.toList());
    }

    /**
     * @author DEMILADE
     * @dateCreated 06/08/2023
     * @description Method to get all completed leaves in the system
     *
     * @return List of all completed leave requests
     */
    @Override
    public List<LeaveRequest> getAllCompletedLeave() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        return queryFactory.selectFrom(qJpaLeaveRequest)
                .where(qJpaLeaveRequest.approvalStatus.eq(LeaveRequestStatus.COMPLETED))
                .orderBy(qJpaLeaveRequest.dateCreated.desc())
                .fetch()
                .stream()
                .map(INSTANCE::jpaLeaveRequestToLeaveRequest)
                .collect(Collectors.toList());
    }

    /**
     * @author DEMILADE
     * @dateCreated 006/08/2023
     * @description Method to approve a leave request. Only requests that are 'pending' may be approved
     *
     * @param editLeaveRequestDTO -DTO for editing a leave request
     *
     * @return Boolean indicating success
     */
    @Override
    public boolean approveLeaveRequest(EditLeaveRequestDTO editLeaveRequestDTO, Employee employee) {
        JpaEmployee approver = new JpaEmployee();
        approver.setEmployeeId(employee.getEmployeeId());

        return new JPAQueryFactory(jpaApi.em()).update(qJpaLeaveRequest)
                .set(qJpaLeaveRequest.approvalStatus, LeaveRequestStatus.APPROVED)
                .set(qJpaLeaveRequest.approver, approver)
                .set(qJpaLeaveRequest.dateModified, Timestamp.from(Instant.now()))
                .set(qJpaLeaveRequest.remarks, editLeaveRequestDTO.getRemarks())
                .where(qJpaLeaveRequest.leaveRequestId.eq(editLeaveRequestDTO.getLeaveRequestId())
                        .and(qJpaLeaveRequest.approvalStatus.eq(LeaveRequestStatus.PENDING)))
                .execute() == 1;
    }

    /**
     * @author DEMILADE
     * @dateCreatded 08/08/2023
     * @description A method to cancel a leave request. Only 'approved' and 'pending' requests may be cancelled
     *
     * @param employeeId Employee id
     *
     * @return Boolean indicating success
     */
    @Override
    public boolean cancelLeaveRequest(String employeeId) {
        LeaveRequestStatus[] requestStatusesToCheck = {
                LeaveRequestStatus.APPROVED,
                LeaveRequestStatus.PENDING
        };

        return new JPAQueryFactory(jpaApi.em()).update(qJpaLeaveRequest)
                .set(qJpaLeaveRequest.approvalStatus, LeaveRequestStatus.CANCELED)
                .set(qJpaLeaveRequest.dateModified, Timestamp.from(Instant.now()))
                .where(qJpaLeaveRequest.employee.employeeId.eq(employeeId)
                        .and(qJpaLeaveRequest.approvalStatus.in(requestStatusesToCheck)))
                .execute() == 1;
    }

    /**
     * @author DEMILADE
     * @dateCreated 06/08/2023
     * @description Method to reject a leave request. Only a pending request may be cancelled
     *
     * @param editLeaveRequestDTO DTO for editing a request
     *
     * @return Boolean indicating success
     */
    @Override
    public boolean rejectLeaveRequest(EditLeaveRequestDTO editLeaveRequestDTO, Employee employee) {
        JpaEmployee approver = new JpaEmployee();
        approver.setEmployeeId(employee.getEmployeeId());

        return new JPAQueryFactory(jpaApi.em()).update(qJpaLeaveRequest)
                .set(qJpaLeaveRequest.approvalStatus, LeaveRequestStatus.REJECTED)
                .set(qJpaLeaveRequest.approver, approver)
                .set(qJpaLeaveRequest.dateModified, Timestamp.from(Instant.now()))
                .set(qJpaLeaveRequest.remarks, editLeaveRequestDTO.getRemarks())
                .where(qJpaLeaveRequest.leaveRequestId.eq(editLeaveRequestDTO.getLeaveRequestId())
                        .and(qJpaLeaveRequest.approvalStatus.eq(LeaveRequestStatus.PENDING)))
                .execute() == 1;
    }

    /**
     * @author DEMILADE
     * @dateCreated 06/08/2023
     * @description Method to accept an approved leave request
     *
     * @param employee Employee object
     *
     * @return boolean indicating success
     */
    @Override
    public boolean acceptLeaveRequest(Employee employee) {

        AtomicBoolean isTransactionSuccessful = new AtomicBoolean(false);
        jpaApi.withTransaction(em -> {
            boolean updatedRequest = new JPAQueryFactory(jpaApi.em()).update(qJpaLeaveRequest)
                    .set(qJpaLeaveRequest.approvalStatus, LeaveRequestStatus.IN_PROGRESS)
                    .set(qJpaLeaveRequest.dateModified, Timestamp.from(Instant.now()))
                    .where(qJpaLeaveRequest.employee.employeeId.eq(employee.getEmployeeId())
                            .and(qJpaLeaveRequest.approvalStatus.eq(LeaveRequestStatus.APPROVED)))
                    .execute() == 1;
            if (!updatedRequest) return false;
            boolean updatedEmployee = updateEmployeeCurrentStatus(EmployeeStatus.ON_LEAVE, employee.getEmployeeId(), employee);
            isTransactionSuccessful.set(updatedEmployee);
            return updatedEmployee;
        });
        return isTransactionSuccessful.get();
    }

    /**
     * @author DEMILADE
     * @dateCreated 06/08/2023
     * @description Method to mark a leave request as completed
     *
     * @param days number of days used for the leave
     * @param employeeId Employee id
     * @param employee Employee object
     *
     * @return Boolean indicating success
     */
    @Override
    public boolean markLeaveRequestAsComplete(int days, String employeeId, Employee employee) {
        AtomicBoolean isTransactionSuccessful = new AtomicBoolean(false);

        Date endDate = DateUtility.convertToDate(LocalDate.now());
        int daysUsed = getNumberOfLeaveDays(employeeId) + days;
        jpaApi.withTransaction(em -> {
            boolean updatedRequest = new JPAQueryFactory(jpaApi.em()).update(qJpaLeaveRequest)
                    .set(qJpaLeaveRequest.approvalStatus, LeaveRequestStatus.COMPLETED)
                    .set(qJpaLeaveRequest.dateModified, Timestamp.from(Instant.now()))
                    .set(qJpaLeaveRequest.duration, daysUsed)
                    .set(qJpaLeaveRequest.endDate, endDate)
                    .where(qJpaLeaveRequest.employee.employeeId.eq(employeeId)
                            .and(qJpaLeaveRequest.approvalStatus.eq(LeaveRequestStatus.IN_PROGRESS)))
                    .execute() == 1;
            if (!updatedRequest) return false;
            boolean updatedEmployee = updateEmployeeLeaveDays(daysUsed, employeeId, employee);
            if (!updatedEmployee) return false;
            boolean activatedEmployee =  updateEmployeeCurrentStatus(EmployeeStatus.ACTIVE,employeeId, employee);
            isTransactionSuccessful.set(activatedEmployee);
            return activatedEmployee;
        });
        return isTransactionSuccessful.get();
    }

    /**
     * @author DEMILADE
     * @dateCreated 06/08/2023
     * @description Method to delete a leave request
     *
     * @param leaveRequestId Leave request id
     *
     * @return boolean indicating success
     */
    @Override
    public boolean deleteLeaveRequest(String leaveRequestId) {
        LeaveRequestStatus[] requestStatusesToCheck = {
                LeaveRequestStatus.IN_PROGRESS,
                LeaveRequestStatus.APPROVED,
                LeaveRequestStatus.PENDING
        };

        return new JPAQueryFactory(jpaApi.em()).delete(qJpaLeaveRequest)
                .where(qJpaLeaveRequest.leaveRequestId.eq(leaveRequestId)
                        .and(qJpaLeaveRequest.approvalStatus.notIn(requestStatusesToCheck)))
                .execute() == 1;
    }

    /**
     * @author DEMILADE
     * @dateCreated 06/08/2023
     * @description Method to get the number of days left for an employee
     *
     * @param employeeId Employee id
     *
     * @return Number of leave days
     */
    @Override
    public Integer getNumberOfLeaveDays(String employeeId) {
        int days = 0;
        try {
            return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEmployee)
                    .where(qJpaEmployee.employeeId.eq(employeeId))
                    .fetchOne()
                    .getLeaveDays();
        }
        catch (Exception e) {
            return days;
        }
    }

    /**
     * @author DEMILADE
     * @dateCreated 06/08/2023
     * @description Method finds and returns an Optional containing a leave request by Employee's id
     *
     * @param employeeId Employee id
     *
     * @return Optional containing a leave request
     */
    @Override
    public Optional<LeaveRequest> getOngoingLeaveRequestByEmployeeId(String employeeId) {
        JpaLeaveRequest j = new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaLeaveRequest)
                .where(qJpaLeaveRequest.employee.employeeId.eq(employeeId)
                        .and(qJpaLeaveRequest.approvalStatus.eq(LeaveRequestStatus.IN_PROGRESS)))
                .fetchOne();
        return Optional.ofNullable(INSTANCE.jpaLeaveRequestToLeaveRequest(j));
    }

    /**
     * @author DEMILADE
     * @dateCreated 06/08/2023
     * @description Method to get the time spent by a user on leave
     *
     * @param date Start date of leave
     *
     * @return Duration of leave
     */
    @Override
    public long getActualLeaveDuration(Date date) {
        LocalDate currDate = LocalDate.now();
        LocalDate startDate = DateUtility.convertToLocalDate(date);
        return ChronoUnit.DAYS.between(startDate, currDate);
    }

    /**
     * @author DEMILADE
     * @dateCreated 15/08/2023
     * @description Method to close all on going leave requests when they hit their due date
     *
     * @return boolean indicating success
     */
    @Override
    public boolean closeCompletedLeave() {
        Date endDate = DateUtility.convertToDate(LocalDate.now());
        Map<String, Integer> employeeIdMap = getEmployeesOnLeave(endDate);
        updateEmployees(employeeIdMap);

        JPAUpdateClause updateClause = new JPAUpdateClause(jpaApi.em(), qJpaLeaveRequest);
        for (Map.Entry<String, Integer> entry : employeeIdMap.entrySet()) {
            String employeeId = entry.getKey();
            Integer leaveDuration = entry.getValue();

            updateClause.where(qJpaLeaveRequest.employee.employeeId.eq(employeeId)
                            .and(qJpaLeaveRequest.endDate.eq(endDate))
                            .and(qJpaLeaveRequest.approvalStatus.eq(LeaveRequestStatus.IN_PROGRESS)))
                    .set(qJpaLeaveRequest.approvalStatus, LeaveRequestStatus.COMPLETED)
                    .set(qJpaLeaveRequest.dateModified, Timestamp.from(Instant.now()))
                    .set(qJpaLeaveRequest.duration, leaveDuration);
        }
        return updateClause.execute() >= 0;

    }

    /**
     * @author DEMILADE
     * @dateCreated 16/08/2023
     * @description Method to get the IDs of all employees whose leaves are to be completed and the leave duratioh
     *
     * @param endDate Date of leave end
     *
     * @return Map with employee ids as keys and leave duration as values
     */
    private Map<String, Integer> getEmployeesOnLeave(Date endDate) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaLeaveRequest)
                .where(qJpaLeaveRequest.endDate.eq(endDate)
                        .and(qJpaLeaveRequest.approvalStatus.eq(LeaveRequestStatus.IN_PROGRESS)))
                .fetch()
                .stream()
                .map(INSTANCE::getDurationAndEmployeeId)
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    /**
     * @author DEMILADE
     * @dateCreated 16/08/2023
     * @description Method to batch update all employees with expired leaves
     *
     * @param employeeMap Map containing employee id and their leave duration
     *
     * @return Boolean indicating success
     */
    private boolean updateEmployees(Map<String, Integer> employeeMap) {
        if (employeeMap.size() == 0) return false;
        Employee employee = iEmployeeApi.getEmployeeById("system_employee").orElseThrow();

        JPAUpdateClause updateClause = new JPAUpdateClause(jpaApi.em(), qJpaEmployee);
        for (Map.Entry<String, Integer> entry : employeeMap.entrySet()) {
            String employeeId = entry.getKey();
            Integer leaveDuration = entry.getValue();

            updateClause.where(qJpaEmployee.employeeId.eq(employeeId))
                    .set(qJpaEmployee.currentStatus, EmployeeStatus.ACTIVE)
                    .set(qJpaEmployee.leaveDays, qJpaEmployee.leaveDays.add(leaveDuration))
                    .set(qJpaEmployee.modifiedBy, stringifyEmployee(employee))
                    .set(qJpaEmployee.dateModified, Timestamp.from(Instant.now()));
        }
        return updateClause.execute() > 0;
    }

    /**
     * @author DEMILADE
     * @dateCreated 06/08/2023
     * @description Method to fetch Jpa leave request by id
     *
     * @param leaveRequestId Leave request id
     *
     * @return Jpa Leave request object
     */
    private JpaLeaveRequest getJpaLeaveRequestById(String leaveRequestId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaLeaveRequest)
                .where(qJpaLeaveRequest.leaveRequestId.eq(leaveRequestId))
                .fetchOne();
    }

    /**
     * @author DEMILADE
     * @dateCreated 06/08/2023
     * @description Method to update an employee's current status
     *
     * @param employeeStatus Employee status
     * @param employeeId Employee id
     * @param employee Employee object
     *
     * @return Boolean indicating success
     */
    private boolean updateEmployeeCurrentStatus(EmployeeStatus employeeStatus, String employeeId, Employee employee) {
        return new JPAQueryFactory(jpaApi.em()).update(qJpaEmployee)
                .set(qJpaEmployee.currentStatus, employeeStatus)
                .set(qJpaEmployee.modifiedBy, stringifyEmployee(employee, "Updated employee current status"))
                .set(qJpaEmployee.dateModified, Timestamp.from(Instant.now()))
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .execute() == 1;
    }

    /**
     * @author DEMILADE
     * @dateCreated 06/08/2023
     * @description method to update the number of leave days an employee has
     *
     * @param leaveDays New number of leave days
     * @param employeeId Employee id
     * @param employee Employee object
     *
     * @return Boolean indicating success
     */
    private boolean updateEmployeeLeaveDays(int leaveDays, String employeeId, Employee employee) {
        return new JPAQueryFactory(jpaApi.em()).update(qJpaEmployee)
                .set(qJpaEmployee.leaveDays, leaveDays)
                .set(qJpaEmployee.modifiedBy, stringifyEmployee(employee, "Updated employee current status"))
                .set(qJpaEmployee.dateModified, Timestamp.from(Instant.now()))
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .execute() == 1;
    }
}
