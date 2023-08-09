package controllers.authentication;


import com.encentral.scaffold.commons.util.MyObjectMapper;
import com.encentral.staffsync.entity.JpaEmployee;
import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.dto.LoginDTO;
import com.esl.internship.staffsync.authentication.model.LoginResult;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.Optional;

import static com.encentral.scaffold.commons.util.ImmutableValidator.validate;

@Api("Authentication")
@Transactional
class AuthenticationController extends Controller {

    @Inject
    IAuthentication iAuthentication;

    @Inject
    MyObjectMapper myObjectMapper;


    @ApiOperation("Employee sign in")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = String.class, message = "Successfully signed in"),
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
    Result signEmployeeIn() {
        final var loginForm = validate(request().body().asJson(), LoginDTO.class);
        if (loginForm.hasError) {
            return Results.badRequest(loginForm.error);
        }

        LoginDTO loginDTO = loginForm.value;

        Optional<JpaEmployee> jpaEmployeeOptional = iAuthentication.getJpaEmployeeByEmail(loginDTO.getEmployeeEmail());
        if (jpaEmployeeOptional.isEmpty()) {
            return Results.unauthorized("Invalid log in details");
        }

        JpaEmployee jpaEmployee = jpaEmployeeOptional.get();

        if (!jpaEmployee.getEmployeeActive()) {
            return Results.status(423, "Account has been restricted due to multiple failed log in attempts");
        }

        //TODO: UNSALT PASSWORD
        if (!jpaEmployee.getPassword().equals(loginDTO.getPassword())) {
            int loginAttempts = jpaEmployee.getLoginAttempts();
            if (loginAttempts >= 5) {
                iAuthentication.restrictAccount(loginDTO.getEmployeeEmail());
                return Results.status(423, "Account has been restricted due to multiple failed log in attempts");
            }
            iAuthentication.updateEmployeeLoginAttempts(loginDTO.getEmployeeEmail());
            String response = iAuthentication.generateInvalidPasswordMessage(loginAttempts);
            return Results.badRequest(response);
        }
        return iAuthentication.signInEmployee(jpaEmployee)
                .map(e -> ok(myObjectMapper.toJsonString(e))).orElseGet(Results::internalServerError);
    }
}
