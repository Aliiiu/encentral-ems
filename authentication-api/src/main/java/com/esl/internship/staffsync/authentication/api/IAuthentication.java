package com.esl.internship.staffsync.authentication.api;

import com.esl.internship.staffsync.authentication.dto.LoginDTO;
import com.esl.internship.staffsync.authentication.model.AuthEmployeeSlice;
import com.esl.internship.staffsync.commons.exceptions.LoginAttemptExceededException;
import com.esl.internship.staffsync.commons.exceptions.InvalidCredentialsException;

import java.util.Optional;

public interface IAuthentication {

    Optional<String> signInEmployee(AuthEmployeeSlice authEmployeeSlice);

    void verifyEmployeeLogin(AuthEmployeeSlice employeeSlice, LoginDTO loginDTO) throws InvalidCredentialsException, LoginAttemptExceededException;

    Optional<AuthEmployeeSlice> getEmployeeSliceByEmail(String email);
}
