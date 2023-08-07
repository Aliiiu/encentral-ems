package controllers.event_management;

import com.encentral.scaffold.commons.model.Employee;
import com.encentral.scaffold.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.event.management.api.IEventApi;
import com.esl.internship.staffsync.event.management.dto.EventDTO;
import com.esl.internship.staffsync.event.management.model.Event;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;

import static com.encentral.scaffold.commons.util.ImmutableValidator.validate;

@Api("Event Management")
@Transactional
public class EventController extends Controller  {
    @Inject
    IEventApi iEventApi;

    @Inject
    MyObjectMapper myObjectMapper;

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
            )
    })
    public Result addEvent() {
        final var eventForm = validate(request().body().asJson(), EventDTO.class);
        if (eventForm.hasError) {
            return badRequest(eventForm.error);
        }
        return ok(myObjectMapper.toJsonString(iEventApi.addEvent(eventForm.value, getTestEmployee())));
    }

    @ApiOperation(value = "Get event by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Event", response = Event.class)}
    )
    public Result getEvent(String eventId) {
        return iEventApi.getEventById(eventId)
                .map(e -> ok(myObjectMapper.toJsonString(e))).orElseGet(Results::notFound);
    }

    @ApiOperation(value = "Get all events")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Events", response = Event.class, responseContainer = "List")}
    )
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
            )
    })
    public Result updateEvent(String eventId) {
        final var eventForm = validate(request().body().asJson(), EventDTO.class);
        if (eventForm.hasError) {
            return badRequest(eventForm.error);
        }
        return ok(myObjectMapper.toJsonString(iEventApi.updateEvent(eventId, eventForm.value, getTestEmployee())));
    }

    @ApiOperation(value = "Delete event")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Deleted", response = boolean.class)}
    )
    public Result deleteEvent(String eventId) {
        return ok(myObjectMapper.toJsonString(iEventApi.deleteEvent(eventId)));
    }

    private Employee getTestEmployee() {
        return new Employee("Employee-001", "EmployeeName");
    }

}
