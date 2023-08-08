package com.esl.internship.staffsync.employee.management.api;


import com.encentral.staffsync.entity.enums.EmployeeStatus;
import com.esl.internship.staffsync.employee.management.dto.EmployeeDTO;
import com.esl.internship.staffsync.employee.management.model.Employee;

import java.util.List;
import java.util.Optional;


public interface IEmployeeApi {

    Employee addEmployee(EmployeeDTO employeeDTO, com.encentral.scaffold.commons.model.Employee employee);

    Optional<Employee> getEmployeeById(String employeeId);

    List<Employee> getAllEmployee();

    boolean updateEmployee(String employeeId, EmployeeDTO employeeDTO, com.encentral.scaffold.commons.model.Employee employee);

    boolean deleteEmployee(String employeeId);

    boolean setEmployeeStatus(String employeeId, EmployeeStatus employeeStatus, com.encentral.scaffold.commons.model.Employee employee);

    boolean markEmployeeActive(String employeeId, com.encentral.scaffold.commons.model.Employee employee);

    boolean markEmployeeInactive(String employeeId, com.encentral.scaffold.commons.model.Employee employee);

    boolean setEmployeeRole(String employeeId, String roleId, com.encentral.scaffold.commons.model.Employee employee);

    boolean setEmployeeProfilePictureUrl(String employeeId, String url, com.encentral.scaffold.commons.model.Employee employee);

    boolean setEmployeeLeaveDays(String employeeId, int leaveDays, com.encentral.scaffold.commons.model.Employee employee);

    boolean updateAnEmployeeRecordField(String fieldName, Object newValue, com.encentral.scaffold.commons.model.Employee employee);

}
