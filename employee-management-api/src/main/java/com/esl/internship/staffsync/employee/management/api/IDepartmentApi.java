package com.esl.internship.staffsync.employee.management.api;


import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.employee.management.service.response.Response;
import com.esl.internship.staffsync.employee.management.dto.DepartmentDTO;
import com.esl.internship.staffsync.employee.management.dto.UpdateDepartmentDTO;
import com.esl.internship.staffsync.employee.management.model.Department;

import java.util.List;
import java.util.Optional;


public interface IDepartmentApi {

    Response<Department> addDepartment(DepartmentDTO departmentDTO, Employee employee);

    Optional<Department> getDepartmentById(String departmentId);

    List<Department> getAllDepartments();

    boolean updateDepartment(String departmentId, UpdateDepartmentDTO departmentDto, Employee employee);

    boolean deleteDepartment(String departmentId);

}
