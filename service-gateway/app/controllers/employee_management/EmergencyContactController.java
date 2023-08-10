package controllers.employee_management;


import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.employee.management.api.IEmployeeEmergencyContactApi;
import com.esl.internship.staffsync.employee.management.dto.EmergencyContactDTO;
import com.esl.internship.staffsync.employee.management.model.EmergencyContact;
import com.esl.internship.staffsync.employee.management.service.response.Response;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;


@Transactional
@Api("Employee Management - Emergency Contact")
public class EmergencyContactController extends Controller {

    @Inject
    IEmployeeEmergencyContactApi iEmployeeEmergencyContactApi;

    @Inject
    MyObjectMapper objectMapper;

    @ApiOperation(value = "Create Emergency Contact")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Created",
                    response = com.esl.internship.staffsync.employee.management.model.EmergencyContact.class
            )
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Department data to create",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.employee.management.dto.EmergencyContactDTO"
            )
    })
    public Result addEmergencyContact(String employeeId) {
        final var emergencyContactForm = validate(request().body().asJson(), EmergencyContactDTO.class);
        if (emergencyContactForm.hasError) {
            return badRequest(emergencyContactForm.error);
        }

        Response<EmergencyContact> serviceResponse = iEmployeeEmergencyContactApi
                .createEmergencyContact(employeeId, emergencyContactForm.value, getEmployee());

        if (serviceResponse.requestHasErrors())
            return badRequest(serviceResponse.getErrorsAsJsonString());
        return ok(objectMapper.toJsonString(serviceResponse.getValue()));
    }

    @ApiOperation(value = "Get Emergency Contact by id")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "EmergencyContact",
                    response = com.esl.internship.staffsync.employee.management.model.EmergencyContact.class
            )
    })
    public Result getEmergencyContact(String emergencyContactId) {
        return ok(objectMapper.toJsonString(
                iEmployeeEmergencyContactApi.getEmergencyContact(emergencyContactId)
        ));
    }

    @ApiOperation(value = "Get Emergency Contacts of an Employee")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "EmergencyContact",
                    response = com.esl.internship.staffsync.employee.management.model.EmergencyContact.class,
                    responseContainer = "List"
            )
    })
    public Result getEmergencyContactsOfEmployee(String employeeId) {
        return ok(objectMapper.toJsonString(
                iEmployeeEmergencyContactApi.getEmergencyContactsOfEmployee(employeeId)
        ));
    }

    @ApiOperation(value = "Get all Emergency Contacts")
    @ApiResponses({
            @ApiResponse(
                    code = 200, message = "EmergencyContacts",
                    response = com.esl.internship.staffsync.employee.management.model.EmergencyContact.class,
                    responseContainer = "List"
            )
    })
    public Result getAllEmergencyContacts() {
        return ok(objectMapper.toJsonString(
                iEmployeeEmergencyContactApi.getAllEmergencyContacts()
        ));
    }

    @ApiOperation(value = "Update Emergency Contact")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Updated", response = com.esl.internship.staffsync.employee.management.model.EmergencyContact.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Department data to update",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.employee.management.dto.EmergencyContactDTO"
            )
    })
    public Result updateEmergencyContact(String emergencyContactId) {
        final var emergencyContactForm = validate(request().body().asJson(), EmergencyContactDTO.class);
        if (emergencyContactForm.hasError) {
            return badRequest(emergencyContactForm.error);
        }
        return ok(objectMapper.toJsonString(
                iEmployeeEmergencyContactApi.updateEmergencyContact(
                        emergencyContactId,
                        emergencyContactForm.value,
                        getEmployee()
                )
        ));
    }

    @ApiOperation(value = "Delete Emergency Contact")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Deleted", response = boolean.class)
    })
    public Result deleteEmergencyContact(String emergencyContactId) {
        return ok(objectMapper.toJsonString(iEmployeeEmergencyContactApi.deleteEmergencyContact(emergencyContactId)));
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/23
     * @description To return the authenticated and authorized Employee
     *
     * @return Employee
     */
    Employee getEmployee() {
        return new Employee("Test-001-EMP", "Test Employee");
    }
}
