package com.esl.internship.staffsync.employee.management.impl;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.entities.*;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.employee.management.api.IDepartmentApi;
import com.esl.internship.staffsync.employee.management.dto.DepartmentDTO;
import com.esl.internship.staffsync.employee.management.dto.UpdateDepartmentDTO;
import com.esl.internship.staffsync.employee.management.model.Department;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.esl.internship.staffsync.commons.util.Utility.stringifyEmployee;
import static com.esl.internship.staffsync.employee.management.model.EmployeeManagementMapper.INSTANCE;

public class DefaultDepartmentApiImpl implements IDepartmentApi {

    @Inject
    JPAApi jpaApi;

    private static final QJpaEmployee qJpaEmployee = QJpaEmployee.jpaEmployee;
    private static final QJpaDepartment qJpaDepartment = QJpaDepartment.jpaDepartment;

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Creates a Department
     *
     * @param departmentDTO DTO for creating a leave request
     * @param employee Employee creating the record
     *
     * @return Response<Department>
     */
    @Override
    public Response<Department> addDepartment(DepartmentDTO departmentDTO, Employee employee) {
        JpaDepartment jpaDepartment = INSTANCE.mapDepartment(departmentDTO);

        jpaDepartment.setDepartmentId(UUID.randomUUID().toString());
        jpaDepartment.setCreatedBy(stringifyEmployee(employee));
        jpaDepartment.setDateCreated(Timestamp.from(Instant.now()));

        jpaApi.em().persist(jpaDepartment);

        return new Response<Department>(INSTANCE.mapDepartment(jpaDepartment));
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Gets a department by ID
     *
     * @param departmentId ID of the department to fetch
     *
     * @return Optional<Department>
     */
    @Override
    public Optional<Department> getDepartmentById(String departmentId) {
        return Optional.ofNullable(
                INSTANCE.mapDepartment(getJpaDepartment(departmentId))
        );
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Gets all department
     *
     * @return List<Department>
     */
    @Override
    public List<Department> getAllDepartments() {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaDepartment)
                .fetch()
                .stream()
                .map(INSTANCE::mapDepartment)
                .collect(Collectors.toList());
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Updates a department
     *
     * @param departmentId ID of the department to modify
     * @param departmentDto DTO for updating a leave request
     * @param employee Employee modifying the record
     *
     * @return boolean Returns true if operation is successful
     */
    @Override
    public boolean updateDepartment(String departmentId, UpdateDepartmentDTO departmentDto, Employee employee) {
        return new JPAQueryFactory(jpaApi.em()).update(qJpaDepartment)
                .where(qJpaDepartment.departmentId.eq(departmentId))
                .set(qJpaDepartment.departmentName, departmentDto.getDepartmentName())
                .set(qJpaDepartment.description, departmentDto.getDescription())
                .set(qJpaDepartment.workingHours, departmentDto.getWorkingHours())
                .set(qJpaDepartment.modifiedBy, stringifyEmployee(employee))
                .set(qJpaDepartment.dateModified, Timestamp.from(Instant.now()))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Deletes a department
     *
     * @param departmentId ID of the department to delete
     *
     * @return boolean Returns true if operation is successful
     */
    @Override
    public boolean deleteDepartment(String departmentId) {
        return new JPAQueryFactory(jpaApi.em()).delete(qJpaDepartment)
                .where(qJpaDepartment.departmentId.eq(departmentId))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description A helper method to fetch a department record from the database
     *
     * @param departmentId ID of the department to fetch
     *
     * @return JpaDepartment A department record or null if not found
     */
    private JpaDepartment getJpaDepartment(String departmentId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaDepartment)
                .where(qJpaDepartment.departmentId.eq(departmentId))
                .fetchOne();
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description A helper method to fetch an employee record from the database
     *
     * @param employeeId ID of the employee to fetch
     *
     * @return JpaEmployee An employee record or null if not found
     */
    private JpaEmployee getJpaEmployee(String employeeId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .fetchOne();
    }

}
