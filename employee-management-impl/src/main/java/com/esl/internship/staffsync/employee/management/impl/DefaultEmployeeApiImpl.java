package com.esl.internship.staffsync.employee.management.impl;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.entities.*;
import com.esl.internship.staffsync.entities.enums.EmployeeStatus;
import com.esl.internship.staffsync.employee.management.api.IPasswordManagementApi;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.employee.management.api.IEmployeeApi;
import com.esl.internship.staffsync.employee.management.dto.EmployeeDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static com.esl.internship.staffsync.commons.util.Utility.stringifyEmployee;
import static com.esl.internship.staffsync.employee.management.model.EmployeeManagementMapper.INSTANCE;

public class DefaultEmployeeApiImpl implements IEmployeeApi {

    @Inject
    JPAApi jpaApi;

    @Inject
    IPasswordManagementApi iPasswordManagementApi;

    private static final QJpaRole qJpaRole = QJpaRole.jpaRole;
    private static final QJpaDepartment qJpaDepartment = QJpaDepartment.jpaDepartment;
    private static final QJpaEmployee qJpaEmployee = QJpaEmployee.jpaEmployee;
    private static final QJpaOption qJpaOption = QJpaOption.jpaOption;

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Add a new Employee record to the database
     *
     * @param employeeDto Employee data
     * @param employee Employee creating the record
     *
     * @return JpaEmployee An employee record or null if not found
     */
    @Override
    public Response<Employee> addEmployee(EmployeeDTO employeeDto, Employee employee) {
        JpaEmployee jpaEmployee = INSTANCE.mapEmployee(employeeDto);

        Response<Employee> response = new Response<>();

        if (employeeWithEmailExists(employeeDto.getEmployeeEmail()))
        {
            response.putError("employeeEmail", "Email Exists!");
            return response;
        }

        JpaRole role = getJpaRole(employeeDto.getRoleId());
        if (role == null)
            response.putError("roleId", "Role does not exist");

        JpaDepartment department = null;
        if (employeeDto.getDepartmentId() != null) {
            department = getJpaDepartment(employeeDto.getDepartmentId());

            if (department == null)
                response.putError("departmentId", "Department does not exist");
        }

        JpaOption employeeGender = getJpaOption(employeeDto.getEmployeeGenderOptionId());
        if (employeeGender == null)
            response.putError("employeeGenderOptionId", "Option does not exist");

        JpaOption stateOfOrigin = null;
        if (employeeDto.getStateOfOriginOptionId() != null) {
            stateOfOrigin = getJpaOption(employeeDto.getStateOfOriginOptionId());

            if (stateOfOrigin == null)
                response.putError("stateOfOriginOptionId", "Option does not exist");
        }

        JpaOption countryOfOrigin = getJpaOption(employeeDto.getCountryOfOriginOptionId());
        if (countryOfOrigin == null)
            response.putError("countryOfOriginOptionId", "Option does not exist");

        JpaOption highestCertification = null;
        if (employeeDto.getHighestCertificationOptionId() != null) {
            highestCertification = getJpaOption(employeeDto.getHighestCertificationOptionId());
            if (highestCertification == null)
                response.putError("highestCertificationOptionId", "Option does not exist");
        }

        JpaOption employeeMaritalStatus = null;
        if (employeeDto.getEmployeeMaritalStatusOptionId() != null) {
            employeeMaritalStatus = getJpaOption(employeeDto.getEmployeeMaritalStatusOptionId());
            if (employeeMaritalStatus == null)
                response.putError("employeeMaritalStatusOptionId", "Option does not exist");
        }


        if (response.requestHasErrors())
            return response;

        jpaEmployee.setEmployeeId(generateId(employeeDto));
        jpaEmployee.setRole(role);
        jpaEmployee.setDepartment(department);
        jpaEmployee.setEmployeeGender(employeeGender);
        jpaEmployee.setStateOfOrigin(stateOfOrigin);
        jpaEmployee.setCountryOfOrigin(countryOfOrigin);
        jpaEmployee.setHighestCertification(highestCertification);
        jpaEmployee.setEmployeeMaritalStatus(employeeMaritalStatus);
        jpaEmployee.setCreatedBy(stringifyEmployee(employee));
        jpaEmployee.setDateCreated(Timestamp.from(Instant.now()));
        setPassword(jpaEmployee, employeeDto.getPassword());

        jpaApi.em().persist(jpaEmployee);

        return response.setValue(INSTANCE.mapEmployee(jpaEmployee));
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Get an employee record by ID
     *
     * @param employeeId ID of the employee to fetch
     *
     * @return Optional<Employee>
     */
    @Override
    public Optional<Employee> getEmployeeById(String employeeId) {
        return Optional.ofNullable(INSTANCE.mapEmployee(getJpaEmployee(employeeId)));
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Get an employee record by email
     *
     * @param email Email of the employee to fetch
     *
     * @return Optional<Employee>
     */
    @Override
    public Optional<Employee> getEmployeeByEmail(String email) {
        return Optional.ofNullable(INSTANCE.mapEmployee(getJpaEmployeeByEmail(email)));
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Get all employee records
     *
     * @return List<Employee>
     */
    @Override
    public List<Employee> getAllEmployee() {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaEmployee)
                .fetch()
                .stream()
                .map(INSTANCE::mapEmployee)
                .collect(Collectors.toList());
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Check the password of an employee
     *
     * @param employeeId ID of the employee
     * @param password Password to check
     *
     * @return boolean
     */
    @Override
    public boolean checkEmployeePassword(String employeeId, String password) {
        JpaEmployee jpaEmployee = getJpaEmployee(employeeId);
        if (jpaEmployee != null)
            return iPasswordManagementApi.verifyPassword(jpaEmployee.getPassword(), password);
        return false;
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description set the password of an employee
     *
     * @param employeeId ID of the employee
     * @param password The new Password
     * @param employee Employee making the change
     *
     * @return boolean
     */
    @Override
    public boolean setEmployeePassword(String employeeId, String password, Employee employee) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .set(qJpaEmployee.modifiedBy, stringifyEmployee(employee))
                .set(qJpaEmployee.dateModified, Timestamp.from(Instant.now()))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Delete an employee
     *
     * @param employeeId ID of the employee to delete
     *
     * @return boolean
     */
    @Override
    public boolean deleteEmployee(String employeeId) {
        return new JPAQueryFactory(jpaApi.em())
                .delete(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Set the status of an employee
     *
     * @param employeeId ID of the employee
     * @param employeeStatus Status
     * @param employee Employee making the change
     *
     * @return boolean
     */
    @Override
    public boolean setEmployeeStatus(String employeeId, EmployeeStatus employeeStatus, Employee employee) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .set(qJpaEmployee.currentStatus, employeeStatus)
                .set(qJpaEmployee.modifiedBy, stringifyEmployee(employee))
                .set(qJpaEmployee.dateModified, Timestamp.from(Instant.now()))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Mark an employee Active
     *
     * @param employeeId ID of the employee
     * @param employee making the change
     *
     * @return boolean
     */
    @Override
    public boolean markEmployeeActive(String employeeId, Employee employee) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .set(qJpaEmployee.employeeActive, true)
                .set(qJpaEmployee.modifiedBy, stringifyEmployee(employee))
                .set(qJpaEmployee.dateModified, Timestamp.from(Instant.now()))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Mark an employee Inactive
     *
     * @param employeeId ID of the employee
     * @param employee Employee making the change
     *
     * @return boolean
     */
    @Override
    public boolean markEmployeeInactive(String employeeId, Employee employee) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .set(qJpaEmployee.employeeActive, false)
                .set(qJpaEmployee.modifiedBy, stringifyEmployee(employee))
                .set(qJpaEmployee.dateModified, Timestamp.from(Instant.now()))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Set the role of an employee
     *
     * @param employeeId ID of the employee
     * @param roleId  ID of the role
     * @param employee Employee making the change
     *
     * @return boolean
     */
    @Override
    public boolean setEmployeeRole(String employeeId, String roleId, Employee employee) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .set(qJpaEmployee.role.roleId, roleId)
                .set(qJpaEmployee.modifiedBy, stringifyEmployee(employee))
                .set(qJpaEmployee.dateModified, Timestamp.from(Instant.now()))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Sets the profile picture url
     *
     * @param employeeId ID of the employee
     *
     * @return boolean
     */
    @Override
    public boolean setEmployeeProfilePictureUrl(String employeeId, String url, Employee employee) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .set(qJpaEmployee.profilePictureUrl, url)
                .set(qJpaEmployee.modifiedBy, stringifyEmployee(employee))
                .set(qJpaEmployee.dateModified, Timestamp.from(Instant.now()))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Set employee leave days
     *
     * @param employeeId ID of the employee
     * @param leaveDays Number of days of leave
     * @param employee Employee making the change
     *
     * @return boolean
     */
    @Override
    public boolean setEmployeeLeaveDays(String employeeId, int leaveDays, Employee employee) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .set(qJpaEmployee.leaveDays, leaveDays)
                .set(qJpaEmployee.modifiedBy, stringifyEmployee(employee))
                .set(qJpaEmployee.dateModified, Timestamp.from(Instant.now()))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Check if an employee exists
     *
     * @param employeeId ID of the employee
     *
     * @return boolean
     */
    @Override
    public boolean employeeExists(String employeeId) {
        return getJpaEmployee(employeeId) != null;
    }

    /**
     * @author WARITH
     * @dateCreated 10/08/2023
     * @description Check if an employee exists by email
     *
     * @param email email of the employee
     *
     * @return boolean
     */
    @Override
    public boolean employeeWithEmailExists(String email) {
        return getJpaEmployeeByEmail(email) != null;
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Set employee login attempts
     *
     * @param employeeId ID of the employee
     * @param loginAttempt Number of login Attempts
     *
     * @return boolean
     */
    @Override
    public boolean setLoginAttempts(String employeeId, int loginAttempt) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .set(qJpaEmployee.loginAttempts, loginAttempt)
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Set employee last login
     *
     * @param employeeId ID of the employee
     * @param lastLogin Timestamp of last login
     *
     * @return boolean
     */
    @Override
    public boolean setLastLogin(String employeeId, Timestamp lastLogin) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .set(qJpaEmployee.lastLogin, lastLogin)
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Helper method to set employee password
     *
     * @param employee The employee Jpa Entity
     * @param password The password to set
     *
     * @return boolean
     */
    private void setPassword(JpaEmployee employee, String password) {
        employee.setPassword(iPasswordManagementApi.hashPassword(password));
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Generate ID for employee
     *
     * @param employeeDto Data of the new employee to be created
     *
     * @return boolean
     */
    private String generateId(EmployeeDTO employeeDto) {
        // ESL-{Employee Initial}-{Date Hired}-{Three Random Digits}

        LocalDate dateHired = LocalDate.from(employeeDto.getDateHired().toInstant().atZone(ZoneId.systemDefault()));

        StringBuilder sb = new StringBuilder("ESL-");
        sb.append(employeeDto.getFirstName().charAt(0));
        sb.append(employeeDto.getLastName().charAt(0));
        sb.append(dateHired.getDayOfMonth());
        sb.append(dateHired.getMonth());
        sb.append(dateHired.getYear());
        sb.append(new Random().nextInt(900) + 100);

        return sb.toString();
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
     * @dateCreated 10/08/2023
     * @description A helper method to fetch an employee record from the database by email
     *
     * @param email ID of the employee to fetch
     *
     * @return JpaEmployee An employee record or null if not found
     */
    private JpaEmployee getJpaEmployeeByEmail(String email) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEmployee)
                .where(qJpaEmployee.employeeEmail.eq(email))
                .fetchOne();
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description A helper method to fetch a role record from the database
     *
     * @param roleId ID of the role to fetch
     *
     * @return JpaRole A role record or null if not found
     */
    private JpaRole getJpaRole(String roleId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaRole)
                .where(qJpaRole.roleId.eq(roleId))
                .fetchOne();
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description A helper method to fetch an option record from the database
     *
     * @param optionId ID of the option to fetch
     *
     * @return JpaOption An option record or null if not found
     */
    private JpaOption getJpaOption(String optionId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaOption)
                .where(qJpaOption.optionId.eq(optionId))
                .fetchOne();
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description A helper method to fetch a department record from the database
     *
     * @param departmentId ID of the employee to fetch
     *
     * @return JpaDepartment An employee record or null if not found
     */
    private JpaDepartment getJpaDepartment(String departmentId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaDepartment)
                .where(qJpaDepartment.departmentId.eq(departmentId))
                .fetchOne();
    }

}
