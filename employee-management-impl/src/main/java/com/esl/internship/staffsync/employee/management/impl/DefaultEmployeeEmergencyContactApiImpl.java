package com.esl.internship.staffsync.employee.management.impl;


import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.entities.*;
import com.esl.internship.staffsync.employee.management.api.IEmployeeEmergencyContactApi;
import com.esl.internship.staffsync.employee.management.dto.EmergencyContactDTO;
import com.esl.internship.staffsync.employee.management.model.EmergencyContact;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.esl.internship.staffsync.commons.util.Utility.stringifyEmployee;
import static com.esl.internship.staffsync.employee.management.model.EmployeeManagementMapper.INSTANCE;


public class DefaultEmployeeEmergencyContactApiImpl implements IEmployeeEmergencyContactApi {

    @Inject
    JPAApi jpaApi;

    public static final QJpaEmergencyContact qJpaEmergencyContact = QJpaEmergencyContact.jpaEmergencyContact;
    public static final QJpaEmployee qJpaEmployee = QJpaEmployee.jpaEmployee;
    public static final QJpaOption qJpaOption = QJpaOption.jpaOption;

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Add a new Emergency Contact for an employee
     *
     * @param employeeId ID of the employee
     * @param emergencyContactDTO Emergency Contact data
     * @param employee making the change
     *
     * @return Response<EmergencyContact>
     */
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

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Get an Employee Emergency Contact
     *
     * @param emergencyContactId ID of the employee emergency contact
     *
     * @return boolean
     */
    @Override
    public Optional<EmergencyContact> getEmergencyContact(String emergencyContactId) {
        return Optional.ofNullable(INSTANCE.mapEmergencyContact(getJpaEmergencyContact(emergencyContactId)));
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Get all the Emergency Contacts of an employee
     *
     * @param employeeId ID of the employee
     *
     * @return List<EmergencyContact>
     */
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

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Get all Emergency contacts in the database
     *
     * @return List<EmergencyContact>
     */
    @Override
    public List<EmergencyContact> getAllEmergencyContacts() {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaEmergencyContact)
                .fetch()
                .stream()
                .map(INSTANCE::mapEmergencyContact)
                .collect(Collectors.toList());
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Update an emergency contact
     *
     * @param emergencyContactId ID of the employee
     * @param emergencyContactDTO Emergency Data to update
     * @param employee making the update
     *
     * @return boolean
     */
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

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Delete an emergency contact
     *
     * @param emergencyContactId ID of the employee emergency contact
     *
     * @return boolean
     */
    @Override
    public boolean deleteEmergencyContact(String emergencyContactId) {
        return new JPAQueryFactory(jpaApi.em())
                .delete(qJpaEmergencyContact)
                .where(qJpaEmergencyContact.emergencyContactId.eq(emergencyContactId))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description A helper method to fetch an emergencyContact record from the database
     *
     * @param emergencyContactId ID of the employee to fetch
     *
     * @return JpaEmergencyContact
     */
    private JpaEmergencyContact getJpaEmergencyContact(String emergencyContactId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEmergencyContact)
                .where(qJpaEmergencyContact.emergencyContactId.eq(emergencyContactId))
                .fetchOne();
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
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
     * @dateCreated 09/08/2023
     * @description A helper method to fetch an option record from the database
     *
     * @param optionId ID of the option to fetch
     *
     * @return JpaOption An option record or null if not found
     */
    private JpaOption getJpaOption(String optionId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaOption)
                .where(qJpaOption.optionId.eq(optionId))
                .fetchOne();
    }

}
