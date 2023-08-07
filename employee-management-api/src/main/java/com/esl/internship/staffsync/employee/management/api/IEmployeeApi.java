package com.esl.internship.staffsync.employee.management.api;

import com.encentral.staffsync.entity.enums.EmployeeStatus;
import com.esl.internship.staffsync.employee.management.dto.EmployeeDTO;
import com.esl.internship.staffsync.employee.management.model.Employee;

import java.util.List;
import java.util.Optional;

public interface IEmployeeApi {

    Employee addEmployee(EmployeeDTO employeeDTO, Employee creator);

    Optional<Employee> getEmployeeById(String employeeId);

    List<Employee> getAllEmployee();

    boolean updateEmployee(String employeeId, EmployeeDTO employeeDTO, Employee creator);

    boolean deleteEmployee(String employeeId);

    boolean setEmployeeStatus(String employeeId, EmployeeStatus employeeStatus, Employee modifier);

    boolean markEmployeeActive(String employeeId, Employee modifier);

    boolean markEmployeeInactive(String employeeId, Employee modifier);

    boolean setEmployeeRole(String employeeId, String roleId, Employee modifier);

    boolean setEmployeeProfilePictureUrl(String employeeId, String url, Employee modifier);

    boolean setEmployeeLeaveDays(String employeeId, int leaveDays, Employee modifier);

}
