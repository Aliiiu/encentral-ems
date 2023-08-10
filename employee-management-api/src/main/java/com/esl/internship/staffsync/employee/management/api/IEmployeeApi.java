package com.esl.internship.staffsync.employee.management.api;


import com.esl.internship.staffsync.employee.management.service.response.Response;
import com.esl.internship.staffsync.employee.management.dto.EmployeeDTO;
import com.esl.internship.staffsync.employee.management.model.Employee;
import com.esl.internship.staffsync.entities.enums.EmployeeStatus;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


public interface IEmployeeApi {

    Response<Employee> addEmployee(EmployeeDTO employeeDTO, com.esl.internship.staffsync.commons.model.Employee employee);

    Optional<Employee> getEmployeeById(String employeeId);

    List<Employee> getAllEmployee();

    boolean checkEmployeePassword(String employeeId, String password);

    boolean setEmployeePassword(String employeeId, String password, com.esl.internship.staffsync.commons.model.Employee employee);

    boolean deleteEmployee(String employeeId);

    boolean setEmployeeStatus(String employeeId, EmployeeStatus employeeStatus, com.esl.internship.staffsync.commons.model.Employee employee);

    boolean markEmployeeActive(String employeeId, com.esl.internship.staffsync.commons.model.Employee employee);

    boolean markEmployeeInactive(String employeeId, com.esl.internship.staffsync.commons.model.Employee employee);

    boolean setEmployeeRole(String employeeId, String roleId, com.esl.internship.staffsync.commons.model.Employee employee);

    boolean setEmployeeProfilePictureUrl(String employeeId, String url, com.esl.internship.staffsync.commons.model.Employee employee);

    boolean setEmployeeLeaveDays(String employeeId, int leaveDays, com.esl.internship.staffsync.commons.model.Employee employee);

    boolean employeeExists(String employeeId);

    boolean setLoginAttempts(String employeeId, int loginAttempt);

    boolean setLastLogin(String employeeId, Timestamp lastLogin);

}
