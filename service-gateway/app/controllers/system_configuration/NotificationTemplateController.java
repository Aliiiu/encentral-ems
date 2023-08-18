package controllers.system_configuration;

import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.authentication.model.RouteRole;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.system.configuration.api.INotification;
import com.esl.internship.staffsync.system.configuration.api.INotificationTemplate;
import com.esl.internship.staffsync.system.configuration.dto.CreateNotificationTemplateDTO;
import com.esl.internship.staffsync.system.configuration.dto.EditNotificationTemplateDTO;
import com.esl.internship.staffsync.system.configuration.model.Notification;
import com.esl.internship.staffsync.system.configuration.model.NotificationTemplate;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import security.WebAuth;

import javax.inject.Inject;
import java.util.Optional;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;

@Transactional
@Api("Notification template")
public class NotificationTemplateController extends Controller {

    @Inject
    INotificationTemplate iNotificationTemplate;

    @Inject
    INotification iNotification;

    @Inject
    IAuthentication iAuthentication;

    @Inject
    MyObjectMapper myObjectMapper;

    @ApiOperation("Get Notification template by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = NotificationTemplate.class, message = "Notification Template retrieved"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid notification template id"),
                    @ApiResponse(code = 404, response = String.class, message = "Notification template not found"),
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
    @WebAuth(permissions = {RoutePermissions.read_notification_template}, roles = {RouteRole.admin, RouteRole.user})
    public Result getNotificationTemplate(@ApiParam(value = "Notification template Id", required = true) String notificationTemplateId) {

        if (notificationTemplateId.length() == 0) {
            return Results.badRequest("Invalid notification template id");
        }
        return iNotificationTemplate.getNotificationTemplate(notificationTemplateId)
                .map(e -> Results.ok(myObjectMapper.toJsonString(e)))
                .orElseGet(Results::notFound);
    }

    @ApiOperation("Get all notification templates")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = NotificationTemplate.class, responseContainer = "List", message = "NotificationTemplates"),
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
    @WebAuth(permissions = {RoutePermissions.read_notification_template}, roles = {RouteRole.admin, RouteRole.user})
    public Result getAllNotificationTemplates() {
        return Results.ok(myObjectMapper.toJsonString(
                iNotificationTemplate.getAllNotificationTemplates()
        ));
    }

    @ApiOperation("Get all notifications created with a template")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Notification.class, responseContainer = "List", message = "Notifications retrieved"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid notification template id"),
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
    @WebAuth(permissions = {RoutePermissions.read_notification_template}, roles = {RouteRole.admin, RouteRole.user})
    public Result getTemplateNotifications(@ApiParam(value = "Notification template Id", required = true) String notificationTemplateId) {

        if (notificationTemplateId.length() == 0) {
            return Results.badRequest("Invalid notification template id");
        }
        return Results.ok(myObjectMapper.toJsonString(
                iNotificationTemplate.getNotifications(notificationTemplateId)
        ));
    }

    @ApiOperation("Create a notification template")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = NotificationTemplate.class, message = "NotificationTemplate created"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid data"),
                    @ApiResponse(code = 409, response = String.class, message = "Notification template name already in use"),
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Notification template creation DTO",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.system.configuration.dto.CreateNotificationTemplateDTO",
                    dataTypeClass = CreateNotificationTemplateDTO.class
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
    @WebAuth(permissions = {RoutePermissions.create_notification_template}, roles = {RouteRole.admin})
    public Result createNotificationTemplate() {

        final var notificationTemplateCreationForm = validate(request().body().asJson(), CreateNotificationTemplateDTO.class);
        if (notificationTemplateCreationForm.hasError) {
            return Results.badRequest(notificationTemplateCreationForm.error);
        }
        CreateNotificationTemplateDTO notificationTemplateDTO = notificationTemplateCreationForm.value;

        if (iNotificationTemplate.checkIfNotificationTemplateNameInUse(notificationTemplateDTO.getNotificationTemplateName())) {
            return Results.status(409, "Notification template name already in use");
        }

        try {
            Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
            NotificationTemplate template = iNotificationTemplate.createNotificationTemplate(
                    notificationTemplateDTO,
                    iAuthentication.getContextCurrentEmployee().orElseThrow()
            );
            if (template != null) {
                iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), template.getNotificationTemplateName(), "notification_template_creation_successful");
            } else {
                iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "anotification_template_creation_failure");
            }
            return ok(myObjectMapper.toJsonString(template));
        } catch (Exception e) {
            return Results.badRequest("Invalid data");
        }

    }

    @ApiOperation("Edit a notification template")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, response = Boolean.class, message = "Changes saved"),
                    @ApiResponse(code = 400, response = String.class, message = "Invalid data"),
                    @ApiResponse(code = 404, response = String.class, message = "Notification template not found"),
                    @ApiResponse(code = 409, response = String.class, message = "Notification template name already in use")
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Notification template creation DTO",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.system.configuration.dto.CreateNotificationTemplateDTO",
                    dataTypeClass = CreateNotificationTemplateDTO.class
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
    public Result editNotificationTemplate() {

        final var notificationTemplateEditForm = validate(request().body().asJson(), EditNotificationTemplateDTO.class);
        if (notificationTemplateEditForm.hasError) {
            return Results.badRequest(notificationTemplateEditForm.error);
        }
        EditNotificationTemplateDTO notificationTemplateDTO = notificationTemplateEditForm.value;

        Optional<NotificationTemplate> nt = iNotificationTemplate.getNotificationTemplate(
                notificationTemplateDTO.getNotificationTemplateId()
        );

        if (nt.isPresent()) {
            String templateName = nt.get().getNotificationTemplateName();
            String templateId = nt.get().getNotificationTemplateId();
            String dtoId = notificationTemplateDTO.getNotificationTemplateId();

            if (!dtoId.equals(templateId) && iNotificationTemplate.checkIfNotificationTemplateNameInUse(templateName)) {
                return Results.status(409, "Notification template name already in use");
            }
            Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
            boolean resp = iNotificationTemplate.editNotificationTemplate(
                    notificationTemplateDTO,
                    employee
            );
            if (resp) {
                iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "notification_template_update_successful");
            } else {
                iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "notification_template_update_failure");
            }
            return Results.ok(myObjectMapper.toJsonString(resp));
        }
        return Results.notFound("Notification template not found");
    }
}
