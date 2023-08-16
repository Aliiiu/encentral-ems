package controllers.employee_management;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.employee.management.api.IEmployeeUpdateRequestApi;
import com.esl.internship.staffsync.employee.management.dto.EmployeeUpdateApprovalDTO;
import com.esl.internship.staffsync.employee.management.dto.EmployeeUpdateRequestDTO;
import com.esl.internship.staffsync.employee.management.model.EmployeeUpdateRequest;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

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
            )
    })
    public Result submitUpdateRequest(String employeeId) {
        final var updateRequestForm = validate(request().body().asJson(), EmployeeUpdateRequestDTO.class);
        if (updateRequestForm.hasError) {
            return badRequest(updateRequestForm.error);
        }

        Response<EmployeeUpdateRequest> response = iEmployeeUpdateRequestApi.createEmployeeUpdateRequest(employeeId, updateRequestForm.value, getEmployee());
        if (response.requestHasErrors())
            return badRequest(response.getErrorsAsJsonString());
        return ok(objectMapper.toJsonString(response.getValue()));
    }

    @ApiOperation(value = "Cancel an Update Request")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Request Received", response = boolean.class),
            @ApiResponse(code = 400, message = "Cannot cancel request not created by employee", response = String.class)
    })
    public Result cancelUpdateRequest(String employeeUpdateRequestId, String employeeId) {
        Response<Boolean> response = iEmployeeUpdateRequestApi.cancelEmployeeUpdateRequest(employeeUpdateRequestId, employeeId);
        if (response.requestHasErrors())
            return badRequest(response.getErrorsAsJsonString());
        else
            return ok(objectMapper.toJsonString(response.getValue()));
    }

    @ApiOperation(value = "Get an Update Request by Id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Update request", response = EmployeeUpdateRequest.class),
            @ApiResponse(code = 400, message = "Not Found", response = String.class)
    })
    public Result getUpdateRequest(String employeeUpdateRequestId) {
        Optional<EmployeeUpdateRequest> response = iEmployeeUpdateRequestApi.getEmployeeUpdateRequest(employeeUpdateRequestId);
        return response.map(employeeUpdateRequest -> ok(objectMapper.toJsonString(employeeUpdateRequest))).orElseGet(() -> notFound("Update Request not found"));
    }

    @ApiOperation(value = "Get all Update Requests of an Employee")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Update request", response = EmployeeUpdateRequest.class, responseContainer = "List")
    })
    public Result getUpdateRequestsOfEmployee(String employeeId) {
        return ok(objectMapper.toJsonString(iEmployeeUpdateRequestApi.getUpdateRequestsOfEmployee(employeeId)));
    }

    @ApiOperation(value = "Get all Pending Update Requests of an Employee")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Update request", response = EmployeeUpdateRequest.class, responseContainer = "List")
    })
    public Result getPendingUpdateRequestsOfEmployee(String employeeId) {
        return ok(objectMapper.toJsonString(iEmployeeUpdateRequestApi.getPendingUpdateRequestsOfEmployee(employeeId)));
    }

    @ApiOperation(value = "Get all Completed Update Requests of an Employee")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Update request", response = EmployeeUpdateRequest.class, responseContainer = "List")
    })
    public Result getCompletedUpdateRequestsOfEmployee(String employeeId) {
        return ok(objectMapper.toJsonString(iEmployeeUpdateRequestApi.getCompletedUpdateRequestsOfEmployee(employeeId)));
    }

    @ApiOperation(value = "Get all Update Requests")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Update request", response = EmployeeUpdateRequest.class, responseContainer = "List")
    })
    public Result getAllEmployeeUpdateRequest() {
        return ok(objectMapper.toJsonString(iEmployeeUpdateRequestApi.getAllEmployeeUpdateRequests()));
    }

    @ApiOperation(value = "Get all Pending Update Requests")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Update request", response = EmployeeUpdateRequest.class, responseContainer = "List")
    })
    public Result getAllPendingUpdateRequest() {
        return ok(objectMapper.toJsonString(iEmployeeUpdateRequestApi.getAllPendingUpdateRequests()));
    }

    @ApiOperation(value = "Get all Approved Update Requests")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Update request", response = EmployeeUpdateRequest.class, responseContainer = "List")
    })
    public Result getAllApprovedUpdateRequest() {
        return ok(objectMapper.toJsonString(iEmployeeUpdateRequestApi.getAllApprovedUpdateRequests()));
    }

    @ApiOperation(value = "Get all Completed Update Requests")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Update request", response = EmployeeUpdateRequest.class, responseContainer = "List")
    })
    public Result getAllCompletedUpdateRequest() {
        return ok(objectMapper.toJsonString(iEmployeeUpdateRequestApi.getAllCompletedUpdateRequests()));
    }

    @ApiOperation(value = "Get all Update Requests by an Employee")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Update request", response = EmployeeUpdateRequest.class, responseContainer = "List")
    })
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
            )
    })
    public Result reviewUpdateRequest(String employeeUpdateRequestId, String approverEmployeeId) {
        final var reviewUpdateForm = validate(request().body().asJson(), EmployeeUpdateApprovalDTO.class);
        if (reviewUpdateForm.hasError)
            return badRequest(reviewUpdateForm.error);
        Response<Boolean> response = iEmployeeUpdateRequestApi.reviewEmployeeUpdateRequest(employeeUpdateRequestId, approverEmployeeId, reviewUpdateForm.value);
        if (response.requestHasErrors())
            return badRequest(response.getErrorsAsJsonString());
        return ok(objectMapper.toJsonString(response.getValue()));
    }

    @ApiOperation(value = "Delete an Update Request by ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Update request Deleted", response = Boolean.class)
    })
    public Result deleteUpdateRequest(String employeeUpdateRequestId) {
        return ok(objectMapper.toJsonString(iEmployeeUpdateRequestApi.deleteEmployeeUpdateRequest(employeeUpdateRequestId)));
    }

    /**
     * @author WARITH
     * @dateCreated 14/08/23
     * @description To return the authenticated and authorized Employee
     *
     * @return Employee
     */
    Employee getEmployee() {
        return new Employee();
    }
}
