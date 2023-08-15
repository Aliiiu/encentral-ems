package com.esl.internship.staffsync.employee.management.impl;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.employee.management.api.IEmployeeUpdateRequestApi;
import com.esl.internship.staffsync.employee.management.dto.EmployeeUpdateApprovalDTO;
import com.esl.internship.staffsync.employee.management.dto.EmployeeUpdateRequestDTO;
import com.esl.internship.staffsync.employee.management.model.EmployeeUpdateRequest;
import com.esl.internship.staffsync.entities.*;
import com.esl.internship.staffsync.entities.enums.EmployeeRequestStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.esl.internship.staffsync.commons.util.Utility.stringifyEmployee;
import static com.esl.internship.staffsync.employee.management.model.EmployeeManagementMapper.INSTANCE;

public class DefaultEmployeeUpdateRequestApiImpl implements IEmployeeUpdateRequestApi {

    @Inject
    JPAApi jpaApi;

    private static final QJpaEmployee qJpaEmployee = QJpaEmployee.jpaEmployee;

    private static final QJpaOption qJpaOption = QJpaOption.jpaOption;

    private static final QJpaEmergencyContact qJpaEmergencyContact = QJpaEmergencyContact.jpaEmergencyContact;

    private static final QJpaEmployeeUpdateRequest qJpaEmployeeUpdateRequest = QJpaEmployeeUpdateRequest.jpaEmployeeUpdateRequest;

    /**
     * @author WARITH
     * @dateCreated 14/08/2023
     * @description Create an Employee-Update-Request record
     *
     * @param employeeId ID of the employee
     * @param employeeUpdateRequestDTO Update Request Data
     * @param employee Employee making the request
     *
     * @return Response<EmployeeUpdateRequest>
     */
    @Override
    public Response<EmployeeUpdateRequest> createEmployeeUpdateRequest(String employeeId, EmployeeUpdateRequestDTO employeeUpdateRequestDTO, Employee employee) {

        Response<EmployeeUpdateRequest> response = new Response<>();

        JpaEmployee jpaEmployee = getJpaEmployee(employeeId);

        if (jpaEmployee == null)
            return response.putError("employeeId", "Employee Does not exist");

        JpaOption employeeGender = getJpaOption(employeeUpdateRequestDTO.getEmployeeGenderOptionId());
        if (employeeGender == null)
            response.putError("employeeGenderOptionId", "Option does not exist");

        JpaOption stateOfOrigin = null;
        if (employeeUpdateRequestDTO.getStateOfOriginOptionId() != null) {
            stateOfOrigin = getJpaOption(employeeUpdateRequestDTO.getStateOfOriginOptionId());
            if (stateOfOrigin == null)
                response.putError("stateOfOriginOptionId", "Option does not exist");
        }

        JpaOption countryOfOrigin = getJpaOption(employeeUpdateRequestDTO.getCountryOfOriginOptionId());
        if (countryOfOrigin == null)
            response.putError("countryOfOriginOptionId", "Option does not exist");

        JpaOption highestCertification = null;
        if (employeeUpdateRequestDTO.getHighestCertificationOptionId() != null) {
            highestCertification = getJpaOption(employeeUpdateRequestDTO.getHighestCertificationOptionId());
            if (highestCertification == null)
                response.putError("highestCertificationOptionId", "Option does not exist");
        }

        JpaOption employeeMaritalStatus = null;
        if (employeeUpdateRequestDTO.getEmployeeMaritalStatusOptionId() != null) {
            employeeMaritalStatus = getJpaOption(employeeUpdateRequestDTO.getEmployeeMaritalStatusOptionId());
            if (employeeMaritalStatus == null)
                response.putError("employeeMaritalStatusOptionId", "Option does not exist");
        }


        if (response.requestHasErrors())
            return response;

        JpaEmployeeUpdateRequest jpaEmployeeUpdateRequest = INSTANCE.mapEmployeeUpdateRequestDTO(employeeUpdateRequestDTO);
        jpaEmployeeUpdateRequest.setEmployee(jpaEmployee);
        jpaEmployeeUpdateRequest.setDateCreated(Timestamp.from(Instant.now()));
        jpaEmployeeUpdateRequest.setEmployeeUpdateRequestId(UUID.randomUUID().toString());
        jpaEmployeeUpdateRequest.setApprovalStatus(EmployeeRequestStatus.PENDING);
        jpaEmployeeUpdateRequest.setEmployeeGender(employeeGender);
        jpaEmployeeUpdateRequest.setCountryOfOrigin(countryOfOrigin);
        jpaEmployeeUpdateRequest.setStateOfOrigin(stateOfOrigin);
        jpaEmployeeUpdateRequest.setEmployeeMaritalStatus(employeeMaritalStatus);
        jpaEmployeeUpdateRequest.setHighestCertification(highestCertification);

        jpaApi.em().persist(jpaEmployeeUpdateRequest);

        return response.setValue(INSTANCE.mapEmployeeUpdateRequest(jpaEmployeeUpdateRequest));

    }

    /**
     * @author WARITH
     * @dateCreated 14/08/2023
     * @description Det an Employee-Update-Request record by ID
     *
     * @param employeeRequestUpdateId ID of the employee-update-request
     *
     * @return Optional<EmployeeUpdateRequest>
     */
    @Override
    public Optional<EmployeeUpdateRequest> getEmployeeUpdateRequest(String employeeRequestUpdateId) {
        return Optional.ofNullable(INSTANCE.mapEmployeeUpdateRequest(getJpaEmployeeUpdateRequest(employeeRequestUpdateId)));
    }

    /**
     * @author WARITH
     * @dateCreated 14/08/2023
     * @description Get the Employee-Update-Request records of an employee
     *
     * @param employeeId ID of the employee
     *
     * @return List<EmployeeUpdateRequest>
     */
    @Override
    public List<EmployeeUpdateRequest> getUpdateRequestsOfEmployee(String employeeId) {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaEmployeeUpdateRequest)
                .where(qJpaEmployeeUpdateRequest.employee.employeeId.eq(employeeId))
                .fetch()
                .stream()
                .map(INSTANCE::mapEmployeeUpdateRequest)
                .collect(Collectors.toList());
    }

    /**
     * @author WARITH
     * @dateCreated 14/08/2023
     * @description Get all the pending Employee-Update-Request records of an employee
     *
     * @param employeeId ID of the employee
     *
     * @return List<EmployeeUpdateRequest>
     */
    @Override
    public List<EmployeeUpdateRequest> getPendingUpdateRequestsOfEmployee(String employeeId) {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaEmployeeUpdateRequest)
                .where(qJpaEmployeeUpdateRequest.employee.employeeId.eq(employeeId))
                .where(qJpaEmployeeUpdateRequest.approvalStatus.eq(EmployeeRequestStatus.PENDING))
                .fetch()
                .stream()
                .map(INSTANCE::mapEmployeeUpdateRequest)
                .collect(Collectors.toList());
    }

    /**
     * @author WARITH
     * @dateCreated 14/08/2023
     * @description Get all the completed Employee-Update-Request records of an employee
     *
     * @param employeeId ID of the employee
     *
     * @return List<EmployeeUpdateRequest>
     */
    @Override
    public List<EmployeeUpdateRequest> getCompletedUpdateRequestsOfEmployee(String employeeId) {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaEmployeeUpdateRequest)
                .where(qJpaEmployeeUpdateRequest.employee.employeeId.eq(employeeId))
                .where(qJpaEmployeeUpdateRequest.approvalStatus.eq(EmployeeRequestStatus.COMPLETED))
                .fetch()
                .stream()
                .map(INSTANCE::mapEmployeeUpdateRequest)
                .collect(Collectors.toList());
    }

    /**
     * @author WARITH
     * @dateCreated 14/08/2023
     * @description Get all Employee-Update-Request records
     *
     * @return List<EmployeeUpdateRequest>
     */
    @Override
    public List<EmployeeUpdateRequest> getAllEmployeeUpdateRequests() {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaEmployeeUpdateRequest)
                .fetch()
                .stream()
                .map(INSTANCE::mapEmployeeUpdateRequest)
                .collect(Collectors.toList());
    }

    /**
     * @author WARITH
     * @dateCreated 14/08/2023
     * @description Get all approved Employee-Update-Request records
     *
     * @return List<EmployeeUpdateRequest>
     */
    @Override
    public List<EmployeeUpdateRequest> getAllApprovedUpdateRequests() {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaEmployeeUpdateRequest)
                .where(qJpaEmployeeUpdateRequest.approvalStatus.eq(EmployeeRequestStatus.APPROVED))
                .fetch()
                .stream()
                .map(INSTANCE::mapEmployeeUpdateRequest)
                .collect(Collectors.toList());
    }

    /**
     * @author WARITH
     * @dateCreated 14/08/2023
     * @description Get all approved Employee-Update-Request records by an employee
     *
     * @param approverEmployeeId ID of the employee that approved the request
     *
     * @return List<EmployeeUpdateRequest>
     */
    @Override
    public List<EmployeeUpdateRequest> getAllApprovedUpdateRequestsByEmployee(String approverEmployeeId) {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaEmployeeUpdateRequest)
                .where(qJpaEmployeeUpdateRequest.approvalStatus.eq(EmployeeRequestStatus.APPROVED))
                .where(qJpaEmployeeUpdateRequest.approver.employeeId.eq(approverEmployeeId))
                .fetch()
                .stream()
                .map(INSTANCE::mapEmployeeUpdateRequest)
                .collect(Collectors.toList());
    }

    /**
     * @author WARITH
     * @dateCreated 14/08/2023
     * @description Get all pending Employee-Update-Request records
     *
     * @return List<EmployeeUpdateRequest>
     */
    @Override
    public List<EmployeeUpdateRequest> getAllPendingUpdateRequests() {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaEmployeeUpdateRequest)
                .where(qJpaEmployeeUpdateRequest.approvalStatus.eq(EmployeeRequestStatus.PENDING))
                .fetch()
                .stream()
                .map(INSTANCE::mapEmployeeUpdateRequest)
                .collect(Collectors.toList());
    }

    /**
     * @author WARITH
     * @dateCreated 14/08/2023
     * @description Get all completed Employee-Update-Request records
     *
     * @return List<EmployeeUpdateRequest>
     */
    @Override
    public List<EmployeeUpdateRequest> getAllCompletedUpdateRequests() {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaEmployeeUpdateRequest)
                .where(qJpaEmployeeUpdateRequest.approvalStatus.eq(EmployeeRequestStatus.COMPLETED))
                .fetch()
                .stream()
                .map(INSTANCE::mapEmployeeUpdateRequest)
                .collect(Collectors.toList());
    }

    /**
     * @author WARITH
     * @dateCreated 14/08/2023
     * @description Review an Employee-Update-Request record
     *
     * @param employeeUpdateRequestId ID of the record to review
     * @param approverEmployeeId ID of the employee making the review
     * @param employeeUpdateApprovalDTO Review information
     *
     * @return Response<Boolean>
     */
    @Override
    public Response<Boolean> reviewEmployeeUpdateRequest(String employeeUpdateRequestId, String approverEmployeeId, EmployeeUpdateApprovalDTO employeeUpdateApprovalDTO) {
        JpaEmployeeUpdateRequest jpaEmployeeUpdateRequest = getJpaEmployeeUpdateRequest(employeeUpdateRequestId);
        Response<Boolean> response = new Response<>(false);

        if (jpaEmployeeUpdateRequest == null) {
            response.putError("employeeUpdateRequestId", "Update request not found");
            return response;
        }

        if (jpaEmployeeUpdateRequest.getApprovalStatus() == EmployeeRequestStatus.CANCELED) {
            response.putError("Approval Status", "Request has been canceled. Cannot be reviewed");
            return response;
        }

        if (jpaEmployeeUpdateRequest.getApprovalStatus() == EmployeeRequestStatus.COMPLETED) {
            response.putError("Approval Status", "Request has been completed already. Cannot be reviewed");
            return response;
        }

        JpaEmployee approver = getJpaEmployee(approverEmployeeId);
        if (approver == null) {
            response.putError("approverEmployeeId", "Employee with Id not found");
            return response;
        }


        EmployeeRequestStatus status = employeeUpdateApprovalDTO.getApprovalStatus();
        String remarks = employeeUpdateApprovalDTO.getRemarks();

        jpaEmployeeUpdateRequest.setApprovalStatus(status);
        jpaEmployeeUpdateRequest.setRemarks(remarks);
        jpaEmployeeUpdateRequest.setApprover(approver);
        jpaEmployeeUpdateRequest.setDateModified(Timestamp.from(Instant.now()));

        if (status == EmployeeRequestStatus.APPROVED) {
            JpaEmployee employee = jpaEmployeeUpdateRequest.getEmployee();
            String modelEmployee = stringifyEmployee(INSTANCE.mapEmployee(employee));
            JpaEmergencyContact emergencyContact = getJpaEmergencyContact(employee.getEmployeeId());

            // update the employee record
            mapJpaEmployeeStatusUpdateRequestToJpaEmployee(jpaEmployeeUpdateRequest, employee);
            employee.setDateModified(Timestamp.from(Instant.now()));
            employee.setModifiedBy(modelEmployee);

            // Update emergency contact record
            if (emergencyContact == null) {
                emergencyContact = new JpaEmergencyContact();
                emergencyContact.setEmergencyContactId(UUID.randomUUID().toString());
                emergencyContact.setDateCreated(Timestamp.from(Instant.now()));
                emergencyContact.setEmployee(employee);
                emergencyContact.setCreatedBy(modelEmployee);
                emergencyContact.setFullName(jpaEmployeeUpdateRequest.getEmergencyContactFullName());
                emergencyContact.setPhoneNumber(jpaEmployeeUpdateRequest.getEmergencyContactPhoneNumber());
                emergencyContact.setRelationship(jpaEmployeeUpdateRequest.getEmergencyContactRelationship());

                jpaApi.em().persist(emergencyContact);
            } else {
                emergencyContact.setDateModified(Timestamp.from(Instant.now()));
                emergencyContact.setModifiedBy(modelEmployee);
                emergencyContact.setFullName(jpaEmployeeUpdateRequest.getEmergencyContactFullName());
                emergencyContact.setPhoneNumber(jpaEmployeeUpdateRequest.getEmergencyContactPhoneNumber());
                emergencyContact.setRelationship(jpaEmployeeUpdateRequest.getEmergencyContactRelationship());
            }

            jpaEmployeeUpdateRequest.setApprovalStatus(EmployeeRequestStatus.COMPLETED);
        }
        return response.setValue(true);
    }

    /**
     * @author WARITH
     * @dateCreated 14/08/2023
     * @description Can an Employee-Update-Request
     *
     * @param employeeUpdateRequestId ID of the record to cancel
     * @param employeeId ID of the employee that made the request
     *
     * @return Response<Boolean>
     */
    @Override
    public Response<Boolean> cancelEmployeeUpdateRequest(String employeeUpdateRequestId, String employeeId) {
        JpaEmployeeUpdateRequest jpaEmployeeUpdateRequest = getJpaEmployeeUpdateRequest(employeeUpdateRequestId);
        Response<Boolean> response = new Response<>(false);

        if (jpaEmployeeUpdateRequest == null) {
            response.putError("employeeUpdateRequestId", "Update request not found");
            return response;
        }

        if (jpaEmployeeUpdateRequest.getApprovalStatus() != EmployeeRequestStatus.PENDING) {
            response.putError("Approval Status", "Request cannot be cancelled. I has been reviewed");
            return response;
        }

        if (!Objects.equals(jpaEmployeeUpdateRequest.getEmployee().getEmployeeId(), employeeId))
        {
            response.putError("employeeId", "Cannot cancel request not created by employee");
            return response;
        }

        jpaEmployeeUpdateRequest.setApprovalStatus(EmployeeRequestStatus.CANCELED);
        jpaEmployeeUpdateRequest.setDateModified(Timestamp.from(Instant.now()));
        return response.setValue(true);
    }

    /**
     * @author WARITH
     * @dateCreated 14/08/2023
     * @description Delete an Employee-Update-Request record
     *
     * @param employeeUpdateRequestId ID of the record to delete
     *
     * @return boolean
     */
    @Override
    public boolean deleteEmployeeUpdateRequest(String employeeUpdateRequestId) {
        return new JPAQueryFactory(jpaApi.em())
                .delete(qJpaEmployeeUpdateRequest)
                .where(qJpaEmployeeUpdateRequest.employeeUpdateRequestId.eq(employeeUpdateRequestId))
                .execute() == 1;
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
        if (employeeId == null)
            return null;
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
        if (employeeUpdateRequestId == null)
            return null;
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEmployeeUpdateRequest)
                .where(qJpaEmployeeUpdateRequest.employeeUpdateRequestId.eq(employeeUpdateRequestId))
                .fetchOne();
    }

    /**
     * @author WARITH
     * @dateCreated 14/08/2023
     * @description A helper method to fetch an employee emergency contact record
     *
     * @param employeeId ID of the employee to fetch emergency contact for
     *
     * @return JpaEmployeeUpdateRequest An employeeUpdateRequest record or null if not found
     */
    private JpaEmergencyContact getJpaEmergencyContact(String employeeId) {
        if (employeeId == null)
            return null;
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEmergencyContact)
                .where(qJpaEmergencyContact.employee.employeeId.eq(employeeId))
                .fetchOne();
    }

    /**
     * @author WARITH
     * @dateCreated 14/08/2023
     * @description A helper method to fetch an Option record
     *
     * @param optionId ID of the Option to fetch
     *
     * @return JpaOption An Option record or null if not found
     */
    private JpaOption getJpaOption(String optionId) {
        if (optionId == null)
            return null;
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaOption)
                .where(qJpaOption.optionId.eq(optionId))
                .fetchOne();
    }

    private void mapJpaEmployeeStatusUpdateRequestToJpaEmployee(JpaEmployeeUpdateRequest jpaEmployeeUpdateRequest, JpaEmployee jpaEmployee) {

        jpaEmployee.setAddress(jpaEmployeeUpdateRequest.getAddress());
        jpaEmployee.setDateOfBirth(jpaEmployeeUpdateRequest.getDateOfBirth());
        jpaEmployee.setFirstName(jpaEmployeeUpdateRequest.getFirstName());
        jpaEmployee.setLastName(jpaEmployeeUpdateRequest.getLastName());
        jpaEmployee.setPhoneNumber(jpaEmployeeUpdateRequest.getPhoneNumber());
        jpaEmployee.setCountryOfOrigin(jpaEmployeeUpdateRequest.getCountryOfOrigin());
        jpaEmployee.setEmployeeGender(jpaEmployeeUpdateRequest.getEmployeeGender());
        jpaEmployee.setEmployeeMaritalStatus(jpaEmployeeUpdateRequest.getEmployeeMaritalStatus());
        jpaEmployee.setHighestCertification(jpaEmployeeUpdateRequest.getHighestCertification());
        jpaEmployee.setStateOfOrigin(jpaEmployeeUpdateRequest.getStateOfOrigin());

    }
}
