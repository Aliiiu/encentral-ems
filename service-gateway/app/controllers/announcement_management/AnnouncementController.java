package controllers.announcement_management;

import com.esl.internship.staffsync.announcement.management.api.IAnnouncementManagementApi;
import com.esl.internship.staffsync.announcement.management.dto.AnnouncementDTO;
import com.esl.internship.staffsync.announcement.management.model.Announcement;
import com.esl.internship.staffsync.announcement.management.model.EmployeeAnnouncement;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

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
            )
    })
    public Result createAnnouncement(String announcerId) {
        final var announcementDtoForm = validate(request().body().asJson(), AnnouncementDTO.class);

        if (announcementDtoForm.hasError)
            return badRequest(announcementDtoForm.error);

        Response<Announcement> response = iAnnouncementManagementApi.createAnnouncement(
                announcerId, announcementDtoForm.value, getEmployee()
        );
        if (response.requestHasErrors())
            return badRequest(response.getErrorsAsJsonString());
        return ok(objectMapper.toJsonString(response.getValue()));
    }

    @ApiOperation(value = "Get Announcement by Id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Announcement", response = Announcement.class)
    })
    public Result getAnnouncementRecordById(String announcementId) {
        Optional<Announcement> result = iAnnouncementManagementApi.getAnnouncementRecordById(announcementId);
        return result.map(announcement -> ok(objectMapper.toJsonString(announcement))).orElseGet(() -> notFound("Announcement record not found"));
    }

    @ApiOperation(value = "Get all Announcement records")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Announcements", response = Announcement.class, responseContainer = "List")
    })
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
            )
    })
    public Result updateAnnouncementRecord(String announcementId) {
        final var announcementDtoForm = validate(request().body().asJson(), AnnouncementDTO.class);

        if (announcementDtoForm.hasError)
            return badRequest(announcementDtoForm.error);
        return ok(objectMapper.toJsonString(iAnnouncementManagementApi.updateAnAnnouncement(
                announcementId, announcementDtoForm.value, getEmployee()
        )));
    }

    @ApiOperation(value = "Delete an Announcement")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Announcement Deleted", response = boolean.class)
    })
    public Result deleteAnnouncementRecord(String announcementId) {
        return ok(objectMapper.toJsonString(iAnnouncementManagementApi.deleteAnAnnouncementRecord(announcementId)));
    }

    @ApiOperation(value = "Get all Posted announcements for Employee")
    @ApiResponses({
            @ApiResponse(code = 200, message = "EmployeeAnnouncement", response = EmployeeAnnouncement.class)
    })
    public Result getAllEmployeeAnnouncements(String employeeId) {
        return ok(objectMapper.toJsonString(iAnnouncementManagementApi.getAllEmployeeAnnouncementsOrderedByDate(employeeId)));
    }

    @ApiOperation(value = "Get all Posted Unread announcements for Employee")
    @ApiResponses({
            @ApiResponse(code = 200, message = "EmployeeAnnouncement", response = EmployeeAnnouncement.class)
    })
    public Result getAllUnreadEmployeeAnnouncements(String employeeId) {
        return ok(objectMapper.toJsonString(iAnnouncementManagementApi.getAllUnreadEmployeeAnnouncementsOrderedByDate(employeeId)));
    }

    @ApiOperation(value = "Mark Posted announcements for Employee as read")
    @ApiResponses({
            @ApiResponse(code = 200, message = "EmployeeAnnouncement", response = boolean.class)
    })
    public Result markEmployeeAnnouncementAsRead(String announcementRecipientId) {
        return ok(objectMapper.toJsonString(iAnnouncementManagementApi.markAnnouncementAsRead(announcementRecipientId)));
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
