package com.esl.internship.staffsync.employee.management.api;

import com.esl.internship.staffsync.employee.management.dto.DepartmentDTO;
import com.esl.internship.staffsync.employee.management.model.Department;
import com.esl.internship.staffsync.employee.management.model.Employee;

import java.util.List;
import java.util.Optional;

public interface IDepartmentApi {

    Department addDepartment(DepartmentDTO departmentDTO, Employee employee);

    Optional<Department> getDepartmentById(String departmentId);

    List<Department> getAllDepartments();

    boolean updateDepartment(String departmentId, DepartmentDTO departmentDto, Employee employee);

    boolean deleteDepartment(String departmentId);

}
