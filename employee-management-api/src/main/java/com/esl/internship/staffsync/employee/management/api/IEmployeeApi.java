package com.esl.internship.staffsync.employee.management.api;


import com.encentral.staffsync.entity.enums.EmployeeStatus;
import com.esl.internship.staffsync.employee.management.service.response.Response;
import com.esl.internship.staffsync.employee.management.dto.EmployeeDTO;
import com.esl.internship.staffsync.employee.management.model.Employee;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


public interface IEmployeeApi {

    Response<Employee> addEmployee(EmployeeDTO employeeDTO, com.encentral.scaffold.commons.model.Employee employee);

    Optional<Employee> getEmployeeById(String employeeId);

    List<Employee> getAllEmployee();

    boolean checkEmployeePassword(String employeeId, String password);

    boolean setEmployeePassword(String employeeId, String password, com.encentral.scaffold.commons.model.Employee employee);

    boolean deleteEmployee(String employeeId);

    boolean setEmployeeStatus(String employeeId, EmployeeStatus employeeStatus, com.encentral.scaffold.commons.model.Employee employee);

    boolean markEmployeeActive(String employeeId, com.encentral.scaffold.commons.model.Employee employee);

    boolean markEmployeeInactive(String employeeId, com.encentral.scaffold.commons.model.Employee employee);

    boolean setEmployeeRole(String employeeId, String roleId, com.encentral.scaffold.commons.model.Employee employee);

    boolean setEmployeeProfilePictureUrl(String employeeId, String url, com.encentral.scaffold.commons.model.Employee employee);

    boolean setEmployeeLeaveDays(String employeeId, int leaveDays, com.encentral.scaffold.commons.model.Employee employee);

    boolean employeeExists(String employeeId);

    boolean setLoginAttempts(String employeeId, int loginAttempt);

    boolean setLastLogin(String employeeId, Timestamp lastLogin);

}
