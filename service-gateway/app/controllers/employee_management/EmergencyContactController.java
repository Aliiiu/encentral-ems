package controllers.employee_management;


import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.authentication.model.RouteRole;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.employee.management.api.IEmployeeEmergencyContactApi;
import com.esl.internship.staffsync.employee.management.dto.EmergencyContactDTO;
import com.esl.internship.staffsync.employee.management.model.EmergencyContact;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.system.configuration.api.INotification;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import security.WebAuth;

import javax.inject.Inject;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;


@Transactional
@Api("Employee Management - Emergency Contact")
public class EmergencyContactController extends Controller {

    @Inject
    IEmployeeEmergencyContactApi iEmployeeEmergencyContactApi;

    @Inject
    MyObjectMapper objectMapper;

    @Inject
    IAuthentication iAuthentication;

    @Inject
    INotification iNotification;

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
            ),
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Authorization",
                    paramType = "header",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            )
    })
    @WebAuth(permissions= {RoutePermissions.create_emergency_contact }, roles = {RouteRole.admin})
    public Result addEmergencyContact(String employeeId) {
        final var emergencyContactForm = validate(request().body().asJson(), EmergencyContactDTO.class);
        if (emergencyContactForm.hasError) {
            return badRequest(emergencyContactForm.error);
        }

        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        Response<EmergencyContact> serviceResponse = iEmployeeEmergencyContactApi
                .createEmergencyContact(employeeId, emergencyContactForm.value, employee);
        boolean result = serviceResponse.getValue() != null;
        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee",employee.getFullName(), serviceResponse.getValue().getFullName(), "emergency_contact_creation_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "emergency_contact_creation_failure");
        }
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

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Authorization",
                    paramType = "header",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            )
    })
    @WebAuth(permissions= {RoutePermissions.read_emergency_contact }, roles = {RouteRole.admin, RouteRole.user})
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
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Authorization",
                    paramType = "header",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            )
    })
    @WebAuth(permissions= {RoutePermissions.read_emergency_contact }, roles = {RouteRole.admin, RouteRole.user})
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
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Authorization",
                    paramType = "header",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            )
    })
    @WebAuth(permissions= {RoutePermissions.read_emergency_contact }, roles = {RouteRole.admin, RouteRole.user})
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
            ),
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Authorization",
                    paramType = "header",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            )
    })
    @WebAuth(permissions= {RoutePermissions.update_emergency_contact }, roles = {RouteRole.admin})
    public Result updateEmergencyContact(String emergencyContactId) {
        final var emergencyContactForm = validate(request().body().asJson(), EmergencyContactDTO.class);
        if (emergencyContactForm.hasError) {
            return badRequest(emergencyContactForm.error);
        }

        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean result = iEmployeeEmergencyContactApi.updateEmergencyContact(
                emergencyContactId,
                emergencyContactForm.value,
                employee
        );

        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee",employee.getFullName(), "", "emergency_contact_update_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "emergency_contact_update_failure");
        }

        return ok(objectMapper.toJsonString(
                result
        ));
    }

    @ApiOperation(value = "Delete Emergency Contact")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Deleted", response = boolean.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Authorization",
                    paramType = "header",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            )
    })
    @WebAuth(permissions= {RoutePermissions.delete_emergency_contact }, roles = {RouteRole.admin})
    public Result deleteEmergencyContact(String emergencyContactId) {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean result = iEmployeeEmergencyContactApi.deleteEmergencyContact(emergencyContactId);

        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee",employee.getFullName(), "", "emergency_contact_deletion_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "emergency_contact_deletion_failure");
        }
        return ok(objectMapper.toJsonString(
                result
        ));
    }
}
