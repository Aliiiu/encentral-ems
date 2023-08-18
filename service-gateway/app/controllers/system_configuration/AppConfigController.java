package controllers.system_configuration;

import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.authentication.model.RouteRole;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.system.configuration.api.IAppConfigApi;
import com.esl.internship.staffsync.system.configuration.api.INotification;
import com.esl.internship.staffsync.system.configuration.model.AppConfig;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import security.WebAuth;

import javax.inject.Inject;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;

@Api("Application Configuration")
@Transactional
public class AppConfigController extends Controller {

    @Inject
    IAppConfigApi iAppConfigApi;

    @Inject
    MyObjectMapper myObjectMapper;

    @Inject
    INotification iNotification;

    @Inject
    IAuthentication iAuthentication;

    @ApiOperation(value = "Create app config")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "AppConfig", response = AppConfig.class)}
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Invoice",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.system.configuration.model.AppConfig"
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
    @WebAuth(permissions = {RoutePermissions.create_app_config}, roles = {RouteRole.admin})
    public Result addAppConfig() {
        final var appConfigForm = validate(request().body().asJson(), AppConfig.class);
        if (appConfigForm.hasError) {
            return badRequest(appConfigForm.error);
        }
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        AppConfig appConfig = iAppConfigApi.addAppConfig(appConfigForm.value, employee);
        if (appConfig != null) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), appConfig.getConfigurationKey(), "app_config_creation_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "app_config_creation_failure");
        }
        return ok(myObjectMapper.toJsonString(appConfig));
    }

    @ApiOperation(value = "Update app config")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Updated", response = boolean.class)}
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Invoice",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.system.configuration.model.AppConfig"
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
    @WebAuth(permissions = {RoutePermissions.update_app_config}, roles = {RouteRole.admin})
    public Result updateAppConfig(String appConfigId) {
        final var appConfigForm = validate(request().body().asJson(), AppConfig.class);
        if (appConfigForm.hasError) {
            return badRequest(appConfigForm.error);
        }
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean resp = iAppConfigApi.updateAppConfig(appConfigId, appConfigForm.value, employee);
        if (resp) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "app_config_update_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "app_config_update_failure");
        }
        return ok(myObjectMapper.toJsonString(resp));
    }

    @ApiOperation(value = "Get app config by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "AppConfig", response = AppConfig.class)}
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
    @WebAuth(permissions = {RoutePermissions.read_app_config}, roles = {RouteRole.admin, RouteRole.user})
    public Result getAppConfig(String appConfigId) {
        return iAppConfigApi.getAppConfig(appConfigId)
                .map(e -> ok(myObjectMapper.toJsonString(e))).orElseGet(Results::notFound);
    }

    @ApiOperation(value = "Get all app config")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Options", response = AppConfig.class, responseContainer = "List")}
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
    @WebAuth(permissions = {RoutePermissions.read_app_config}, roles = {RouteRole.admin, RouteRole.user})
    public Result getAppConfigs() {
        return ok(myObjectMapper.toJsonString(iAppConfigApi.getAppConfigs()));
    }

    @ApiOperation(value = "Delete app config")
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
    @WebAuth(permissions = {RoutePermissions.delete_app_config}, roles = {RouteRole.admin})
    public Result deleteAppConfig(String appConfigId) {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean resp = iAppConfigApi.deleteAppConfig(appConfigId);
        if (resp) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "app_config_deletion_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "app_config_deletion_failure");
        }
        return ok(myObjectMapper.toJsonString(resp));
    }
}
