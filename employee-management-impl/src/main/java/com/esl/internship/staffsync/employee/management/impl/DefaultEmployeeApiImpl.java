package com.esl.internship.staffsync.employee.management.impl;

import com.encentral.staffsync.entity.*;
import com.encentral.staffsync.entity.enums.EmployeeStatus;
import com.esl.internship.staffsync.employee.management.service.response.Response;
import com.esl.internship.staffsync.employee.management.api.IEmployeeApi;
import com.esl.internship.staffsync.employee.management.dto.EmployeeDTO;
import com.esl.internship.staffsync.employee.management.model.Employee;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static com.encentral.scaffold.commons.util.Utility.stringifyEmployee;
import static com.esl.internship.staffsync.employee.management.model.EmployeeManagementMapper.INSTANCE;

public class DefaultEmployeeApiImpl implements IEmployeeApi {

    @Inject
    JPAApi jpaApi;

    private static final QJpaRole qJpaRole = QJpaRole.jpaRole;
    private static final QJpaDepartment qJpaDepartment = QJpaDepartment.jpaDepartment;
    private static final QJpaEmployee qJpaEmployee = QJpaEmployee.jpaEmployee;
    private static final QJpaOption qJpaOption = QJpaOption.jpaOption;

    @Override
    public Response<Employee> addEmployee(EmployeeDTO employeeDto, com.encentral.scaffold.commons.model.Employee employee) {
        JpaEmployee jpaEmployee = INSTANCE.mapEmployee(employeeDto);

        Response<Employee> response = new Response<>();

        JpaRole role = getJpaRole(employeeDto.getRoleId());
        if (role == null)
            response.putError("roleId", "Role does not exist");

        JpaDepartment department = getJpaDepartment(employeeDto.getDepartmentId());
        if (department == null )
            response.putError("departmentId", "Department does not exist");

        JpaOption employeeGender = getJpaOption(employeeDto.getEmployeeGenderOptionId());
        if (employeeGender == null)
            response.putError("employeeGenderOptionId", "Option does not exist");

        JpaOption stateOfOrigin = getJpaOption(employeeDto.getStateOfOriginOptionId());
        if (stateOfOrigin == null)
            response.putError("stateOfOriginOptionId", "Option does not exist");

        JpaOption countryOfOrigin = getJpaOption(employeeDto.getCountryOfOriginOptionId());
        if (stateOfOrigin == null)
            response.putError("countryOfOriginOptionId", "Option does not exist");

        JpaOption highestCertification = getJpaOption(employeeDto.getHighestCertificationOptionId());
        if (stateOfOrigin == null)
            response.putError("highestCertificationOptionId", "Option does not exist");

        JpaOption employeeMaritalStatus = getJpaOption(employeeDto.getEmployeeMaritalStatusOptionId());
        if (stateOfOrigin == null)
            response.putError("employeeMaritalStatusOptionId", "Option does not exist");


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

    @Override
    public Optional<Employee> getEmployeeById(String employeeId) {
        return Optional.ofNullable(INSTANCE.mapEmployee(getJpaEmployee(employeeId)));
    }

    @Override
    public List<Employee> getAllEmployee() {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaEmployee)
                .fetch()
                .stream()
                .map(INSTANCE::mapEmployee)
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkEmployeePassword(String employeeId, String password) {
        JpaEmployee jpaEmployee = getJpaEmployee(employeeId);
        if (jpaEmployee != null)
            return checkPassword(jpaEmployee.getPassword(), password);
        return false;
    }

    @Override
    public boolean setEmployeePassword(String employeeId, String password, com.encentral.scaffold.commons.model.Employee employee) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .set(qJpaEmployee.password, hashPassword(password))
                .set(qJpaEmployee.modifiedBy, stringifyEmployee(employee))
                .set(qJpaEmployee.dateModified, Timestamp.from(Instant.now()))
                .execute() == 1;
    }

    @Override
    public boolean deleteEmployee(String employeeId) {
        return new JPAQueryFactory(jpaApi.em())
                .delete(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .execute() == 1;
    }

    @Override
    public boolean setEmployeeStatus(String employeeId, EmployeeStatus employeeStatus, com.encentral.scaffold.commons.model.Employee employee) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .set(qJpaEmployee.currentStatus, employeeStatus)
                .set(qJpaEmployee.modifiedBy, stringifyEmployee(employee))
                .set(qJpaEmployee.dateModified, Timestamp.from(Instant.now()))
                .execute() == 1;
    }

    @Override
    public boolean markEmployeeActive(String employeeId, com.encentral.scaffold.commons.model.Employee employee) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .set(qJpaEmployee.employeeActive, true)
                .set(qJpaEmployee.modifiedBy, stringifyEmployee(employee))
                .set(qJpaEmployee.dateModified, Timestamp.from(Instant.now()))
                .execute() == 1;
    }

    @Override
    public boolean markEmployeeInactive(String employeeId, com.encentral.scaffold.commons.model.Employee employee) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .set(qJpaEmployee.employeeActive, false)
                .set(qJpaEmployee.modifiedBy, stringifyEmployee(employee))
                .set(qJpaEmployee.dateModified, Timestamp.from(Instant.now()))
                .execute() == 1;
    }

    @Override
    public boolean setEmployeeRole(String employeeId, String roleId, com.encentral.scaffold.commons.model.Employee employee) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .set(qJpaEmployee.role.roleId, roleId)
                .set(qJpaEmployee.modifiedBy, stringifyEmployee(employee))
                .set(qJpaEmployee.dateModified, Timestamp.from(Instant.now()))
                .execute() == 1;
    }

    @Override
    public boolean setEmployeeProfilePictureUrl(String employeeId, String url, com.encentral.scaffold.commons.model.Employee employee) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .set(qJpaEmployee.profilePictureUrl, url)
                .set(qJpaEmployee.modifiedBy, stringifyEmployee(employee))
                .set(qJpaEmployee.dateModified, Timestamp.from(Instant.now()))
                .execute() == 1;
    }

    @Override
    public boolean setEmployeeLeaveDays(String employeeId, int leaveDays, com.encentral.scaffold.commons.model.Employee employee) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .set(qJpaEmployee.leaveDays, leaveDays)
                .set(qJpaEmployee.modifiedBy, stringifyEmployee(employee))
                .set(qJpaEmployee.dateModified, Timestamp.from(Instant.now()))
                .execute() == 1;
    }

    @Override
    public boolean employeeExists(String employeeId) {
        return getJpaEmployee(employeeId) != null;
    }

    @Override
    public boolean setLoginAttempts(String employeeId, int loginAttempt) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .set(qJpaEmployee.loginAttempts, loginAttempt)
                .execute() == 1;
    }

    @Override
    public boolean setLastLogin(String employeeId, Timestamp lastLogin) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .set(qJpaEmployee.lastLogin, lastLogin)
                .execute() == 1;
    }

    private void setPassword(JpaEmployee employee, String password) {
        employee.setPassword(hashPassword(password));
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to hexadecimal format
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean checkPassword(String employeePasswordHash, String password) {
        return employeePasswordHash.equals(hashPassword(password));
    }

    private String generateId(EmployeeDTO employeeDto) {
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

    private JpaEmployee getJpaEmployee(String employeeId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .fetchOne();
    }

    private JpaRole getJpaRole(String roleId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaRole)
                .where(qJpaRole.roleId.eq(roleId))
                .fetchOne();
    }

    private JpaOption getJpaOption(String optionId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaOption)
                .where(qJpaOption.optionId.eq(optionId))
                .fetchOne();
    }

    private JpaDepartment getJpaDepartment(String departmentId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaDepartment)
                .where(qJpaDepartment.departmentId.eq(departmentId))
                .fetchOne();
    }

}
