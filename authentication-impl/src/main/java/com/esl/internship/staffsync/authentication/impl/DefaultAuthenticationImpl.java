package com.esl.internship.staffsync.authentication.impl;

import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.dto.LoginDTO;
import com.esl.internship.staffsync.authentication.impl.jwt.JwtUtil;
import com.esl.internship.staffsync.authentication.model.AuthEmployeeSlice;
import com.esl.internship.staffsync.authentication.model.EmployeeAuthInfo;
import com.esl.internship.staffsync.commons.exceptions.InvalidCredentialsException;
import com.esl.internship.staffsync.commons.exceptions.LoginAttemptExceededException;
import com.esl.internship.staffsync.entities.JpaEmployee;
import com.esl.internship.staffsync.entities.QJpaEmployee;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static com.esl.internship.staffsync.authentication.model.AuthenticationMapper.INSTANCE;

/**
 * @author DEMILADE
 * @dateCreated 09/08/2023
 * @description General implementation of IAuthentication
 */
public class DefaultAuthenticationImpl implements IAuthentication {

    private static final QJpaEmployee qJpaEmployee = QJpaEmployee.jpaEmployee;

    @Inject
    JPAApi jpaApi;

    @Inject
    JwtUtil jwtUtil;


    /**
     * @author DEMILADE
     * @dateCreated 09/08/2023
     * @description Method to sign an employee in
     *
     * @param employeeSlice Object containing Employee details
     * @return Optional containing JWT
     */
    @Override
    public Optional<String> signInEmployee(AuthEmployeeSlice employeeSlice) {
        EmployeeAuthInfo employeeAuthInfo = INSTANCE.authEmployeeSliceToEmployeeAuthInfo(employeeSlice);
        updateEmployeeLastLogin(employeeSlice.getEmployeeEmail());
        return Optional.ofNullable(jwtUtil.generateToken(employeeAuthInfo));
    }

    /**
     * @author DEMILADE
     * @dateCreated 09/08/2023
     * @description Fetches employee details via email
     *
     * @param email Employee email
     *
     * @return Optional containing Employee details object
     */
    @Override
    public Optional<AuthEmployeeSlice> getEmployeeSliceByEmail(String email) {
        JpaEmployee j = new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEmployee)
                .where(qJpaEmployee.employeeEmail.eq(email))
                .fetchOne();
        AuthEmployeeSlice authEmployeeSlice = INSTANCE.jpaEmployeeToAuthEmployeeSlice(j);
        return Optional.ofNullable(authEmployeeSlice);
    }

    /**
     * @author DEMILADE
     * @dateCreated 10/08/2023
     * @description Method to verify an employee's password matches the input password
     *
     * @param employeeSlice Employee object
     * @param loginDTO DTO containing Log in info
     *
     * @throws InvalidCredentialsException Exception to be thrown for password mismatch
     * @throws LoginAttemptExceededException Exception to be thrown when user exceeds allowed login attempts
     */
    @Override
    public void verifyEmployeeLogin(AuthEmployeeSlice employeeSlice, LoginDTO loginDTO) throws InvalidCredentialsException, LoginAttemptExceededException {
        if (!employeeSlice.getPassword().equals(loginDTO.getPassword())) {
            int loginAttempts = employeeSlice.getLoginAttempts();
            if (loginAttempts >= 5) {
                restrictAccount(loginDTO.getEmployeeEmail());
                throw new LoginAttemptExceededException("Account has been restricted due to multiple failed log in attempts");
            }
            updateEmployeeLoginAttempts(loginDTO.getEmployeeEmail());
            String response = generateInvalidPasswordMessage(loginAttempts);
            throw new InvalidCredentialsException(response);
        }
    }

    /**
     * @author DEMILADE
     * @dateCreated 09/08/2023
     * @description Method to update an employee's last login date
     *
     * @param employeeEmail Employee email
     *
     * @return Boolean indicating success
     */
    private boolean updateEmployeeLastLogin(String employeeEmail) {
        return new JPAQueryFactory(jpaApi.em()).update(qJpaEmployee)
                .set(qJpaEmployee.lastLogin, Timestamp.from(Instant.now()))
                .set(qJpaEmployee.loginAttempts, 0)
                .where(qJpaEmployee.employeeEmail.eq(employeeEmail))
                .execute() == 1;
    }

    /**
     * @author DEMILADE
     * @dateCreated 09/08/2023
     * @description Method to update an employee's login attempts
     *
     * @param employeeEmail Employee email
     *
     * @return Boolean indicating success
     */
    private boolean updateEmployeeLoginAttempts(String employeeEmail) {
        return new JPAQueryFactory(jpaApi.em()).update(qJpaEmployee)
                .set(qJpaEmployee.loginAttempts, qJpaEmployee.loginAttempts.add(Expressions.ONE))
                .where(qJpaEmployee.employeeEmail.eq(employeeEmail))
                .execute() == 1;
    }

    /**
     * @author DEMILADE
     * @dateCreated 09/08/2023
     * @description Method to restrict an account after login attempts have been exceeded
     *
     * @param employeeEmail Employee email
     *
     * @return Boolean indicating success
     */
    private boolean restrictAccount(String employeeEmail) {
        return new JPAQueryFactory(jpaApi.em()).update(qJpaEmployee)
                .set(qJpaEmployee.employeeActive, false)
                .set(qJpaEmployee.loginAttempts, 0)
                .where(qJpaEmployee.employeeEmail.eq(employeeEmail))
                .execute() == 1;
    }

    /**
     * @author DEMILADE
     * @dateCreated 09/08/2023
     * @description Generates an error message for attempted logins with a wrong password
     *
     * @param attempts Number of attempted logins
     *
     * @return Error message
     */
    private String generateInvalidPasswordMessage(int attempts) {
        int attemptsLeft = 5 - (attempts + 1);
        return String.format("Invalid details. You have %d attempt(s) left", attemptsLeft);
    }


    //TODO: Implement code to reset log in attempts on a periodic basis
}