package controllers.system_configuration;

import com.encentral.scaffold.commons.model.Employee;
import com.encentral.scaffold.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.system.configuration.api.IAppConfigApi;
import com.esl.internship.staffsync.system.configuration.model.AppConfig;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import javax.inject.Inject;

import static com.encentral.scaffold.commons.util.ImmutableValidator.validate;

@Api("Application Controller")
@Transactional
public class AppConfigController extends Controller {

    @Inject
    IAppConfigApi iAppConfigApi;

    @Inject
    MyObjectMapper myObjectMapper;

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
            )
    })
    public Result addAppConfig() {
        final var appConfigForm = validate(request().body().asJson(), AppConfig.class);
        if (appConfigForm.hasError) {
            return badRequest(appConfigForm.error);
        }
        return ok(myObjectMapper.toJsonString(iAppConfigApi.addAppConfig(appConfigForm.value, getTestEmployee())));
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
            )
    })
    public Result updateAppConfig(String appConfigId) {
        final var appConfigForm = validate(request().body().asJson(), AppConfig.class);
        if (appConfigForm.hasError) {
            return badRequest(appConfigForm.error);
        }
        return ok(myObjectMapper.toJsonString(iAppConfigApi.updateAppConfig(appConfigId, appConfigForm.value, getTestEmployee())));
    }

    @ApiOperation(value = "Get app config by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "AppConfig", response = AppConfig.class)}
    )
    public Result getAppConfig(String appConfigId) {
        return iAppConfigApi.getAppConfig(appConfigId)
                .map(e -> ok(myObjectMapper.toJsonString(e))).orElseGet(Results::notFound);
    }

    @ApiOperation(value = "Get all app config")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Options", response = AppConfig.class, responseContainer = "List")}
    )
    public Result getAppConfigs() {
        return ok(myObjectMapper.toJsonString(iAppConfigApi.getAppConfigs()));
    }

    @ApiOperation(value = "Delete app config")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Deleted", response = boolean.class)}
    )
    public Result deleteAppConfig(String appConfigId) {
        return ok(myObjectMapper.toJsonString(iAppConfigApi.deleteAppConfig(appConfigId)));
    }

    private Employee getTestEmployee() {
        return new Employee("12345", "employee");
    }
}
