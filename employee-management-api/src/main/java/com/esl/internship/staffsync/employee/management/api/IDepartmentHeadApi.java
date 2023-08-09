package com.esl.internship.staffsync.employee.management.api;


import com.encentral.staffsync.entity.JpaDepartmentHead;
import com.esl.internship.staffsync.employee.management.model.Department;
import com.esl.internship.staffsync.employee.management.model.DepartmentHead;
import com.esl.internship.staffsync.employee.management.model.Employee;

import java.util.List;
import java.util.Optional;


public interface IDepartmentHeadApi {


    boolean setEmployeeAsDepartmentHead(String employeeId, String departmentId, com.encentral.scaffold.commons.model.Employee employee);

    boolean removeEmployeeAsDepartmentHead(String employeeId, String departmentId);

    Optional<DepartmentHead> getDepartmentHeadById(String departmentHeadId);

    Optional<Employee> getDepartmentHeadByDepartmentId(String departmentId);

    List<Department> getDepartmentsHeadByEmployee(String employeeId);

    List<DepartmentHead> getAllDepartmentHeads();

    JpaDepartmentHead createNewDepartmentHeadRecord(com.encentral.scaffold.commons.model.Employee employee);

}
