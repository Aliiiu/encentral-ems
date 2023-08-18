package controllers.employee_management;

import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.authentication.model.RouteRole;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.employee.management.api.IEmployeeUpdateRequestApi;
import com.esl.internship.staffsync.employee.management.dto.EmployeeUpdateApprovalDTO;
import com.esl.internship.staffsync.employee.management.dto.EmployeeUpdateRequestDTO;
import com.esl.internship.staffsync.employee.management.model.EmployeeUpdateRequest;
import com.esl.internship.staffsync.system.configuration.api.INotification;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import security.WebAuth;

import javax.inject.Inject;
import java.util.Optional;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;

@Api("Employee Management - Employee Update Request")
@Transactional
public class EmployeeUpdateRequestController extends Controller {

    @Inject
    IEmployeeUpdateRequestApi iEmployeeUpdateRequestApi;

    @Inject
    MyObjectMapper objectMapper;

    @Inject
    INotification iNotification;

    @Inject
    IAuthentication iAuthentication;

    @ApiOperation(value = "Submit an Update Request")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Request Received", response = EmployeeUpdateRequest.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Employee data to update",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.employee.management.dto.EmployeeUpdateRequestDTO"
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
    @WebAuth(permissions = {RoutePermissions.create_employee_update}, roles = {RouteRole.user, RouteRole.admin})
    public Result submitUpdateRequest(String employeeId) {
        final var updateRequestForm = validate(request().body().asJson(), EmployeeUpdateRequestDTO.class);
        if (updateRequestForm.hasError) {
            return badRequest(updateRequestForm.error);
        }
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        Response<EmployeeUpdateRequest> response = iEmployeeUpdateRequestApi.createEmployeeUpdateRequest(employeeId, updateRequestForm.value, employee);

        boolean result = response.getValue() != null;

        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), response.getValue().getEmployeeUpdateRequestId(), "update_request_creation_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "update_request_creation_failure");
        }

        if (response.requestHasErrors())
            return badRequest(response.getErrorsAsJsonString());
        return ok(objectMapper.toJsonString(response.getValue()));
    }

    @ApiOperation(value = "Cancel an Update Request")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Request Received", response = boolean.class),
            @ApiResponse(code = 400, message = "Cannot cancel request not created by employee", response = String.class)
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
    @WebAuth(permissions = {RoutePermissions.delete_employee_update}, roles = {RouteRole.admin, RouteRole.user})
    public Result cancelUpdateRequest(String employeeUpdateRequestId, String employeeId) {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        Response<Boolean> response = iEmployeeUpdateRequestApi.cancelEmployeeUpdateRequest(employeeUpdateRequestId, employeeId);

        if (response.requestHasErrors()) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "update_request_deletion_failure");
            return badRequest(response.getErrorsAsJsonString());
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "update_request_deletion_successful");
            return ok(objectMapper.toJsonString(response.getValue()));
        }
    }

    @ApiOperation(value = "Get an Update Request by Id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Update request", response = EmployeeUpdateRequest.class),
            @ApiResponse(code = 400, message = "Not Found", response = String.class)
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
    @WebAuth(permissions = {RoutePermissions.read_employee_update}, roles = {RouteRole.admin, RouteRole.user})
    public Result getUpdateRequest(String employeeUpdateRequestId) {
        Optional<EmployeeUpdateRequest> response = iEmployeeUpdateRequestApi.getEmployeeUpdateRequest(employeeUpdateRequestId);
        return response.map(employeeUpdateRequest -> ok(objectMapper.toJsonString(employeeUpdateRequest))).orElseGet(() -> notFound("Update Request not found"));
    }

    @ApiOperation(value = "Get all Update Requests of an Employee")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Update request", response = EmployeeUpdateRequest.class, responseContainer = "List")
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
    @WebAuth(permissions = {RoutePermissions.read_employee_update}, roles = {RouteRole.admin, RouteRole.user})
    public Result getUpdateRequestsOfEmployee(String employeeId) {
        return ok(objectMapper.toJsonString(iEmployeeUpdateRequestApi.getUpdateRequestsOfEmployee(employeeId)));
    }

    @ApiOperation(value = "Get all Pending Update Requests of an Employee")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Update request", response = EmployeeUpdateRequest.class, responseContainer = "List")
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
    @WebAuth(permissions = {RoutePermissions.read_employee_update}, roles = {RouteRole.admin, RouteRole.user})
    public Result getPendingUpdateRequestsOfEmployee(String employeeId) {
        return ok(objectMapper.toJsonString(iEmployeeUpdateRequestApi.getPendingUpdateRequestsOfEmployee(employeeId)));
    }

    @ApiOperation(value = "Get all Completed Update Requests of an Employee")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Update request", response = EmployeeUpdateRequest.class, responseContainer = "List")
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
    @WebAuth(permissions = {RoutePermissions.read_employee_update}, roles = {RouteRole.admin, RouteRole.user})
    public Result getCompletedUpdateRequestsOfEmployee(String employeeId) {
        return ok(objectMapper.toJsonString(iEmployeeUpdateRequestApi.getCompletedUpdateRequestsOfEmployee(employeeId)));
    }

    @ApiOperation(value = "Get all Update Requests")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Update request", response = EmployeeUpdateRequest.class, responseContainer = "List")
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
    @WebAuth(permissions = {RoutePermissions.read_employee_update}, roles = {RouteRole.admin})
    public Result getAllEmployeeUpdateRequest() {
        return ok(objectMapper.toJsonString(iEmployeeUpdateRequestApi.getAllEmployeeUpdateRequests()));
    }

    @ApiOperation(value = "Get all Pending Update Requests")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Update request", response = EmployeeUpdateRequest.class, responseContainer = "List")
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
    @WebAuth(permissions = {RoutePermissions.read_employee_update}, roles = {RouteRole.admin})
    public Result getAllPendingUpdateRequest() {
        return ok(objectMapper.toJsonString(iEmployeeUpdateRequestApi.getAllPendingUpdateRequests()));
    }

    @ApiOperation(value = "Get all Approved Update Requests")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Update request", response = EmployeeUpdateRequest.class, responseContainer = "List")
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
    @WebAuth(permissions = {RoutePermissions.read_employee_update}, roles = {RouteRole.admin, RouteRole.user})
    public Result getAllApprovedUpdateRequest() {
        return ok(objectMapper.toJsonString(iEmployeeUpdateRequestApi.getAllApprovedUpdateRequests()));
    }

    @ApiOperation(value = "Get all Completed Update Requests")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Update request", response = EmployeeUpdateRequest.class, responseContainer = "List")
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
    @WebAuth(permissions = {RoutePermissions.read_employee_update}, roles = {RouteRole.admin, RouteRole.user})
    public Result getAllCompletedUpdateRequest() {
        return ok(objectMapper.toJsonString(iEmployeeUpdateRequestApi.getAllCompletedUpdateRequests()));
    }

    @ApiOperation(value = "Get all Update Requests by an Employee")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Update request", response = EmployeeUpdateRequest.class, responseContainer = "List")
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
    @WebAuth(permissions = {RoutePermissions.read_employee_update}, roles = {RouteRole.admin, RouteRole.user})
    public Result getAllApprovedUpdateRequestByEmployee(@ApiParam(value = "Employee ID of the approver") String approverEmployeeId) {
        return ok(objectMapper.toJsonString(iEmployeeUpdateRequestApi.getAllApprovedUpdateRequestsByEmployee(approverEmployeeId)));
    }

    @ApiOperation(value = "Review an Update Request")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Request Reviewed", response = boolean.class),
            @ApiResponse(code = 200, message = "Request cannot be reviewed: Cancelled Update Request", response = String.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Review Information",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.employee.management.dto.EmployeeUpdateApprovalDTO"
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
    @WebAuth(permissions = {RoutePermissions.update_employee_update}, roles = {RouteRole.admin})
    public Result reviewUpdateRequest(String employeeUpdateRequestId, String approverEmployeeId) {
        final var reviewUpdateForm = validate(request().body().asJson(), EmployeeUpdateApprovalDTO.class);

        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        Response<Boolean> response = iEmployeeUpdateRequestApi.reviewEmployeeUpdateRequest(employeeUpdateRequestId, approverEmployeeId, reviewUpdateForm.value);

        if (reviewUpdateForm.hasError)
            return badRequest(reviewUpdateForm.error);
        if (response.requestHasErrors()) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "update_request_update_failure");
            return badRequest(response.getErrorsAsJsonString());
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "update_request_update_successful");
            return ok(objectMapper.toJsonString(response.getValue()));
        }
    }

    @ApiOperation(value = "Delete an Update Request by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Update request Deleted", response = Boolean.class)
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
    @WebAuth(permissions = {RoutePermissions.delete_employee_update}, roles = {RouteRole.admin, RouteRole.user})
    public Result deleteUpdateRequest(String employeeUpdateRequestId) {

        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean result = iEmployeeUpdateRequestApi.deleteEmployeeUpdateRequest(employeeUpdateRequestId);

        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "update_request_deletion_success");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "update_request_deletion_failure");
        }
        return ok(objectMapper.toJsonString(result));
    }


}
