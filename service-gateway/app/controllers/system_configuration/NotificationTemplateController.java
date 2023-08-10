package controllers.system_configuration;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
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

import javax.inject.Inject;
import java.util.Optional;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;

@Transactional
@Api("Notification template")
public class NotificationTemplateController extends Controller {

    @Inject
    INotificationTemplate iNotificationTemplate;

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
            )
    })
    public Result createNotificationTemplate() {

        final var notificationTemplateCreationForm = validate(request().body().asJson(), CreateNotificationTemplateDTO.class);
        if (notificationTemplateCreationForm.hasError) {
            return Results.badRequest(notificationTemplateCreationForm.error);
        }
        //TODO: Check if user is admin
        CreateNotificationTemplateDTO notificationTemplateDTO = notificationTemplateCreationForm.value;

        if (iNotificationTemplate.checkIfNotificationTemplateNameInUse(notificationTemplateDTO.getNotificationTemplateName())) {
            return Results.status(409, "Notification template name already in use");
        }

        try {
            return ok(myObjectMapper.toJsonString(iNotificationTemplate.createNotificationTemplate(
                    notificationTemplateDTO,
                    getTestEmployee()
            )));
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
                    value = "Notification template DTO",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.system.configuration.dto.EditNotificationTemplateDTO",
                    dataTypeClass = EditNotificationTemplateDTO.class
            )
    })
    public Result editNotificationTemplate() {

        final var notificationTemplateEditForm = validate(request().body().asJson(), EditNotificationTemplateDTO.class);
        if (notificationTemplateEditForm.hasError) {
            return Results.badRequest(notificationTemplateEditForm.error);
        }
        EditNotificationTemplateDTO notificationTemplateDTO = notificationTemplateEditForm.value;

        //TODO: Check if user is admin

        Optional<NotificationTemplate> nt = iNotificationTemplate.getNotificationTemplate(
                notificationTemplateDTO.getNotificationTemplateId()
        );

        if (nt.isPresent()) {
            String templateName = nt.get().getNotificationTemplateName();
            String templateId = nt.get(). getNotificationTemplateId();
            String dtoId = notificationTemplateDTO.getNotificationTemplateId();

            if (!dtoId.equals(templateId) && iNotificationTemplate.checkIfNotificationTemplateNameInUse(templateName)) {
                return Results.status(409, "Notification template name already in use");
            }
            return Results.ok(myObjectMapper.toJsonString(
                    iNotificationTemplate.editNotificationTemplate(
                            notificationTemplateDTO,
                            getTestEmployee()
                    ))
            );
        }
        return Results.notFound("Notification template not found");

    }

//    @ApiOperation("Delete Notification template by id")
//    @ApiResponses(
//            value = {
//                    @ApiResponse(code = 200, response = Boolean.class, message = "Notification template deleted"),
//                    @ApiResponse(code = 400, response = String.class, message = "Invalid notification template id"),
//            }
//    )
//    public Result deleteNotificationTemplate(@ApiParam(value = "Notification template Id", required = true) String notificationTemplateId) {
//
//        if (notificationTemplateId.length() == 0) {
//            return Results.badRequest("Invalid notification template id");
//        }
//        return ok(myObjectMapper.toJsonString(
//                iNotificationTemplate.deleteNotificationTemplate(notificationTemplateId)
//        ));
//    }

    private Employee getTestEmployee() {
        return new Employee("92f6fac6-f49b-448f-9c33-f0d50608bc83", "employee");
    }

    private Employee getDummyEmployee() {return  new Employee("f04b5314-9f26-43a0-b129-3e149165253e", "Name");}
}
