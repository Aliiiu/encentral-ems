package com.esl.internship.staffsync.employee.management.impl;

import com.encentral.scaffold.commons.model.Employee;
import com.encentral.staffsync.entity.*;
import com.esl.internship.staffsync.employee.management.service.response.Response;
import com.esl.internship.staffsync.employee.management.api.IDepartmentApi;
import com.esl.internship.staffsync.employee.management.api.IDepartmentHeadApi;
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

import static com.encentral.scaffold.commons.util.Utility.stringifyEmployee;
import static com.esl.internship.staffsync.employee.management.model.EmployeeManagementMapper.INSTANCE;


public class DefaultDepartmentApiImpl implements IDepartmentApi {

    @Inject
    JPAApi jpaApi;

    @Inject
    IDepartmentHeadApi iDepartmentHeadApi;

    private static final QJpaEmployee qJpaEmployee = QJpaEmployee.jpaEmployee;
    private static final QJpaDepartment qJpaDepartment = QJpaDepartment.jpaDepartment;
    private static final QJpaDepartmentHead qJpaDepartmentHead = QJpaDepartmentHead.jpaDepartmentHead;

    @Override
    public Response<Department> addDepartment(DepartmentDTO departmentDTO, Employee employee) {
        JpaDepartment jpaDepartment = INSTANCE.mapDepartment(departmentDTO);
        String employeeId = departmentDTO.getDepartmentHeadEmployeeId();
        JpaEmployee jpaEmployee = getJpaEmployee(employeeId);

        if (jpaEmployee == null) return new Response<Department>()
                .putError(
                        "departmentHeadEmployeeId",
                        "Cannot set department Head. Employee with Id does not exist"
                );

        jpaDepartment.setDepartmentId(UUID.randomUUID().toString());
        jpaDepartment.setCreatedBy(stringifyEmployee(employee));
        jpaDepartment.setDateCreated(Timestamp.from(Instant.now()));

        jpaApi.em().persist(jpaDepartment);

        JpaDepartmentHead departmentHead = iDepartmentHeadApi.createNewDepartmentHeadRecord(employee);
        departmentHead.setEmployee(jpaEmployee);
        departmentHead.setDepartment(jpaDepartment);

        jpaApi.em().persist(departmentHead);


        return new Response<Department>(INSTANCE.mapDepartment(jpaDepartment));
    }

    @Override
    public Optional<Department> getDepartmentById(String departmentId) {
        return Optional.ofNullable(
                INSTANCE.mapDepartment(getJpaDepartment(departmentId))
        );
    }

    @Override
    public List<Department> getAllDepartments() {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaDepartment)
                .fetch()
                .stream()
                .map(INSTANCE::mapDepartment)
                .collect(Collectors.toList());
    }

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

    @Override
    public boolean deleteDepartment(String departmentId) {
        return new JPAQueryFactory(jpaApi.em()).delete(qJpaDepartment)
                .where(qJpaDepartment.departmentId.eq(departmentId))
                .execute() == 1;
    }

    private JpaDepartment getJpaDepartment(String departmentId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaDepartment)
                .where(qJpaDepartment.departmentId.eq(departmentId))
                .fetchOne();
    }

    private JpaEmployee getJpaEmployee(String employeeId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .fetchOne();
    }

}
