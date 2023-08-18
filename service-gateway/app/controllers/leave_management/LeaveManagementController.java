package controllers.leave_management;

import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.authentication.model.RouteRole;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.leave.management.api.ILeaveRequest;
import com.esl.internship.staffsync.leave.management.dto.CreateLeaveRequestDTO;
import com.esl.internship.staffsync.leave.management.dto.EditLeaveRequestDTO;
import com.esl.internship.staffsync.leave.management.model.LeaveRequest;
import com.esl.internship.staffsync.leave.management.model.LeaveRequestEmployee;
import com.esl.internship.staffsync.system.configuration.api.INotification;
import com.esl.internship.staffsync.system.configuration.api.IOption;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import security.WebAuth;

import javax.inject.Inject;
import java.util.Optional;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;

@Api("Leave Management")
@Transactional
public class LeaveManagementController extends Controller {

    @Inject
    ILeaveRequest iLeaveRequest;

    @Inject
    IAuthentication iAuthentication;

    @Inject
    INotification iNotification;

    @Inject
    IOption iOption;

    @Inject
    MyObjectMapper myObjectMapper;

    @ApiOperation("Get single leave request by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = LeaveRequest.class, message = "Leave request retrieved"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid leave request id"),
                    @ApiResponse(code = 404, response = String.class, message = "leave request not found")
            }
    )
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
    @WebAuth(permissions = {RoutePermissions.read_leave_request}, roles = {RouteRole.user, RouteRole.admin})
    public Result getLeaveRequest(@ApiParam(value = "Leave request Id", required = true) String leaveRequestId) {
        if (leaveRequestId.length() == 0) {
            return Results.badRequest("Invalid leave request id");
        }
        return iLeaveRequest.getLeaveRequest(leaveRequestId)
                .map(e -> Results.ok(myObjectMapper.toJsonString(e)))
                .orElseGet(Results::notFound);
    }

    @ApiOperation("Get all leave requests in the system")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = LeaveRequest.class, responseContainer = "List", message = "Leave requests retrieved"),
            }
    )
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
    @WebAuth(permissions = {RoutePermissions.read_leave_request}, roles = {RouteRole.admin})
    public Result getAllLeaveRequests() {
        return Results.ok(myObjectMapper.toJsonString(
                iLeaveRequest.getAllLeaveRequests()
        ));
    }

    @ApiOperation("Create a leave request")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = LeaveRequest.class, message = "Leave request created"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid data"),
                    @ApiResponse(code = 401, response = String.class, message = "Employee is not authorized to access this resource"),
                    @ApiResponse(code = 409, response = String.class, message = "Employee already has a request pending")
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Leave request Creation DTO",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.leave.management.dto.CreateLeaveRequestDTO",
                    dataTypeClass = CreateLeaveRequestDTO.class
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
    @WebAuth(permissions = {RoutePermissions.create_leave_request}, roles = {RouteRole.admin, RouteRole.user})
    public Result createLeaveRequest() {
        final var leaveRequestCreationForm = validate(request().body().asJson(), CreateLeaveRequestDTO.class);
        if (leaveRequestCreationForm.hasError) {
            return Results.badRequest(leaveRequestCreationForm.error);
        }
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        CreateLeaveRequestDTO createLeaveRequestDTO = leaveRequestCreationForm.value;
        if (!iAuthentication.checkIfUserIsCurrentEmployeeOrAdmin(createLeaveRequestDTO.getEmployeeId())) {
            return Results.unauthorized("Employee is not authorized to access this resource");
        }

        String leaveType = createLeaveRequestDTO.getLeaveTypeId();
        if (iOption.optionBelongsToOptionType(leaveType, "leave_type")) {
            return Results.badRequest("Invalid leave type");
        }

        if (iLeaveRequest.checkIfEmployeeHasOpenLeaveRequest(createLeaveRequestDTO.getEmployeeId()))
            return Results.status(409, "Employee already has a request pending ");
        LeaveRequest request = iLeaveRequest.addLeaveRequest(createLeaveRequestDTO);
        if (request != null) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), request.getLeaveRequestId(), "leave_request_creation_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "leave_request_creation_failure");
        }
        return Results.ok(myObjectMapper.toJsonString(request));
    }

    @ApiOperation("Get all leave requests created by an employee")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = LeaveRequest.class, responseContainer = "List", message = "Leave requests retrieved"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid employee id"),
                    @ApiResponse(code = 401, response = String.class, message = "Employee is not authorized to access this resource")
            }
    )
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
    @WebAuth(permissions = {RoutePermissions.read_leave_request}, roles = {RouteRole.admin, RouteRole.user})
    public Result getEmployeeLeaveRequests(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        if (employeeId.length() == 0) {
            return Results.badRequest("Invalid employee id");
        }
        if (!iAuthentication.checkIfUserIsCurrentEmployeeOrAdmin(employeeId)) {
            return Results.unauthorized("Employee is not authorized to access this resource");
        }
        return Results.ok(myObjectMapper.toJsonString(
                iLeaveRequest.getEmployeeLeaveRequests(employeeId)
        ));
    }

    @ApiOperation("Get all leave requests approved by an employee")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = LeaveRequest.class, responseContainer = "List", message = "Leave requests retrieved"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid employee id"),
            }
    )
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
    @WebAuth(permissions = {RoutePermissions.read_leave_request}, roles = {RouteRole.admin})
    public Result getEmployeeApprovedLeaveRequests(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        if (employeeId.length() == 0) {
            return Results.badRequest("Invalid employee id");
        }
        return Results.ok(myObjectMapper.toJsonString(
                iLeaveRequest.getEmployeeApprovedLeaveRequests(employeeId)
        ));
    }


    @ApiOperation("Get all pending leave requests in the system")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = LeaveRequest.class, responseContainer = "List", message = "Leave requests retrieved"),
            }
    )
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
    @WebAuth(permissions = {RoutePermissions.read_leave_request}, roles = {RouteRole.admin})
    public Result getAllPendingRequests() {
        return Results.ok(myObjectMapper.toJsonString(
                iLeaveRequest.getAllPendingRequests()
        ));
    }

    @ApiOperation("Get employee leave history")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = LeaveRequest.class, responseContainer = "List", message = "Leave requests retrieved"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid employee id"),
                    @ApiResponse(code = 401, response = String.class, message = "Employee is not authorized to access this resource"),

            }
    )
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
    @WebAuth(permissions = {RoutePermissions.read_leave_request}, roles = {RouteRole.admin, RouteRole.user})
    public Result getEmployeeLeaveHistory(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        if (employeeId.length() == 0) {
            return Results.badRequest("Invalid employee id");
        }
        if (!iAuthentication.checkIfUserIsCurrentEmployeeOrAdmin(employeeId)) {
            return Results.unauthorized("Employee is not authorized to access this resource");
        }
        return Results.ok(myObjectMapper.toJsonString(
                iLeaveRequest.getEmployeeLeaveHistory(employeeId)
        ));
    }

    @ApiOperation("Get all completed leaves in the system")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = LeaveRequest.class, responseContainer = "List", message = "Leave requests retrieved"),
                    @ApiResponse(code = 401, response = String.class, message = "Employee is not authorized to access this resource"),
            }
    )
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
    @WebAuth(permissions = {RoutePermissions.read_leave_request}, roles = {RouteRole.admin})
    public Result getAllCompletedLeave() {
        return Results.ok(myObjectMapper.toJsonString(
                iLeaveRequest.getAllCompletedLeave()
        ));
    }

    @ApiOperation("Get all ongoing leave in the system")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = LeaveRequest.class, responseContainer = "List", message = "Leave requests retrieved"),
            }
    )
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
    @WebAuth(permissions = {RoutePermissions.read_leave_request}, roles = {RouteRole.admin, RouteRole.user})
    public Result getAllOngoingLeave() {
        return Results.ok(myObjectMapper.toJsonString(
                iLeaveRequest.getAllOngoingLeave()
        ));
    }

    @ApiOperation("Approve a pending leave request ")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Boolean.class, message = "Leave requests retrieved"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid details"),
                    @ApiResponse(code = 404, response = String.class, message = "Leave request not found")
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Notification Creation DTO",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.leave.management.dto.EditLeaveRequestDTO",
                    dataTypeClass = EditLeaveRequestDTO.class
            )
            ,
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Authorization",
                    paramType = "header",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            )
    })
    @WebAuth(permissions = {RoutePermissions.update_leave_request}, roles = {RouteRole.admin})
    public Result approveLeaveRequest() {
        final var leaveRequestEditForm = validate(request().body().asJson(), EditLeaveRequestDTO.class);
        if (leaveRequestEditForm.hasError) {
            return Results.badRequest(leaveRequestEditForm.error);
        }
        EditLeaveRequestDTO editLeaveRequestDTO = leaveRequestEditForm.value;
        Optional<LeaveRequest> lr = iLeaveRequest.getLeaveRequest(editLeaveRequestDTO.getLeaveRequestId());
        if (lr.isPresent()) {
            Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
            boolean resp = iLeaveRequest.approveLeaveRequest(editLeaveRequestDTO, employee);
            if (resp) {
                iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), lr.get().getEmployee().getName(), "leave_request_approved");
                iNotification.sendNotification(lr.get().getEmployee().getEmployeeId(), "system_employee", employee.getFullName(), lr.get().getEmployee().getName(), "leave_request_approved");
            } else {
                iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "leave_request_update_failure");
            }
            return Results.ok(myObjectMapper.toJsonString(resp));
        }
        return Results.notFound("Leave request not found");
    }

    @ApiOperation("Cancel a pending or approved leave request")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = String.class, message = "Leave request successfully cancelled"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid employee id"),
                    @ApiResponse(code = 404, response = String.class, message = "No open leave request found for user"),
                    @ApiResponse(code = 401, response = String.class, message = "Employee is not authorized to access this resource")
            }
    )
    @WebAuth(permissions = {RoutePermissions.update_leave_request}, roles = {RouteRole.admin, RouteRole.user})
    public Result cancelLeaveRequest(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        if (employeeId.length() == 0) {
            return Results.badRequest("Invalid employee id");
        }
        if (!iAuthentication.checkIfUserIsCurrentEmployeeOrAdmin(employeeId)) {
            return Results.unauthorized("Employee is not authorized to access this resource");
        }
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean resp = iLeaveRequest.cancelLeaveRequest(employeeId);
        if (resp) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "leave_request_cancelled");
            return Results.ok("Leave request successfully cancelled");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "leave_request_update_failure");
        }
        return Results.notFound("No open leave request found for user");
    }

    @ApiOperation("Reject an employee's pending leave request")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = String.class, message = "Leave request successfully rejected"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid data"),
                    @ApiResponse(code = 4004, response = String.class, message = "No open leave request with that id")

            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Notification Creation DTO",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.leave.management.dto.EditLeaveRequestDTO",
                    dataTypeClass = EditLeaveRequestDTO.class
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
    @WebAuth(permissions = {RoutePermissions.update_leave_request}, roles = {RouteRole.admin})
    public Result rejectLeaveRequest() {
        final var leaveRequestEditForm = validate(request().body().asJson(), EditLeaveRequestDTO.class);
        if (leaveRequestEditForm.hasError) {
            return Results.badRequest(leaveRequestEditForm.error);
        }
        EditLeaveRequestDTO editLeaveRequestDTO = leaveRequestEditForm.value;

        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        LeaveRequestEmployee leaveOwner = iLeaveRequest.getLeaveRequest(editLeaveRequestDTO.getLeaveRequestId()).orElseThrow().getEmployee();
        boolean resp = iLeaveRequest.rejectLeaveRequest(editLeaveRequestDTO, iAuthentication.getContextCurrentEmployee().orElseThrow());

        if (resp) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), leaveOwner.getName(), "leave_request_rejected");
            iNotification.sendNotification(leaveOwner.getEmployeeId(), "system_employee", employee.getFullName(), leaveOwner.getName(), "leave_request_rejected");
            return Results.ok("Leave request successfully rejected");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "leave_request_update_failure");
        }
        return Results.notFound("No open leave request with that id");
    }

    @ApiOperation("Accept an approved leave request")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = String.class, message = "Leave request accepted"),
                    @ApiResponse(code = 404, response = String.class, message = "No approved leave request found for user")
            }
    )
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
    @WebAuth(permissions = {RoutePermissions.update_leave_request}, roles = {RouteRole.admin, RouteRole.user})
    public Result acceptLeaveRequest() {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean resp = iLeaveRequest.acceptLeaveRequest(iAuthentication.getContextCurrentEmployee().orElseThrow());
        if (resp) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "leave_request_accepted");
            return Results.ok("Leave request accepted");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "leave_request_update_failure");
        }

        if (resp) {
            return Results.ok("Leave request accepted");
        }
        return Results.notFound("No approved leave request found for user");
    }

    @ApiOperation("Mark a leave request as completed")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Boolean.class, message = "Leave request marked as completed"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid employee id"),
                    @ApiResponse(code = 401, response = String.class, message = "User is not authorized to access this resource"),
                    @ApiResponse(code = 404, response = String.class, message = "No on-going leave request found"),
            }
    )
    @WebAuth(permissions = {RoutePermissions.update_leave_request}, roles = {RouteRole.admin, RouteRole.user})
    public Result markLeaveRequestAsComplete(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        if (employeeId.length() == 0) {
            return Results.badRequest("Invalid employee id");
        }
        if (!iAuthentication.checkIfUserIsCurrentEmployeeOrAdmin(employeeId)) {
            return Results.unauthorized("Employee is not authorized to access this resource");
        }
        Optional<LeaveRequest> lr = iLeaveRequest.getOngoingLeaveRequestByEmployeeId(employeeId);

        if (lr.isPresent()) {
            int leaveDuration = (int) iLeaveRequest.getActualLeaveDuration(lr.get().getStartDate());
            Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
            LeaveRequestEmployee leaveRequestEmployee = lr.get().getEmployee();
            boolean result = iLeaveRequest.markLeaveRequestAsComplete(leaveDuration, employeeId, employee);
            if (result) {
                iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), leaveRequestEmployee.getName(), "leave_request_completed");
                if (!employee.getEmployeeId().equals(employeeId)) {
                    iNotification.sendNotification(leaveRequestEmployee.getEmployeeId(), "system_employee", "You", "your", "leave_request_completed");
                }
                return Results.ok(myObjectMapper.toJsonString(result));
            } else {
                iNotification.sendNotification(leaveRequestEmployee.getEmployeeId(), "system_employee", employee.getFullName(), "", "leave_request_completed");
            }
        }
        return Results.notFound("No on-going leave request found");
    }

    @ApiOperation("Delete a Leave request by  id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Boolean.class, message = "Leave request deleted"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid leave request id"),
            }
    )
    @WebAuth(permissions = {RoutePermissions.delete_leave_request}, roles = {RouteRole.admin})
    public Result deleteLeaveRequest(@ApiParam(value = "Leave request Id", required = true) String leaveRequestId) {
        if (leaveRequestId.length() == 0) {
            return Results.badRequest("Invalid leave request id");
        }
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean result = iLeaveRequest.deleteLeaveRequest(leaveRequestId);
        LeaveRequestEmployee leaveRequestEmployee = iLeaveRequest.getLeaveRequest(leaveRequestId).orElseThrow().getEmployee();
        if (result) {
            iNotification.sendNotification(leaveRequestEmployee.getEmployeeId(), "system_employee", employee.getFullName(), leaveRequestId, "leave_request_deletion_successful");
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), leaveRequestId, "leave_request_deletion_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "leave_request_deletion_failure");
        }

        return ok(myObjectMapper.toJsonString(
                result
        ));
    }

}