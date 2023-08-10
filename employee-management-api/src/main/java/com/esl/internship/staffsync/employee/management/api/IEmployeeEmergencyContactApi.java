package com.esl.internship.staffsync.employee.management.api;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.employee.management.dto.EmergencyContactDTO;
import com.esl.internship.staffsync.employee.management.model.EmergencyContact;
import com.esl.internship.staffsync.commons.service.response.Response;

import java.util.List;
import java.util.Optional;

public interface IEmployeeEmergencyContactApi {

    Response<EmergencyContact> createEmergencyContact(String employeeId, EmergencyContactDTO emergencyContactDTO, Employee employee);

    Optional<EmergencyContact> getEmergencyContact(String emergencyContactId);

    List<EmergencyContact> getEmergencyContactsOfEmployee(String employeeId);

    List<EmergencyContact> getAllEmergencyContacts();

    boolean updateEmergencyContact(String emergencyContactId, EmergencyContactDTO emergencyContactDTO, Employee employee);

    boolean deleteEmergencyContact(String emergencyContactId);

}
