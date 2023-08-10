package com.esl.internship.staffsync.employee.management.impl;

import com.esl.internship.staffsync.entities.*;
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

import static com.esl.internship.staffsync.commons.util.Utility.stringifyEmployee;
import static com.esl.internship.staffsync.employee.management.model.EmployeeManagementMapper.INSTANCE;


public class DefaultDepartmentHeadApiImpl implements IDepartmentHeadApi {

    @Inject
    JPAApi jpaApi;

    private static final QJpaDepartmentHead qJpaDepartmentHead = QJpaDepartmentHead.jpaDepartmentHead;
    private static final QJpaDepartment qJpaDepartment = QJpaDepartment.jpaDepartment;
    private static final QJpaEmployee qJpaEmployee = QJpaEmployee.jpaEmployee;

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Set Employee as a department head
     *
     * @param employeeId ID of the employee
     * @param departmentId ID of the department
     * @param employee Employee creating the record
     *
     * @return boolean
     */
    @Override
    public boolean setEmployeeAsDepartmentHead(String employeeId, String departmentId, com.esl.internship.staffsync.commons.model.Employee employee) {
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

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Remove Employee as a department head
     *
     * @param employeeId ID of the employee
     * @param departmentId ID of the department
     *
     * @return boolean
     */
    @Override
    public boolean removeEmployeeAsDepartmentHead(String employeeId, String departmentId) {
        return new JPAQueryFactory(jpaApi.em()).delete(qJpaDepartmentHead)
                .where(qJpaDepartmentHead.department.departmentId.eq(departmentId))
                .where(qJpaDepartmentHead.employee.employeeId.eq(employeeId))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Gets departmentHead by ID
     *
     * @param departmentHeadId ID of the departmentHead to fetch
     *
     * @return Optional<DepartmentHead>
     */
    @Override
    public Optional<DepartmentHead> getDepartmentHeadById(String departmentHeadId) {
        return Optional.ofNullable(INSTANCE.mapDepartmentHead(getJpaDepartmentHead(departmentHeadId)));
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Gets the employee heading a department
     *
     * @param departmentId ID of the department
     *
     * @return Optional<Employee>
     */
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

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Get all departments headed by an Employee
     *
     * @param employeeId ID of the employee
     *
     * @return List<Department>
     */
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

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Gets all departmentHead records
     *
     * @return List<DepartmentHead>
     */
    @Override
    public List<DepartmentHead> getAllDepartmentHeads() {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaDepartmentHead)
                .fetch()
                .stream()
                .map(INSTANCE::mapDepartmentHead)
                .collect(Collectors.toList());
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

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description A helper method to fetch a departmentHead record from the database
     *
     * @param departmentHeadId ID of the departmentHead to fetch
     *
     * @return JpaDepartmentHead A departmentHead record or null if not found
     */
    private JpaDepartmentHead getJpaDepartmentHead(String departmentHeadId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaDepartmentHead)
                .where(qJpaDepartmentHead.departmentHeadId.eq(departmentHeadId))
                .fetchOne();
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description A helper method to create a new JpaDeparmentHead object
     *
     * @param employee Employee creating the record
     *
     * @return JpaDepartment
     */
    private JpaDepartmentHead createNewDepartmentHeadRecord(com.esl.internship.staffsync.commons.model.Employee employee) {
        JpaDepartmentHead jpaDepartmentHead = new JpaDepartmentHead();

        jpaDepartmentHead.setDepartmentHeadId(UUID.randomUUID().toString());
        jpaDepartmentHead.setCreatedBy(stringifyEmployee(employee));
        jpaDepartmentHead.setDateCreated(Timestamp.from(Instant.now()));

        return jpaDepartmentHead;
    }
}
