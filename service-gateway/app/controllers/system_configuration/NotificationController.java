package controllers.system_configuration;


import com.encentral.scaffold.commons.model.Employee;
import com.encentral.scaffold.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.system.configuration.api.INotification;
import com.esl.internship.staffsync.system.configuration.model.Notification;
import com.esl.internship.staffsync.system.configuration.dto.CreateNotificationDTO;
import io.swagger.annotations.*;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.db.jpa.Transactional;
import javax.inject.Inject;

import static com.encentral.scaffold.commons.util.ImmutableValidator.validate;

@Transactional
@Api("Notification")
public class NotificationController extends Controller {

    @Inject
    INotification iNotification;

    @Inject
    MyObjectMapper myObjectMapper;

    @ApiOperation("Get single Notification by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Notification.class, message = "Notification retrieved"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid notification id"),
                    @ApiResponse(code = 404, response = String.class, message = "Notification not found")
            }
    )
    public Result getNotification(@ApiParam(value = "Notification Id", required = true) String notificationId) {

        if (notificationId.length() == 0) {
            return Results.badRequest("Invalid notification id");
        }
        //TODO: Check if AppUser ID matches receiver id or is Admin
        return iNotification.getNotification(notificationId)
                .map(e -> Results.ok(myObjectMapper.toJsonString(e)))
                .orElseGet(Results::notFound);
    }

    @ApiOperation("Get List of all an Employee's received Notifications by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Notification.class, responseContainer = "List", message = "Notification retrieved"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid Employee id"),
                    @ApiResponse(code = 404, response = String.class, message = "Employee Notification not found")
            }
    )


    public Result getEmployeeNotifications(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        if (employeeId.length() == 0) {
            return Results.badRequest("Invalid Employee id");
        }
        //TODO: Check if AppUser ID matches receiver id or is admin
        return Results.ok(myObjectMapper.toJsonString(
                iNotification.getEmployeeReceivedNotifications(employeeId)
        ));
    }

    @ApiOperation("Get List of all Notifications in the system")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Notification.class, responseContainer = "List", message = "Notifications retrieved"),
                    @ApiResponse(code = 404, response = String.class, message = "Employee Notification not found")
            }
    )
    public Result getSystemNotifications() {
        //TODO : Check if Admin
        return Results.ok(myObjectMapper.toJsonString(
                iNotification.getSystemNotifications()
        ));
    }

    @ApiOperation("Get List of all an Employee's sent Notifications by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Notification.class, responseContainer = "List", message = "Notification retrieved"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid Employee id"),
                    @ApiResponse(code = 404, response = String.class, message = "Employee Notifications not found")
            }
    )


    public Result getEmployeeSentNotifications(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        if (employeeId.length() == 0) {
            return Results.badRequest("Invalid Employee id");
        }
        //TODO: Check if AppUser ID matches receiver id or is admin
        return Results.ok(myObjectMapper.toJsonString(
                iNotification.getEmployeeSentNotifications(employeeId)
        ));
    }

    @ApiOperation("Get List of an employee's unread notifications by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Notification.class, responseContainer = "List", message = "Notifications retrieved"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid Employee id"),
                    @ApiResponse(code = 404, response = String.class, message = "Employee Notification not found")
            }
    )
    public Result getEmployeeUnReadNotifications(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        //TODO: Check if AppUser ID matches receiver id or if Admin
        if (employeeId.length() == 0) {
            return Results.badRequest("Invalid employee id");
        }
        return Results.ok(myObjectMapper.toJsonString(
                iNotification.getEmployeeUnreadNotifications(employeeId)
        ));
    }

    @ApiOperation("Mark a notification as read by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Boolean.class, message = "Notification marked as read"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid notification id"),
            }
    )
    public Result markNotificationAsRead(@ApiParam(value = "Notification Id", required = true) String notificationId) {
        if (notificationId.length() == 0) {
            return Results.badRequest("Invalid notification id");
        }
        //TODO: Check if receiver id matches user id
        return ok(myObjectMapper.toJsonString(
                iNotification.markNotificationAsRead(notificationId, getTestEmployee())
        ));
    }

    @ApiOperation("Mark a notification as deleted by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Boolean.class, message = "Notification marked as deleted"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid notification id"),
            }
    )
    public Result markNotificationAsDeleted(@ApiParam(value = "Notification Id", required = true) String notificationId) {
        if (notificationId.length() == 0) {
            return Results.badRequest("Invalid notification id");
        }
        return ok(myObjectMapper.toJsonString(
                iNotification.markNotificationAsDeleted(notificationId, getTestEmployee())
        ));
    }


    @ApiOperation("Delete a Notification by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Boolean.class, message = "Notification deleted"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid notification id"),
            }
    )
    public Result deleteNotification(@ApiParam(value = "Notification Id", required = true) String notificationId) {
        if (notificationId.length() == 0) {
            return Results.badRequest("Invalid notification id");
        }
        return ok(myObjectMapper.toJsonString(
                iNotification.deleteNotification(notificationId)
        ));
    }

    @ApiOperation("Delete all an employee's notifications by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Boolean.class, message = "Notifications deleted"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid employee id"),
            }
    )
    public Result deleteAllEmployeeNotifications(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        if (employeeId.length() == 0) {
            return Results.badRequest("Invalid employee id");
        }
        return ok(myObjectMapper.toJsonString(
                iNotification.deleteAllEmployeeNotification(employeeId)
        ));
    }

    @ApiOperation("Mark all an employee's notifications as deleted by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Boolean.class, message = "Notifications marked as deleted"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid employee id"),
            }
    )
    public Result markAllEmployeeNotificationsAsDeleted(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        if (employeeId.length() == 0) {
            return Results.badRequest("Invalid employee id");
        }
        return ok(myObjectMapper.toJsonString(
                iNotification.markAllEmployeeNotificationAsDeleted(employeeId, getTestEmployee())
        ));
    }

    @ApiOperation("Mark all an employee's notifications as read by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Boolean.class, message = "Notifications marked as reed"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid employee id"),
            }
    )
    public Result markAllEmployeeNotificationsAsRead(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        if (employeeId.length() == 0) {
            return Results.badRequest("Invalid employee id");
        }
        return ok(myObjectMapper.toJsonString(
                iNotification.markAllEmployeeNotificationAsRead(employeeId, getTestEmployee())
        ));
    }

    @ApiOperation("Create a notification")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Notification.class, message = "Notification created"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid data"),
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Notification Creation DTO",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.system.configuration.dto.CreateNotificationDTO",
                    dataTypeClass = CreateNotificationDTO.class
            )
    })
    public Result createNotification() {
        final var notificationCreationForm = validate(request().body().asJson(), CreateNotificationDTO.class);
        if (notificationCreationForm.hasError) {
            return Results.badRequest(notificationCreationForm.error);
        }
        //TODO: Check if user is admin
        CreateNotificationDTO notificationDTO = notificationCreationForm.value;

        return ok(myObjectMapper.toJsonString(iNotification.createNotification(
                notificationDTO,
                getTestEmployee()
        )));
    }


    private Employee getTestEmployee() {
        return new Employee("12345", "employee");
    }
}

