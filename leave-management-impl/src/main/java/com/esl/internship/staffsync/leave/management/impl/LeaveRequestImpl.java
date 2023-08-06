package com.esl.internship.staffsync.leave.management.impl;

import com.encentral.scaffold.commons.model.Employee;
import com.encentral.staffsync.entity.JpaLeaveRequest;
import com.encentral.staffsync.entity.QJpaLeaveRequest;
import com.encentral.staffsync.entity.enums.LeaveRequestStatus;
import com.esl.internship.staffsync.leave.management.api.ILeaveRequest;
import com.esl.internship.staffsync.leave.management.dto.CreateLeaveRequestDTO;
import com.esl.internship.staffsync.leave.management.model.LeaveRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.esl.internship.staffsync.leave.management.model.LeaveManagementMapper.INSTANCE;

/**
 * @author DEMILADE
 * @dateCreated 06/08/2023
 * @description General implementation of ILeaveRequest
 */
public class LeaveRequestImpl implements ILeaveRequest {

    private static final QJpaLeaveRequest qJpaLeaveRequest = QJpaLeaveRequest.jpaLeaveRequest;

    @Inject
    JPAApi jpaApi;


    @Override
    public LeaveRequest addLeaveRequest(CreateLeaveRequestDTO createLeaveRequestDTO) {
        LeaveRequest leaveRequest = INSTANCE.dtoToLeaveRequest(createLeaveRequestDTO);
        JpaLeaveRequest jpaLeaveRequest = INSTANCE.leaveRequestToJpaLeaveRequest(leaveRequest);

        jpaLeaveRequest.setLeaveRequestId(UUID.randomUUID().toString());
        jpaLeaveRequest.setApprovalStatus(LeaveRequestStatus.PENDING);
        jpaLeaveRequest.setDateCreated(Timestamp.from(Instant.now()));
        jpaApi.em().persist(jpaLeaveRequest);

        return INSTANCE.jpaLeaveRequestToLeaveRequest(jpaLeaveRequest);
    }

    @Override
    public Optional<LeaveRequest> getLeaveRequest(String leaveRequestId) {
        return Optional.ofNullable(INSTANCE.jpaLeaveRequestToLeaveRequest(getJpaLeaveRequestById(leaveRequestId)));
    }

    @Override
    public List<LeaveRequest> getEmployeeLeaveRequests(String employeeId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        return queryFactory.selectFrom(qJpaLeaveRequest)
                .where(qJpaLeaveRequest.employee.employeeId.eq(employeeId))
                .orderBy(qJpaLeaveRequest.dateCreated.desc())
                .fetch()
                .stream()
                .map(INSTANCE::jpaLeaveRequestToLeaveRequest)
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkIfEmployeeHasOpenLeaveRequest(String employeeId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        LeaveRequestStatus[] requestStatusesToCheck = {
                LeaveRequestStatus.IN_PROGRESS,
                LeaveRequestStatus.APPROVED,
                LeaveRequestStatus.PENDING
        };

        return queryFactory.selectFrom(qJpaLeaveRequest)
                .where(qJpaLeaveRequest.employee.employeeId.eq(employeeId))
                .where(qJpaLeaveRequest.approvalStatus.in(requestStatusesToCheck))
                .orderBy(qJpaLeaveRequest.dateCreated.desc())
                .fetch() != null;
    }

    @Override
    public boolean isOnLeave(String employeeId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        return queryFactory.selectFrom(qJpaLeaveRequest)
                .where(qJpaLeaveRequest.employee.employeeId.eq(employeeId))
                .where(qJpaLeaveRequest.approvalStatus.eq(LeaveRequestStatus.IN_PROGRESS))
                .orderBy(qJpaLeaveRequest.dateCreated.desc())
                .fetch() != null;
    }

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

    @Override
    public boolean approveLeaveRequest(String employeeId, Employee employee) {
        return false;
    }

    @Override
    public boolean cancelLeaveRequest(String employeeId, Employee employee) {
        return false;
    }

    @Override
    public boolean rejectLeaveRequest(Employee employee) {
        return false;
    }

    @Override
    public boolean acceptLeaveRequest(Employee employee) {
        return false;
    }

    @Override
    public boolean markLeaveRequestAsComplete(Employee employee) {
        return false;
    }

    @Override
    public boolean deleteLeaveRequest(String leaveRequestId) {
        return false;
    }

    @Override
    public boolean getDuedate(String employeeId) {
        return false;
    }

    private JpaLeaveRequest getJpaLeaveRequestById(String leaveRequestId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaLeaveRequest)
                .where(qJpaLeaveRequest.leaveRequestId.eq(leaveRequestId))
                .fetchOne();
    }
}
