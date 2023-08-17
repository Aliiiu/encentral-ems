package com.esl.internship.staffsync.authentication.api;

import com.esl.internship.staffsync.authentication.dto.LoginDTO;
import com.esl.internship.staffsync.authentication.model.AuthEmployeeSlice;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.commons.exceptions.LoginAttemptExceededException;
import com.esl.internship.staffsync.commons.exceptions.InvalidCredentialsException;
import com.esl.internship.staffsync.commons.model.Employee;

import java.util.List;
import java.util.Optional;

public interface IAuthentication {

    Optional<String> signInEmployee(AuthEmployeeSlice authEmployeeSlice);

    boolean verifyEmployeeLogin(AuthEmployeeSlice employeeSlice, LoginDTO loginDTO) throws InvalidCredentialsException, LoginAttemptExceededException;

    Optional<AuthEmployeeSlice> getEmployeeSliceByEmail(String email);

    Optional<Employee> getContextCurrentEmployee();

    List<RoutePermissions> getCurrentEmployeePermissions(String employeeId);

    boolean checkIfUserIsCurrentEmployeeOrAdmin(String employeeId);

    boolean checkIfUserIsCurrentEmployee(String employeeId);

    boolean resetLogInAttempts();
}
