package controllers.leave_management;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.leave.management.api.ILeaveRequest;
import com.esl.internship.staffsync.leave.management.dto.CreateLeaveRequestDTO;
import com.esl.internship.staffsync.leave.management.dto.EditLeaveRequestDTO;
import com.esl.internship.staffsync.leave.management.model.LeaveRequest;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.Optional;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;

@Api("Leave Management")
@Transactional
public class LeaveManagementController extends Controller {

    @Inject
    ILeaveRequest iLeaveRequest;

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
    public Result getLeaveRequest(@ApiParam(value = "Leave request Id", required = true) String leaveRequestId) {
        if (leaveRequestId.length() == 0) {
            return Results.badRequest("Invalid leave request id");
        }
        //TODO: Check if AppUser ID matches employee id or is Admin
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
    public Result getAllLeaveRequests() {
        //TODO: Check if user is admin
        return Results.ok(myObjectMapper.toJsonString(
                iLeaveRequest.getAllLeaveRequests()
        ));
    }

    @ApiOperation("Create a leave request")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = LeaveRequest.class, message = "Leave request created"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid data"),
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
            )
    })
    public Result createLeaveRequest() {
        final var leaveRequestCreationForm = validate(request().body().asJson(), CreateLeaveRequestDTO.class);
        if (leaveRequestCreationForm.hasError) {
            return Results.badRequest(leaveRequestCreationForm.error);
        }
        //TODO: Check if user is admin or active user matches employeeID
        CreateLeaveRequestDTO createLeaveRequestDTO = leaveRequestCreationForm.value;

        if (iLeaveRequest.checkIfEmployeeHasOpenLeaveRequest(createLeaveRequestDTO.getEmployeeId()))
            return Results.status(409, "Employee already has a request pending ");
        return Results.ok(myObjectMapper.toJsonString(iLeaveRequest.addLeaveRequest(
                createLeaveRequestDTO
        )));
    }

    @ApiOperation("Get all leave requests created by an employee")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = LeaveRequest.class, responseContainer = "List", message = "Leave requests retrieved"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid employee id"),
            }
    )
    public Result getEmployeeLeaveRequests(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        if (employeeId.length() == 0) {
            return Results.badRequest("Invalid employee id");
        }
        //TODO: Check if user is admin
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
    public Result getEmployeeApprovedLeaveRequests(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        if (employeeId.length() == 0) {
            return Results.badRequest("Invalid employee id");
        }
        //TODO: Check if user is admin
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
    public Result getAllPendingRequests() {
        //TODO: Check if user is admin
        return Results.ok(myObjectMapper.toJsonString(
                iLeaveRequest.getAllPendingRequests()
        ));
    }

    @ApiOperation("Get employee leave history")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = LeaveRequest.class, responseContainer = "List", message = "Leave requests retrieved"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid employee id"),
            }
    )
    public Result getEmployeeLeaveHistory(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        if (employeeId.length() == 0) {
            return Results.badRequest("Invalid employee id");
        }
        //TODO: Check if user is admin
        return Results.ok(myObjectMapper.toJsonString(
                iLeaveRequest.getEmployeeLeaveHistory(employeeId)
        ));
    }

    @ApiOperation("Get all completed leaves in the system")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = LeaveRequest.class, responseContainer = "List", message = "Leave requests retrieved"),
            }
    )
    public Result getAllCompletedLeave() {

        //TODO: Check if user is admin
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
    public Result getAllOngoingLeave() {

        //TODO: Check if user is admin
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
    })
    public Result approveLeaveRequest() {
        final var leaveRequestEditForm = validate(request().body().asJson(), EditLeaveRequestDTO.class);
        if (leaveRequestEditForm.hasError) {
            return Results.badRequest(leaveRequestEditForm.error);
        }
        //TODO: Check if user is admin
        EditLeaveRequestDTO editLeaveRequestDTO = leaveRequestEditForm.value;
        Optional<LeaveRequest> lr = iLeaveRequest.getLeaveRequest(editLeaveRequestDTO.getLeaveRequestId());
        if (lr.isPresent()) {
            return Results.ok(myObjectMapper.toJsonString(
                    iLeaveRequest.approveLeaveRequest(editLeaveRequestDTO, getTestEmployee())
            ));
        }
        return Results.notFound("Leave request not found");
    }

    @ApiOperation("Cancel a pending or approved leave request")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = String.class, message = "Leave request successfully cancelled"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid employee id"),
                    @ApiResponse(code = 404, response = String.class, message = "No open leave request found for user")

            }
    )
    public Result cancelLeaveRequest(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        if (employeeId.length() == 0) {
            return Results.badRequest("Invalid employee id");
        }
        //TODO: Check if user is the employee with id employeeid or admin
        boolean resp = iLeaveRequest.cancelLeaveRequest(employeeId);
        if (resp) {
            return Results.ok("Leave request successfully cancelled");
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
            )
    })
    public Result rejectLeaveRequest() {
        final var leaveRequestEditForm = validate(request().body().asJson(), EditLeaveRequestDTO.class);
        if (leaveRequestEditForm.hasError) {
            return Results.badRequest(leaveRequestEditForm.error);
        }
        //TODO: Check if user is admin
        EditLeaveRequestDTO editLeaveRequestDTO = leaveRequestEditForm.value;
        boolean resp = iLeaveRequest.rejectLeaveRequest(editLeaveRequestDTO, getDummyEmployee());
        if (resp) {
            return Results.ok("Leave request successfully rejected");
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
    public Result acceptLeaveRequest() {
        //TODO: Check if user is the employee with id employeeid
        boolean resp = iLeaveRequest.acceptLeaveRequest(getDummyEmployee());
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
                    @ApiResponse(code = 404, response = String.class, message = "No on-going leave request found"),
            }
    )
    public Result markLeaveRequestAsComplete(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        if (employeeId.length() == 0) {
            return Results.badRequest("Invalid employee id");
        }
        //TODO: Check if user is the employee with id employeeId or admin
        Optional<LeaveRequest> lr = iLeaveRequest.getOngoingLeaveRequestByEmployeeId(employeeId);

        if (lr.isPresent()) {
            int leaveDuration = (int) iLeaveRequest.getActualLeaveDuration(lr.get().getStartDate());
            boolean result = iLeaveRequest.markLeaveRequestAsComplete(leaveDuration, employeeId, getTestEmployee());
            return Results.ok(myObjectMapper.toJsonString(result));
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
    public Result deleteLeaveRequest(@ApiParam(value = "Leave request Id", required = true) String leaveRequestId) {
        if (leaveRequestId.length() == 0) {
            return Results.badRequest("Invalid leave request id");
        }
        return ok(myObjectMapper.toJsonString(
                iLeaveRequest.deleteLeaveRequest(leaveRequestId)
        ));
    }

    private Employee getDummyEmployee() {
        return new Employee("92f6fac6-f49b-448f-9c33-f0d50608bc83", "employee");
    }

    private Employee getTestEmployee() {
        return new Employee("f04b5314-9f26-43a0-b129-3e149165253e", "Name");
    }

}