package com.esl.internship.staffsync.authentication.api;

import com.encentral.staffsync.entity.JpaEmployee;

import java.util.Optional;

public interface IAuthentication {

    String generateInvalidPasswordMessage(int attempts);

    boolean updateEmployeeLoginAttempts(String employeeEmail);

    Optional<String> signInEmployee(JpaEmployee jpaEmployee);

    boolean updateEmployeeLastLogin(String employeeId);

    boolean restrictAccount(String employeeEmail);

    Optional<JpaEmployee> getJpaEmployeeByEmail(String email);

}
