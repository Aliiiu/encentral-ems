package com.esl.internship.staffsync.authentication.impl;

import com.encentral.staffsync.entity.JpaEmployee;
import com.encentral.staffsync.entity.QJpaEmployee;
import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.impl.jwt.JwtUtil;
import com.esl.internship.staffsync.authentication.model.EmployeeAuthInfo;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static com.esl.internship.staffsync.authentication.model.AuthenticationMapper.INSTANCE;

public class AuthenticationImpl implements IAuthentication {

    private static final QJpaEmployee qJpaEmployee = QJpaEmployee.jpaEmployee;

    @Inject
    JPAApi jpaApi;

    @Override
    public Optional<String> signInEmployee(JpaEmployee jpaEmployee) {
        EmployeeAuthInfo employeeAuthInfo = INSTANCE.jpaEmployeeToEmployeeAuthInfo(jpaEmployee);
        updateEmployeeLastLogin(jpaEmployee.getEmployeeEmail());
        return Optional.ofNullable(JwtUtil.generateToken(employeeAuthInfo));
    }

    @Override
    public String generateInvalidPasswordMessage(int attempts) {
        int attemptsLeft = 5 - attempts;
        return String.format("Invalid details. You have %d attempt(s) left", attemptsLeft);
    }

    @Override
    public Optional<JpaEmployee> getJpaEmployeeByEmail(String email) {
        JpaEmployee j = new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEmployee)
                .where(qJpaEmployee.employeeEmail.eq(email))
                .fetchOne();
        return Optional.ofNullable(j);
    }

    @Override
    public boolean updateEmployeeLastLogin(String employeeEmail) {
        return new JPAQueryFactory(jpaApi.em()).update(qJpaEmployee)
                .set(qJpaEmployee.lastLogin, Timestamp.from(Instant.now()))
                .set(qJpaEmployee.loginAttempts, 0)
                .where(qJpaEmployee.employeeEmail.eq(employeeEmail))
                .execute() == 1;
    }

    @Override
    public boolean updateEmployeeLoginAttempts(String employeeId) {
        return new JPAQueryFactory(jpaApi.em()).update(qJpaEmployee)
                .set(qJpaEmployee.loginAttempts, qJpaEmployee.loginAttempts.add(Expressions.ONE))
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .execute() == 1;
    }

    @Override
    public boolean restrictAccount(String employeeEmail) {
        return new JPAQueryFactory(jpaApi.em()).update(qJpaEmployee)
                .set(qJpaEmployee.employeeActive, false)
                .set(qJpaEmployee.loginAttempts, 0)
                .where(qJpaEmployee.employeeEmail.eq(employeeEmail))
                .execute() == 1;
    }


    //TODO: Implement code to reset log in attempts on a periodic basis
}