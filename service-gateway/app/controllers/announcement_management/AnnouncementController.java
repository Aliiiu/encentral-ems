package controllers.announcement_management;

import com.esl.internship.staffsync.announcement.management.api.IAnnouncementManagementApi;
import com.esl.internship.staffsync.announcement.management.dto.AnnouncementDTO;
import com.esl.internship.staffsync.announcement.management.model.Announcement;
import com.esl.internship.staffsync.announcement.management.model.EmployeeAnnouncement;
import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.authentication.model.RouteRole;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import security.WebAuth;

import javax.inject.Inject;

import java.util.Optional;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;

@Api("Announcement Management")
@Transactional
public class AnnouncementController extends Controller {

    @Inject
    IAnnouncementManagementApi iAnnouncementManagementApi;

    @Inject
    MyObjectMapper objectMapper;

    @Inject
    IAuthentication iAuthentication;

    @ApiOperation(value = "Create an Announcement")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Announcement Created", response = Announcement.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Announcement Information",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.announcement.management.dto.AnnouncementDTO"
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
    @WebAuth(permissions= {RoutePermissions.create_announcement }, roles = {RouteRole.admin})
    public Result createAnnouncement() {
        Optional<Employee> employeeOptional = iAuthentication.getContextCurrentEmployee();
        if (employeeOptional.isEmpty())
            return unauthorized();

        Employee employee = employeeOptional.get();

        final var announcementDtoForm = validate(request().body().asJson(), AnnouncementDTO.class);

        if (announcementDtoForm.hasError)
            return badRequest(announcementDtoForm.error);

        Response<Announcement> response = iAnnouncementManagementApi.createAnnouncement(
                employee.getEmployeeId(), announcementDtoForm.value, employee
        );
        if (response.requestHasErrors())
            return badRequest(response.getErrorsAsJsonString());
        return ok(objectMapper.toJsonString(response.getValue()));
    }

    @ApiOperation(value = "Get Announcement by Id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Announcement", response = Announcement.class)
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
    @WebAuth(permissions= {RoutePermissions.read_announcement }, roles = {RouteRole.admin})
    public Result getAnnouncementRecordById(@ApiParam(value = "ID of the announcement Record to fetch") String announcementId) {
        Optional<Announcement> result = iAnnouncementManagementApi.getAnnouncementRecordById(announcementId);
        return result.map(announcement -> ok(objectMapper.toJsonString(announcement))).orElseGet(() -> notFound("Announcement record not found"));
    }

    @ApiOperation(value = "Get all Announcement records")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Announcements", response = Announcement.class, responseContainer = "List")
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
    @WebAuth(permissions= {RoutePermissions.read_announcement }, roles = {RouteRole.admin})
    public Result getAllAnnouncementRecords() {
        return ok(objectMapper.toJsonString(iAnnouncementManagementApi.getAllAnnouncementRecords()));
    }

    @ApiOperation(value = "Update an Announcement")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Announcement Updated", response = boolean.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Announcement Information",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.announcement.management.dto.AnnouncementDTO"
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
    @WebAuth(permissions= {RoutePermissions.update_announcement }, roles = {RouteRole.admin})
    public Result updateAnnouncementRecord(@ApiParam(value = "ID of the announcement Record to Update")String announcementId) {
        Optional<Employee> employeeOptional = iAuthentication.getContextCurrentEmployee();
        if (employeeOptional.isEmpty())
            return unauthorized();

        Employee employee = employeeOptional.get();

        final var announcementDtoForm = validate(request().body().asJson(), AnnouncementDTO.class);

        if (announcementDtoForm.hasError)
            return badRequest(announcementDtoForm.error);
        return ok(objectMapper.toJsonString(iAnnouncementManagementApi.updateAnAnnouncement(
                announcementId, announcementDtoForm.value, employee
        )));
    }

    @ApiOperation(value = "Delete an Announcement")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Announcement Deleted", response = boolean.class)
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
    @WebAuth(permissions= {RoutePermissions.delete_announcement }, roles = {RouteRole.admin})
    public Result deleteAnnouncementRecord(@ApiParam(value = "ID of the announcement Record to delete") String announcementId) {
        return ok(objectMapper.toJsonString(iAnnouncementManagementApi.deleteAnAnnouncementRecord(announcementId)));
    }

    @ApiOperation(value = "Get all Posted announcements for Employee")
    @ApiResponses({
            @ApiResponse(code = 200, message = "EmployeeAnnouncement", response = EmployeeAnnouncement.class)
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
    @WebAuth(permissions= {RoutePermissions.read_announcement }, roles = {RouteRole.admin, RouteRole.user})
    public Result getAllEmployeeAnnouncements() {
        Optional<Employee> employeeOptional = iAuthentication.getContextCurrentEmployee();
        if (employeeOptional.isEmpty())
            return unauthorized();

        Employee employee = employeeOptional.get();

        return ok(objectMapper.toJsonString(iAnnouncementManagementApi.getAllEmployeeAnnouncementsOrderedByDate(employee.getEmployeeId())));
    }

    @ApiOperation(value = "Get all Posted Unread announcements for Employee")
    @ApiResponses({
            @ApiResponse(code = 200, message = "EmployeeAnnouncement", response = EmployeeAnnouncement.class)
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
    @WebAuth(permissions= {RoutePermissions.read_announcement }, roles = {RouteRole.admin, RouteRole.user})
    public Result getAllUnreadEmployeeAnnouncements() {
        Optional<Employee> employeeOptional = iAuthentication.getContextCurrentEmployee();
        if (employeeOptional.isEmpty())
            return unauthorized();

        Employee employee = employeeOptional.get();
        return ok(objectMapper.toJsonString(iAnnouncementManagementApi.getAllUnreadEmployeeAnnouncementsOrderedByDate(employee.getEmployeeId())));
    }

    @ApiOperation(value = "Mark a posted announcement for Employee as read")
    @ApiResponses({
            @ApiResponse(code = 200, message = "EmployeeAnnouncement", response = boolean.class)
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
    @WebAuth(permissions= {RoutePermissions.read_announcement}, roles = {RouteRole.admin, RouteRole.user})
    public Result markEmployeeAnnouncementAsRead(@ApiParam(value = "ID of the announcementRecipient to mark read")String announcementRecipientId) {
        return ok(objectMapper.toJsonString(iAnnouncementManagementApi.markAnnouncementAsRead(announcementRecipientId)));
    }

    @ApiOperation(value = "Mark Posted all announcements for Employee as read")
    @ApiResponses({
            @ApiResponse(code = 200, message = "EmployeeAnnouncement", response = boolean.class)
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
    @WebAuth(permissions= {RoutePermissions.read_announcement}, roles = {RouteRole.admin, RouteRole.user})
    public Result markAllEmployeeAnnouncementsAsRead() {
        Optional<Employee> employeeOptional = iAuthentication.getContextCurrentEmployee();
        if (employeeOptional.isEmpty())
            return unauthorized();
        Employee employee = employeeOptional.get();

        return ok(objectMapper.toJsonString(iAnnouncementManagementApi.markAllAnnouncementsForEmployeeAsRead(employee.getEmployeeId())));
    }
}
