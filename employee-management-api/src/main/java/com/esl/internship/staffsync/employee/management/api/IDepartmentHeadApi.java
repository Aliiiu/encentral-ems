package com.esl.internship.staffsync.employee.management.api;

import com.esl.internship.staffsync.employee.management.model.DepartmentHead;
import com.esl.internship.staffsync.employee.management.model.Employee;

import java.util.List;
import java.util.Optional;

public interface IDepartmentHeadApi {

    boolean setEmployeeAsDepartmentHead(String employeeId, String departmentId, Employee employee);

    boolean removeEmployeeAsDepartmentHead(String employeeId, String departmentId);

    Optional<DepartmentHead> getDepartmentHeadByDepartmentId(String departmentId);

    Optional<DepartmentHead> getDepartmentHeadById(String departmentHeadId);

    List<DepartmentHead> getAllDepartmentHeads();


}
