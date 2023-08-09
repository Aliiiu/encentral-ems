package com.esl.internship.staffsync.employee.management.impl;

import com.encentral.scaffold.commons.model.Employee;
import com.encentral.staffsync.entity.*;
import com.esl.internship.staffsync.employee.management.api.IEmployeeEmergencyContactApi;
import com.esl.internship.staffsync.employee.management.dto.EmergencyContactDTO;
import com.esl.internship.staffsync.employee.management.model.EmergencyContact;
import com.esl.internship.staffsync.employee.management.service.response.Response;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.encentral.scaffold.commons.util.Utility.stringifyEmployee;
import static com.esl.internship.staffsync.employee.management.model.EmployeeManagementMapper.INSTANCE;

public class DefaultEmployeeEmergencyContactApiImpl implements IEmployeeEmergencyContactApi {

    @Inject
    JPAApi jpaApi;

    public static final QJpaEmergencyContact qJpaEmergencyContact = QJpaEmergencyContact.jpaEmergencyContact;
    public static final QJpaEmployee qJpaEmployee = QJpaEmployee.jpaEmployee;
    public static final QJpaOption qJpaOption = QJpaOption.jpaOption;

    @Override
    public Response<EmergencyContact> createEmergencyContact(String employeeId, EmergencyContactDTO emergencyContactDTO, Employee employee) {
        JpaEmergencyContact jpaEmergencyContact = INSTANCE.mapEmergencyContact(emergencyContactDTO);
        Response<EmergencyContact> response = new Response<>();

        JpaEmployee jpaEmployee = getJpaEmployee(employeeId);
        if (jpaEmployee == null)
            response.putError("employeeId", "Employee does not exist");

        JpaOption contactGender = getJpaOption(emergencyContactDTO.getContactGenderOptionId());
        if(contactGender == null)
            response.putError("contactGenderOptionId", "Option does not exist");

        if (response.requestHasErrors())
            return response;

        jpaEmergencyContact.setEmployee(jpaEmployee);
        jpaEmergencyContact.setContactGender(contactGender);
        jpaEmergencyContact.setCreatedBy(stringifyEmployee(employee));
        jpaEmergencyContact.setDateCreated(Timestamp.from(Instant.now()));

        jpaApi.em().persist(jpaEmergencyContact);

        return response.setValue(INSTANCE.mapEmergencyContact(jpaEmergencyContact));

    }

    @Override
    public Optional<EmergencyContact> getEmergencyContact(String emergencyContactId) {
        return Optional.ofNullable(INSTANCE.mapEmergencyContact(getJpaEmergencyContact(emergencyContactId)));
    }

    @Override
    public List<EmergencyContact> getEmergencyContactsOfEmployee(String employeeId) {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaEmergencyContact)
                .where(qJpaEmergencyContact.employee.employeeId.eq(employeeId))
                .fetch()
                .stream()
                .map(INSTANCE::mapEmergencyContact)
                .collect(Collectors.toList());

    }

    @Override
    public List<EmergencyContact> getAllEmergencyContacts() {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaEmergencyContact)
                .fetch()
                .stream()
                .map(INSTANCE::mapEmergencyContact)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateEmergencyContact(String emergencyContactId, EmergencyContactDTO emergencyContactDTO, Employee employee) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaEmergencyContact)
                .where(qJpaEmergencyContact.emergencyContactId.eq(emergencyContactId))
                .set(qJpaEmergencyContact.address, emergencyContactDTO.getAddress())
                .set(qJpaEmergencyContact.contactGender.optionId, emergencyContactDTO.getContactGenderOptionId())
                .set(qJpaEmergencyContact.email, emergencyContactDTO.getEmail())
                .set(qJpaEmergencyContact.firstName, emergencyContactDTO.getFirstName())
                .set(qJpaEmergencyContact.lastName, emergencyContactDTO.getLastName())
                .set(qJpaEmergencyContact.relationship, emergencyContactDTO.getRelationship())
                .set(qJpaEmergencyContact.phoneNumber, emergencyContactDTO.getPhoneNumber())
                .set(qJpaEmergencyContact.modifiedBy, stringifyEmployee(employee))
                .set(qJpaEmergencyContact.dateModified, Timestamp.from(Instant.now()))
                .execute() == 1;
    }

    @Override
    public boolean deleteEmergencyContact(String emergencyContactId) {
        return new JPAQueryFactory(jpaApi.em())
                .delete(qJpaEmergencyContact)
                .where(qJpaEmergencyContact.emergencyContactId.eq(emergencyContactId))
                .execute() == 1;
    }

    private JpaEmergencyContact getJpaEmergencyContact(String emergencyContactId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEmergencyContact)
                .where(qJpaEmergencyContact.emergencyContactId.eq(emergencyContactId))
                .fetchOne();
    }

    private JpaEmployee getJpaEmployee(String employeeId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .fetchOne();
    }

    private JpaOption getJpaOption(String optionId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaOption)
                .where(qJpaOption.optionId.eq(optionId))
                .fetchOne();
    }

}
