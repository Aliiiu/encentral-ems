package com.esl.internship.staffsync.employee.management.impl;

import com.encentral.staffsync.entity.*;
import com.esl.internship.staffsync.employee.management.api.IDepartmentHeadApi;
import com.esl.internship.staffsync.employee.management.model.Department;
import com.esl.internship.staffsync.employee.management.model.DepartmentHead;
import com.esl.internship.staffsync.employee.management.model.Employee;
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

public class DefaultDepartmentHeadApiImpl implements IDepartmentHeadApi {

    @Inject
    JPAApi jpaApi;

    private static final QJpaDepartmentHead qJpaDepartmentHead = QJpaDepartmentHead.jpaDepartmentHead;
    private static final QJpaDepartment qJpaDepartment = QJpaDepartment.jpaDepartment;
    private static final QJpaEmployee qJpaEmployee = QJpaEmployee.jpaEmployee;

    @Override
    public boolean setEmployeeAsDepartmentHead(String employeeId, String departmentId, com.encentral.scaffold.commons.model.Employee employee) {
        JpaEmployee jpaEmployee = getJpaEmployee(employeeId);
        JpaDepartment jpaDepartment = getJpaDepartment(departmentId);

        if (jpaEmployee == null || jpaDepartment == null)
            return false;

        JpaDepartmentHead departmentHead = createNewDepartmentHeadRecord(employee);
        departmentHead.setEmployee(jpaEmployee);
        departmentHead.setDepartment(jpaDepartment);

        jpaApi.em().persist(departmentHead);

        return true;
    }

    @Override
    public boolean removeEmployeeAsDepartmentHead(String employeeId, String departmentId) {
        return new JPAQueryFactory(jpaApi.em()).delete(qJpaDepartmentHead)
                .where(qJpaDepartmentHead.department.departmentId.eq(departmentId))
                .where(qJpaDepartmentHead.employee.employeeId.eq(employeeId))
                .execute() == 1;
    }

    @Override
    public Optional<DepartmentHead> getDepartmentHeadById(String departmentHeadId) {
        return Optional.ofNullable(INSTANCE.mapDepartmentHead(getJpaDepartmentHead(departmentHeadId)));
    }

    @Override
    public Optional<Employee> getDepartmentHeadByDepartmentId(String departmentId) {
        return Optional.ofNullable(
                INSTANCE.mapEmployee(
                        new JPAQueryFactory(jpaApi.em())
                                .selectFrom(qJpaDepartmentHead)
                                .select(qJpaDepartmentHead.employee)
                                .where(qJpaDepartmentHead.department.departmentId.eq(departmentId))
                                .fetchOne()
                )
        );
    }

    @Override
    public List<Department> getDepartmentsHeadByEmployee(String employeeId) {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaDepartmentHead)
                .select(qJpaDepartmentHead.department)
                .where(qJpaDepartmentHead.employee.employeeId.eq(employeeId))
                .fetch()
                .stream()
                .map(INSTANCE::mapDepartment)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentHead> getAllDepartmentHeads() {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaDepartmentHead)
                .fetch()
                .stream()
                .map(INSTANCE::mapDepartmentHead)
                .collect(Collectors.toList());
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

    private JpaDepartmentHead getJpaDepartmentHead(String departmentHeadId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaDepartmentHead)
                .where(qJpaDepartmentHead.departmentHeadId.eq(departmentHeadId))
                .fetchOne();
    }

    @Override
    public JpaDepartmentHead createNewDepartmentHeadRecord(com.encentral.scaffold.commons.model.Employee employee) {
        JpaDepartmentHead jpaDepartmentHead = new JpaDepartmentHead();

        jpaDepartmentHead.setDepartmentHeadId(UUID.randomUUID().toString());
        jpaDepartmentHead.setCreatedBy(stringifyEmployee(employee));
        jpaDepartmentHead.setDateCreated(Timestamp.from(Instant.now()));

        return jpaDepartmentHead;
    }
}
