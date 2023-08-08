package com.esl.internship.staffsync.employee.management.api;


import com.encentral.scaffold.commons.model.Employee;
import com.esl.internship.staffsync.employee.management.model.Department;
import com.esl.internship.staffsync.employee.management.model.DepartmentHead;

import java.util.List;
import java.util.Optional;


public interface IDepartmentHeadApi {


    boolean setEmployeeAsDepartmentHead(String employeeId, String departmentId, Employee employee);

    boolean removeEmployeeAsDepartmentHead(String employeeId, String departmentId);

    Optional<DepartmentHead> getDepartmentHeadById(String departmentHeadId);

    Optional<DepartmentHead> getDepartmentHeadByDepartmentId(String departmentId);

    List<Department> getDepartmentsHeadByEmployee(String employeeId);

    List<DepartmentHead> getAllDepartmentHeads();

}
