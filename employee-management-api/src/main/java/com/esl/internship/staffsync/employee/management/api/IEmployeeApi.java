package com.esl.internship.staffsync.employee.management.api;


import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.employee.management.dto.EmployeeDTO;
import com.esl.internship.staffsync.entities.enums.EmployeeStatus;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


public interface IEmployeeApi {

    Response<Employee> addEmployee(EmployeeDTO employeeDTO, Employee employee);

    Optional<Employee> getEmployeeById(String employeeId);

    Optional<Employee> getEmployeeByEmail(String email);

    List<Employee> getAllEmployee();

    boolean checkEmployeePassword(String employeeId, String password);

    boolean setEmployeePassword(String employeeId, String password, Employee employee);

    boolean deleteEmployee(String employeeId);

    boolean setEmployeeStatus(String employeeId, EmployeeStatus employeeStatus, Employee employee);

    boolean markEmployeeActive(String employeeId, Employee employee);

    boolean markEmployeeInactive(String employeeId, Employee employee);

    boolean setEmployeeRole(String employeeId, String roleId, Employee employee);

    boolean setEmployeeProfilePictureUrl(String employeeId, String url, Employee employee);

    boolean setEmployeeLeaveDays(String employeeId, int leaveDays, Employee employee);

    boolean employeeExists(String employeeId);

    boolean employeeWithEmailExists(String email);

    boolean setLoginAttempts(String employeeId, int loginAttempt);

    boolean setLastLogin(String employeeId, Timestamp lastLogin);

}
