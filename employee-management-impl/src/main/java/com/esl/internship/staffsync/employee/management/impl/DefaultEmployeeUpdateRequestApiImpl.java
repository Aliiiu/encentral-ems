package com.esl.internship.staffsync.employee.management.impl;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.employee.management.api.IEmployeeApi;
import com.esl.internship.staffsync.employee.management.api.IEmployeeUpdateRequestApi;
import com.esl.internship.staffsync.employee.management.dto.EmployeeUpdateApprovalDTO;
import com.esl.internship.staffsync.employee.management.dto.EmployeeUpdateRequestDTO;
import com.esl.internship.staffsync.employee.management.model.EmployeeUpdateRequest;
import com.esl.internship.staffsync.entities.JpaEmployee;
import com.esl.internship.staffsync.entities.JpaEmployeeUpdateRequest;
import com.esl.internship.staffsync.entities.QJpaEmployee;
import com.esl.internship.staffsync.entities.QJpaEmployeeUpdateRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class DefaultEmployeeUpdateRequestApiImpl implements IEmployeeUpdateRequestApi {

    @Inject
    JPAApi jpaApi;

    private static final QJpaEmployee qJpaEmployee = QJpaEmployee.jpaEmployee;

    private static final QJpaEmployeeUpdateRequest qJpaEmployeeUpdateRequest = QJpaEmployeeUpdateRequest.jpaEmployeeUpdateRequest;

    @Override
    public Response<EmployeeUpdateRequest> createEmployeeUpdateRequest(String employeeId, EmployeeUpdateRequestDTO employeeUpdateRequestDTO, Employee employee) {
        Response<EmployeeUpdateRequest> res = new Response<>();

        JpaEmployee jpaEmployee = getJpaEmployee(employeeId);

        if (jpaEmployee == null)
            return res.putError("employeeId", "Employee Does not exist");


        return null;
    }

    @Override
    public Optional<EmployeeUpdateRequest> getEmployeeUpdateRequest(String employeeRequestUpdateId) {
        return Optional.empty();
    }

    @Override
    public List<EmployeeUpdateRequest> getUpdateRequestsOfEmployee(String employeeId) {
        return null;
    }

    @Override
    public List<EmployeeUpdateRequest> getAllEmployeeUpdateRequests() {
        return null;
    }

    @Override
    public boolean reviewEmployeeUpdateRequest(String employeeUpdateRequestId, String approverEmployeeId, EmployeeUpdateApprovalDTO employeeUpdateApprovalDTO) {
        return false;
    }

    @Override
    public boolean deleteEmployeeUpdateRequest(String employeeUpdateRequestId) {
        return false;
    }

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description A helper method to fetch an employee record from the database
     *
     * @param employeeId ID of the employee to fetch
     *
     * @return JpaEmployee An employee record or null if not found
     */
    private JpaEmployee getJpaEmployee(String employeeId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .fetchOne();
    }

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description A helper method to fetch an employee update request record
     *
     * @param employeeUpdateRequestId ID of the employee to fetch
     *
     * @return JpaEmployeeUpdateRequest An employeeUpdateRequest record or null if not found
     */
    private JpaEmployeeUpdateRequest getJpaEmployeeUpdateRequest(String employeeUpdateRequestId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEmployeeUpdateRequest)
                .where(qJpaEmployeeUpdateRequest.employeeUpdateRequestId.eq(employeeUpdateRequestId))
                .fetchOne();
    }
}
