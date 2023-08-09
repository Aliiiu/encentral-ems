package com.esl.internship.staffsync.authentication.api;

import com.esl.internship.staffsync.authentication.model.AuthEmployeeSlice;
import com.esl.internship.staffsync.entities.JpaEmployee;

import java.util.Optional;

public interface IAuthentication {

    String generateInvalidPasswordMessage(int attempts);

    boolean updateEmployeeLoginAttempts(String employeeEmail);

    Optional<String> signInEmployee(AuthEmployeeSlice authEmployeeSlice);

    boolean updateEmployeeLastLogin(String employeeEmail);

    boolean restrictAccount(String employeeEmail);

    Optional<AuthEmployeeSlice> getEmployeeSliceByEmail(String email);
}
