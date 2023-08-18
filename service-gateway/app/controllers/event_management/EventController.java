package controllers.event_management;

import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.authentication.model.RouteRole;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.event.management.api.IEventApi;
import com.esl.internship.staffsync.event.management.dto.EventDTO;
import com.esl.internship.staffsync.event.management.model.Event;
import com.esl.internship.staffsync.system.configuration.api.INotification;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import security.WebAuth;

import javax.inject.Inject;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;

@Api("Event Management")
@Transactional
public class EventController extends Controller  {
    @Inject
    IEventApi iEventApi;

    @Inject
    MyObjectMapper myObjectMapper;

    @Inject
    INotification iNotification;

    @Inject
    IAuthentication iAuthentication;

    @ApiOperation(value = "Create an event")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Event Created", response = Event.class)}
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Event data to be created",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.event.management.dto.EventDTO"
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
    @WebAuth(permissions = {RoutePermissions.create_event}, roles = {RouteRole.admin, RouteRole.user})
    public Result addEvent() {
        final var eventForm = validate(request().body().asJson(), EventDTO.class);
        if (eventForm.hasError) {
            return badRequest(eventForm.error);
        }
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        Event event = iEventApi.addEvent(eventForm.value, employee);
        if (event != null) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), event.getEventTitle(), "event_creation_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "event_creation_failure");
        }
        return ok(myObjectMapper.toJsonString(event));
    }

    @ApiOperation(value = "Get event by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Event", response = Event.class)}
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
    @WebAuth(permissions = {RoutePermissions.read_event}, roles = {RouteRole.admin, RouteRole.user})
    public Result getEvent(String eventId) {
        return iEventApi.getEventById(eventId)
                .map(e -> ok(myObjectMapper.toJsonString(e))).orElseGet(Results::notFound);
    }

    @ApiOperation(value = "Get all events")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Events", response = Event.class, responseContainer = "List")}
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
    @WebAuth(permissions = {RoutePermissions.read_event}, roles = {RouteRole.admin, RouteRole.user})
    public Result getAllEvents() {
        return ok(myObjectMapper.toJsonString(iEventApi.getAllEvents()));
    }

    @ApiOperation(value = "Update an event")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Event Updated", response = boolean.class)}
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Event data to update",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.event.management.dto.EventDTO"
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
    @WebAuth(permissions = {RoutePermissions.update_event}, roles = {RouteRole.admin, RouteRole.user})
    public Result updateEvent(String eventId) {
        final var eventForm = validate(request().body().asJson(), EventDTO.class);
        if (eventForm.hasError) {
            return badRequest(eventForm.error);
        }
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean result = iEventApi.updateEvent(eventId, eventForm.value, employee);
        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "event_update_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "event_update_failure");
        }
        return ok(myObjectMapper.toJsonString(result));
    }

    @ApiOperation(value = "Delete event")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Deleted", response = boolean.class)}
    )
    public Result deleteEvent(String eventId) {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean result = iEventApi.deleteEvent(eventId);
        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "event_deletion_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "event_deletion_failure");
        }
        return ok(myObjectMapper.toJsonString(result));
    }

}
