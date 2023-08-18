package controllers.attendance_management;

import com.esl.internship.staffsync.attendance.tracking.api.IAttendanceTracking;
import com.esl.internship.staffsync.attendance.tracking.dto.AttendanceFilterDTO;
import com.esl.internship.staffsync.attendance.tracking.dto.SystemFilterDTO;
import com.esl.internship.staffsync.attendance.tracking.model.Attendance;
import com.esl.internship.staffsync.attendance.tracking.model.AttendanceReport;
import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.authentication.model.RouteRole;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.employee.management.api.IEmployeeApi;
import com.esl.internship.staffsync.system.configuration.api.INotification;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import security.WebAuth;

import javax.inject.Inject;
import java.util.Optional;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;

@Api("Attendance Tracking")
@Transactional
public class AttendanceController extends Controller {

    @Inject
    IAttendanceTracking iAttendanceTracking;

    @Inject
    INotification iNotification;

    @Inject
    IAuthentication iAuthentication;

    @Inject
    IEmployeeApi iEmployee;

    @Inject
    MyObjectMapper objectMapper;

    @ApiOperation(value = "Employee Check In")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Checked-In", response = Boolean.class)}
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
    @WebAuth(permissions= {RoutePermissions.create_attendance }, roles = {RouteRole.admin, RouteRole.user})
    public Result clockIn(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        boolean result = iAttendanceTracking.checkIn(employeeId);
        String name = iEmployee.getEmployeeById(employeeId).orElseThrow().getFullName();

        if (result) {
            iNotification.sendNotification(employeeId, "system_employee", name, "", "attendance_check_in_successful");
        } else {
            iNotification.sendNotification(employeeId, "system_employee", name, "", "attendance_check_in_failure");
        }
        return ok(objectMapper.toJsonString(result));
    }

    @ApiOperation(value = "Employee Check Out")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Checked-out", response = Boolean.class)}
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
    @WebAuth(permissions= {RoutePermissions.update_attendance }, roles = {RouteRole.admin, RouteRole.user})
    public Result clockOut(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        boolean result = iAttendanceTracking.checkOut(employeeId);
        String name = iEmployee.getEmployeeById(employeeId).orElseThrow().getFullName();

        if (result) {
            iNotification.sendNotification(employeeId, "system_employee", name, "", "attendance_check_out_successful");
        } else {
            iNotification.sendNotification(employeeId, "system_employee", name, "", "attendance_check_out_failure");
        }
        return ok(objectMapper.toJsonString(result));
    }

    @ApiOperation(value = "Get an Attendance Record By ID")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Attendance Record", response = Attendance.class)}
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
    @WebAuth(permissions= {RoutePermissions.read_attendance }, roles = {RouteRole.admin, RouteRole.user})
    public Result getAttendance(String attendanceId) {
        Optional<Attendance> attendanceRecord = iAttendanceTracking.getAttendance(attendanceId);
        return attendanceRecord.map(attendance -> ok(objectMapper.toJsonString(attendance))).orElseGet(() -> notFound("Attendance Record not found"));
    }

    @ApiOperation(value = "Get Attendance Records for an Employee")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Attendance Record", response = Attendance.class, responseContainer = "List")}
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Filter information",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.attendance.tracking.dto.AttendanceFilterDTO"
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
    @WebAuth(permissions= {RoutePermissions.read_attendance }, roles = {RouteRole.admin, RouteRole.user})
    public Result getEmployeeAttendanceRecords() {
        final var attendanceFilterForm = validate(request().body().asJson(), AttendanceFilterDTO.class);

        if (attendanceFilterForm.hasError)
            return badRequest(attendanceFilterForm.error);

        return ok(objectMapper.toJsonString(iAttendanceTracking.getEmployeeAttendance(attendanceFilterForm.value)));
    }

    @ApiOperation(value = "Get all Attendance Records for an Employee")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Attendance Record", response = Attendance.class, responseContainer = "List")}
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
    @WebAuth(permissions= {RoutePermissions.read_attendance }, roles = {RouteRole.admin, RouteRole.user})
    public Result getAllAttendanceRecordForEmployee(String employeeId) {
        return ok(objectMapper.toJsonString(iAttendanceTracking.getAllEmployeeAttendance(employeeId)));
    }

    @ApiOperation(value = "Get Attendance Report for an Employee")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Attendance Report", response = AttendanceReport.class, responseContainer = "List")}
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Filter information",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.attendance.tracking.dto.AttendanceFilterDTO"
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
    @WebAuth(permissions= {RoutePermissions.read_attendance }, roles = {RouteRole.admin, RouteRole.user})
    public Result getEmployeeAttendanceReport() {
        final var attendanceFilterForm = validate(request().body().asJson(), AttendanceFilterDTO.class);

        if (attendanceFilterForm.hasError)
            return badRequest(attendanceFilterForm.error);

        return ok(objectMapper.toJsonString(iAttendanceTracking.getEmployeeAttendanceReport(attendanceFilterForm.value)));
    }

    @ApiOperation(value = "Get Attendance Records for Department")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Attendance Record", response = Attendance.class, responseContainer = "List")}
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Filter information",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.attendance.tracking.dto.AttendanceFilterDTO"
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
    @WebAuth(permissions= {RoutePermissions.read_attendance}, roles = {RouteRole.admin, RouteRole.user})
    public Result getDepartmentAttendance() {
        final var attendanceFilterForm = validate(request().body().asJson(), AttendanceFilterDTO.class);

        if (attendanceFilterForm.hasError)
            return badRequest(attendanceFilterForm.error);

        return ok(objectMapper.toJsonString(iAttendanceTracking.getDepartmentAttendance(attendanceFilterForm.value)));
    }

    @ApiOperation(value = "Get all Attendance Records for Department")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Attendance Record", response = Attendance.class, responseContainer = "List")}
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
    @WebAuth(permissions= {RoutePermissions.read_attendance }, roles = {RouteRole.admin, RouteRole.user})
    public Result getAllDepartmentAttendance(String departmentId) {
        return ok(objectMapper.toJsonString(iAttendanceTracking.getAllDepartmentAttendance(departmentId)));
    }

    @ApiOperation(value = "Get Attendance Report for Department")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Attendance Report", response = AttendanceReport.class, responseContainer = "List")}
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Filter information",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.attendance.tracking.dto.AttendanceFilterDTO"
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
    @WebAuth(permissions= {RoutePermissions.read_attendance }, roles = {RouteRole.admin, RouteRole.user})
    public Result getDepartmentAttendanceReport() {
        final var attendanceFilterForm = validate(request().body().asJson(), AttendanceFilterDTO.class);

        if (attendanceFilterForm.hasError)
            return badRequest(attendanceFilterForm.error);

        return ok(objectMapper.toJsonString(iAttendanceTracking.getDepartmentAttendanceReport(attendanceFilterForm.value)));
    }

    @ApiOperation(value = "Get System Attendance Record")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Attendance Record", response = Attendance.class, responseContainer = "List")}
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Filter information",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.attendance.tracking.dto.SystemFilterDTO"
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
    @WebAuth(permissions= {RoutePermissions.read_attendance }, roles = {RouteRole.admin})
    public Result getSystemAttendance() {
        final var systemFilterForm = validate(request().body().asJson(), SystemFilterDTO.class);

        if (systemFilterForm.hasError)
            return badRequest(systemFilterForm.error);

        return ok(objectMapper.toJsonString(iAttendanceTracking.getSystemAttendance(systemFilterForm.value)));
    }

    @ApiOperation(value = "Get all System Attendance Record")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Attendance Record", response = Attendance.class, responseContainer = "List")}
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
    @WebAuth(permissions= {RoutePermissions.read_attendance }, roles = {RouteRole.admin})
    public Result getAllSystemAttendance() {
        return ok(objectMapper.toJsonString(iAttendanceTracking.getAllSystemAttendance()));
    }

    @ApiOperation(value = "Get System Attendance Report")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Attendance Report", response = AttendanceReport.class, responseContainer = "List")}
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Filter information",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.attendance.tracking.dto.SystemFilterDTO"
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
    @WebAuth(permissions= {RoutePermissions.read_attendance }, roles = {RouteRole.admin})
    public Result getSystemAttendanceReport() {
        final var systemFilterForm = validate(request().body().asJson(), SystemFilterDTO.class);

        if (systemFilterForm.hasError)
            return badRequest(systemFilterForm.error);
        return ok(objectMapper.toJsonString(iAttendanceTracking.getSystemAttendanceReport(systemFilterForm.value)));
    }


    @ApiOperation(value = "Get Employee working hours (Attendance Report)")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Attendance Report", response = AttendanceReport.class)}
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Filter information",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.attendance.tracking.dto.AttendanceFilterDTO"
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
    @WebAuth(permissions= {RoutePermissions.read_attendance }, roles = {RouteRole.admin, RouteRole.user})
    public Result getEmployeeWorkingHours() {
        final var attendanceFilterForm = validate(request().body().asJson(), AttendanceFilterDTO.class);

        if (attendanceFilterForm.hasError)
            return badRequest(attendanceFilterForm.error);

        Optional<AttendanceReport> report = iAttendanceTracking.getEmployeeWorkingHours(attendanceFilterForm.value);

        return report.map(attendanceReport -> ok(objectMapper.toJsonString(attendanceReport))).orElseGet(() -> notFound("No report found"));

    }

    @ApiOperation(value = "Get Department working hours (Attendance Report)")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Attendance Report", response = AttendanceReport.class, responseContainer = "List")}
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Filter information",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.attendance.tracking.dto.AttendanceFilterDTO"
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
    @WebAuth(permissions= {RoutePermissions.read_attendance }, roles = {RouteRole.admin, RouteRole.user})
    public Result getDepartmentWorkingHours() {
        final var attendanceFilterForm = validate(request().body().asJson(), AttendanceFilterDTO.class);

        if (attendanceFilterForm.hasError)
            return badRequest(attendanceFilterForm.error);

        return ok(objectMapper.toJsonString(iAttendanceTracking.getDepartmentWorkingHours(attendanceFilterForm.value)));
    }

    @ApiOperation(value = "Get System working hours (Attendance Report)")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Attendance Report", response = AttendanceReport.class, responseContainer = "List")}
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Filter information",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.attendance.tracking.dto.SystemFilterDTO"
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
    @WebAuth(permissions= {RoutePermissions.read_attendance }, roles = {RouteRole.admin, RouteRole.user})
    public Result getSystemWorkingHours() {
        final var systemFilterForm = validate(request().body().asJson(), SystemFilterDTO.class);

        if (systemFilterForm.hasError)
            return badRequest(systemFilterForm.error);

        return ok(objectMapper.toJsonString(iAttendanceTracking.getWorkingHours(systemFilterForm.value)));
    }

    @ApiOperation(value = "Delete an Attendance record")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Record Deleted", response = boolean.class)}
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
    @WebAuth(permissions= {RoutePermissions.delete_attendance }, roles = {RouteRole.admin})
    public Result deleteAttendanceRecord(String attendanceRecordId) {

        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean result = iAttendanceTracking.deleteAttendanceRecord(attendanceRecordId);

        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "attendance_record_deletion_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "attendance_record_deletion_failure");
        }
        return ok(objectMapper.toJsonString(result));
    }

    @ApiOperation(value = "Delete an Attendance record")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Deleted", response = boolean.class)}
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
    @WebAuth(permissions= {RoutePermissions.delete_attendance }, roles = {RouteRole.admin})
    public Result deleteEmployeeAttendanceRecord(String employeeId) {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean result = iAttendanceTracking.deleteEmployeeAttendanceRecord(employeeId);

        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "attendance_record_deletion_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "attendance_record_deletion_failure");
        }
        return ok(objectMapper.toJsonString(result));
    }

    @ApiOperation(value = "Employee has Open Attendance?")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Open Attendance", response = boolean.class)}
    )
    public Result employeeHasOpenAttendance(String employeeId) {
        return ok(objectMapper.toJsonString(iAttendanceTracking.checkIfOpenAttendanceExists(employeeId)));
    }

}
