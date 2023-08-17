package controllers.authentication;


import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.dto.LoginDTO;
import com.esl.internship.staffsync.authentication.model.AuthEmployeeSlice;
import com.esl.internship.staffsync.authentication.model.AuthToken;
import com.esl.internship.staffsync.commons.exceptions.InvalidCredentialsException;
import com.esl.internship.staffsync.commons.exceptions.LoginAttemptExceededException;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.Optional;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;

@Api("Authentication")
@Transactional
public class AuthenticationController extends Controller {

    @Inject
    IAuthentication iAuthentication;

    @Inject
    MyObjectMapper myObjectMapper;


    @ApiOperation("Authenticate user and provide token")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = AuthToken.class, message = "Successfully signed in"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid employee details"),
                    @ApiResponse(code = 401, response = String.class, message = "Invalid details. You have x amount of attempts left"),
                    @ApiResponse(code = 423, response = String.class, message = "Account has been restricted due to multiple failed log in attempts")
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Employee signin DTO",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.authentication.dto.LoginDTO",
                    dataTypeClass = LoginDTO.class
            )
    })
    public Result signEmployeeIn() {
        final var loginForm = validate(request().body().asJson(), LoginDTO.class);
        if (loginForm.hasError) {
            return Results.badRequest(loginForm.error);
        }

        LoginDTO loginDTO = loginForm.value;
        Optional<AuthEmployeeSlice> optionalAuthEmployeeSlice = iAuthentication.getEmployeeSliceByEmail(loginDTO.getEmployeeEmail());
        if (optionalAuthEmployeeSlice.isEmpty()) {
            return Results.unauthorized("Invalid log in details");
        }

        AuthEmployeeSlice employeeSlice = optionalAuthEmployeeSlice.get();
        if (!employeeSlice.getEmployeeActive()) {
            return Results.status(423, "Account has been restricted");
        }
        try {
            if (iAuthentication.verifyEmployeeLogin(employeeSlice, loginDTO)) {
                return iAuthentication.signInEmployee(employeeSlice)
                        .map(e -> ok(myObjectMapper.toJsonString(new AuthToken(e)))).orElseGet(Results::internalServerError);
            }
        } catch (LoginAttemptExceededException e) {
            Results.status(423, e.getMessage());
        } catch (InvalidCredentialsException e) {
            return Results.badRequest(e.getMessage());
        }
        return Results.status(423, "Account has been restricted");
    }
}
