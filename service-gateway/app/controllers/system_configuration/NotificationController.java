package controllers.system_configuration;


import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.authentication.model.RouteRole;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.system.configuration.api.INotification;
import com.esl.internship.staffsync.system.configuration.api.INotificationTemplate;
import com.esl.internship.staffsync.system.configuration.dto.CreateNotificationDTO;
import com.esl.internship.staffsync.system.configuration.model.Notification;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import security.WebAuth;

import javax.inject.Inject;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;

@Transactional
@Api("Notification")
public class NotificationController extends Controller {

    @Inject
    INotification iNotification;

    @Inject
    INotificationTemplate iNotificationTemplate;

    @Inject
    IAuthentication iAuthentication;

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
    @WebAuth(permissions = {RoutePermissions.read_notification}, roles = {RouteRole.admin, RouteRole.user})
    public Result getNotification(@ApiParam(value = "Notification Id", required = true) String notificationId) {

        if (notificationId.length() == 0) {
            return Results.badRequest("Invalid notification id");
        }
        return iNotification.getNotification(notificationId)
                .map(e -> Results.ok(myObjectMapper.toJsonString(e)))
                .orElseGet(Results::notFound);
    }

    @ApiOperation("Get List of all an Employee's received Notifications by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Notification.class, responseContainer = "List", message = "Notification retrieved"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid Employee id"),
                    @ApiResponse(code = 401, response = String.class, message = "Employee is not authorized to access this resource"),
                    @ApiResponse(code = 404, response = String.class, message = "Employee Notification not found")
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
    @WebAuth(permissions = {RoutePermissions.read_notification}, roles = {RouteRole.admin, RouteRole.user})
    public Result getEmployeeNotifications(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        if (employeeId.length() == 0) {
            return Results.badRequest("Invalid Employee id");
        }
        if (!iAuthentication.checkIfUserIsCurrentEmployeeOrAdmin(employeeId)) {
            return Results.unauthorized("Employee is not authorized to access this resource");
        }
        return Results.ok(myObjectMapper.toJsonString(
                iNotification.getEmployeeReceivedNotifications(employeeId)
        ));
    }

    @ApiOperation("Get List of all Notifications in the system")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Notification.class, responseContainer = "List", message = "Notifications retrieved"),
                    @ApiResponse(code = 401, response = String.class, message = "Employee is not authorized to access this resource"),
                    @ApiResponse(code = 404, response = String.class, message = "Employee Notification not found")
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
    public Result getSystemNotifications() {
        return Results.ok(myObjectMapper.toJsonString(
                iNotification.getSystemNotifications()
        ));
    }

    @ApiOperation("Get List of all an Employee's sent Notifications by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Notification.class, responseContainer = "List", message = "Notification retrieved"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid Employee id"),
                    @ApiResponse(code = 401, response = String.class, message = "Employee is not authorized to access this resource"),
                    @ApiResponse(code = 404, response = String.class, message = "Employee Notifications not found")
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
    @WebAuth(permissions = {RoutePermissions.read_notification}, roles={RouteRole.admin, RouteRole.user})
    public Result getEmployeeSentNotifications(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        if (employeeId.length() == 0) {
            return Results.badRequest("Invalid Employee id");
        }
        if (!iAuthentication.checkIfUserIsCurrentEmployeeOrAdmin(employeeId)) {
            return Results.unauthorized("Employee is not authorized to access this resource");
        }
        return Results.ok(myObjectMapper.toJsonString(
                iNotification.getEmployeeSentNotifications(employeeId)
        ));
    }

    @ApiOperation("Get List of an employee's unread notifications by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Notification.class, responseContainer = "List", message = "Notifications retrieved"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid Employee id"),
                    @ApiResponse(code = 401, response = String.class, message = "Employee is not authorized to access this resource"),
                    @ApiResponse(code = 404, response = String.class, message = "Employee Notification not found")
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
    @WebAuth(permissions= {RoutePermissions.read_notification }, roles ={RouteRole.admin, RouteRole.user})
    public Result getEmployeeUnReadNotifications(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        if (!iAuthentication.checkIfUserIsCurrentEmployeeOrAdmin(employeeId)) {
            return Results.unauthorized("Employee is not authorized to access this resource");
        }
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
    @WebAuth(permissions= {RoutePermissions.update_notification }, roles ={RouteRole.admin, RouteRole.user})
    public Result markNotificationAsRead(@ApiParam(value = "Notification Id", required = true) String notificationId) {
        if (notificationId.length() == 0) {
            return Results.badRequest("Invalid notification id");
        }

        String employeeId = iAuthentication.getContextCurrentEmployee().orElseThrow().getEmployeeId();
        if (iNotification.verifyEmployee(employeeId, notificationId)|| iAuthentication.checkIfUserIsCurrentEmployeeOrAdmin(employeeId)) {
            return Results.unauthorized("Employee is not authorized to access this resource");
        }

        return ok(myObjectMapper.toJsonString(
                iNotification.markNotificationAsRead(notificationId, iAuthentication.getContextCurrentEmployee().orElseThrow())
        ));
    }

    @ApiOperation("Mark a notification as deleted by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Boolean.class, message = "Notification marked as deleted"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid notification id"),
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
    @WebAuth(permissions= {RoutePermissions.update_notification }, roles ={ RouteRole.admin, RouteRole.user})
    public Result markNotificationAsDeleted(@ApiParam(value = "Notification Id", required = true) String notificationId) {
        if (notificationId.length() == 0) {
            return Results.badRequest("Invalid notification id");
        }
        String employeeId = iAuthentication.getContextCurrentEmployee().orElseThrow().getEmployeeId();
        if (iNotification.verifyEmployee(employeeId, notificationId) || iAuthentication.checkIfUserIsCurrentEmployeeOrAdmin(employeeId)) {
            return Results.unauthorized("Employee is not authorized to access this resource");
        }

        return ok(myObjectMapper.toJsonString(
                iNotification.markNotificationAsDeleted(notificationId, iAuthentication.getContextCurrentEmployee().orElseThrow())
        ));
    }


    @ApiOperation("Delete a Notification by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Boolean.class, message = "Notification deleted"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid notification id"),
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
    @WebAuth(permissions= {RoutePermissions.delete_notification }, roles ={RouteRole.admin})
    public Result deleteNotification(@ApiParam(value = "Notification Id", required = true) String notificationId) {
        if (notificationId.length() == 0) {
            return Results.badRequest("Invalid notification id");
        }
        String employeeId = iAuthentication.getContextCurrentEmployee().orElseThrow().getEmployeeId();
        if (iNotification.verifyEmployee(employeeId, notificationId) || iAuthentication.checkIfUserIsCurrentEmployeeOrAdmin(employeeId)) {
            return Results.unauthorized("Employee is not authorized to access this resource");
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
    @WebAuth(permissions= {RoutePermissions.delete_notification }, roles ={RouteRole.admin})
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
    @WebAuth(permissions= {RoutePermissions.update_notification }, roles ={RouteRole.admin, RouteRole.user})
    public Result markAllEmployeeNotificationsAsDeleted(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        if (employeeId.length() == 0) {
            return Results.badRequest("Invalid employee id");
        }
        if (!iAuthentication.checkIfUserIsCurrentEmployeeOrAdmin(employeeId)) {
            return Results.unauthorized("Employee is not authorized to access this resource");
        }
        return ok(myObjectMapper.toJsonString(
                iNotification.markAllEmployeeNotificationAsDeleted(employeeId, iAuthentication.getContextCurrentEmployee().orElseThrow())
        ));
    }

    @ApiOperation("Mark all an employee's notifications as read by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Boolean.class, message = "Notifications marked as reed"),
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
    @WebAuth(permissions= {RoutePermissions.update_notification }, roles ={RouteRole.admin, RouteRole.user})
    public Result markAllEmployeeNotificationsAsRead(@ApiParam(value = "Employee Id", required = true) String employeeId) {
        if (employeeId.length() == 0) {
            return Results.badRequest("Invalid employee id");
        }
        if (!iAuthentication.checkIfUserIsCurrentEmployeeOrAdmin(employeeId)) {
            return Results.unauthorized("Employee is not authorized to access this resource");
        }
        return ok(myObjectMapper.toJsonString(
                iNotification.markAllEmployeeNotificationAsRead(employeeId, iAuthentication.getContextCurrentEmployee().orElseThrow())
        ));
    }

    @ApiOperation("Create a notification")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Notification.class, message = "Notification created"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid data"),
                    @ApiResponse(code = 404, response = String.class, message = "Notification template not found")
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
    @WebAuth(permissions= {RoutePermissions.create_notification }, roles ={RouteRole.admin})
    public Result createNotification() {
        final var notificationCreationForm = validate(request().body().asJson(), CreateNotificationDTO.class);
        if (notificationCreationForm.hasError) {
            return Results.badRequest(notificationCreationForm.error);
        }
        CreateNotificationDTO notificationDTO = notificationCreationForm.value;
        boolean res = iNotificationTemplate.checkIfNotificationTemplateExists(
                notificationDTO.getNotificationTemplateBeanId()
        );
        if (!res) return Results.notFound("Notification template not found");
        return ok(myObjectMapper.toJsonString(iNotification.createNotification(
                notificationDTO,
                iAuthentication.getContextCurrentEmployee().orElseThrow().getEmployeeId()
        )));
    }

}

