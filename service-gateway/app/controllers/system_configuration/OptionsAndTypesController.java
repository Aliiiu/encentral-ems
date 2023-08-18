package controllers.system_configuration;

import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.authentication.model.RouteRole;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.system.configuration.api.INotification;
import com.esl.internship.staffsync.system.configuration.api.IOption;
import com.esl.internship.staffsync.system.configuration.api.IOptionType;
import com.esl.internship.staffsync.system.configuration.model.Option;
import com.esl.internship.staffsync.system.configuration.model.OptionType;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import security.WebAuth;

import javax.inject.Inject;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;


@Api("Options and Types")
@Transactional
public class OptionsAndTypesController extends Controller {

    @Inject
    IOptionType iOptionType;

    @Inject
    IOption iOption;

    @Inject
    INotification iNotification;

    @Inject
    IAuthentication iAuthentication;

    @Inject
    MyObjectMapper myObjectMapper;

    @ApiOperation(value = "Add option")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Option", response = Option.class)}
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
    @WebAuth(permissions = {RoutePermissions.create_option}, roles = {RouteRole.admin})
    public Result addOption() {
        final var optionForm = validate(request().body().asJson(), Option.class);
        if (optionForm.hasError) {
            return badRequest(optionForm.error);
        }
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        Option option = iOption.addOption(optionForm.value, employee);
        if (option != null) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), option.getOptionValue(), "Option_creation_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "Option_creation_failure");
        }
        return ok(myObjectMapper.toJsonString(option));
    }

    @ApiOperation(value = "Update option type")
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
    @WebAuth(permissions = {RoutePermissions.update_option_type}, roles = {RouteRole.admin})
    public Result editOptionType(String optionTypeId) {
        final var optionTypeForm = validate(request().body().asJson(), OptionType.class);
        if (optionTypeForm.hasError) {
            return badRequest(optionTypeForm.error);
        }
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean resp = iOptionType.editOptionType(optionTypeId, optionTypeForm.value, employee);
        if (resp) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "Option_type_update_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "Option_type_update_failure");
        }
        return ok(myObjectMapper.toJsonString(resp));
    }

    @ApiOperation(value = "Update option value")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Updated", response = boolean.class)}
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
    @WebAuth(permissions = {RoutePermissions.update_option}, roles = {RouteRole.admin})
    public Result editOption(String optionId, String value) {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean resp = iOption.editOption(optionId, value, employee);
        if (resp) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "Option_update_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "Option_update_failure");
        }
        return ok(myObjectMapper.toJsonString(resp));
    }

    @ApiOperation(value = "Get option type by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OptionType", response = OptionType.class)}
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
    @WebAuth(permissions = {RoutePermissions.read_option_type}, roles = {RouteRole.admin, RouteRole.user})
    public Result getOptionType(String optionTypeId) {
        return iOptionType.getOptionType(optionTypeId)
                .map(e -> ok(myObjectMapper.toJsonString(e))).orElseGet(Results::notFound);
    }

    @ApiOperation(value = "Get option by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Option", response = Option.class)}
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
    @WebAuth(permissions = {RoutePermissions.read_option}, roles = {RouteRole.admin, RouteRole.user})
    public Result getOption(String optionId) {
        return iOption.getOption(optionId)
                .map(e -> ok(myObjectMapper.toJsonString(e))).orElseGet(Results::notFound);
    }

    @ApiOperation(value = "Get all option types")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OptionType", response = OptionType.class, responseContainer = "List")}
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
    @WebAuth(permissions = {RoutePermissions.read_option_type}, roles = {RouteRole.admin, RouteRole.user})
    public Result getOptionTypes() {
        return ok(myObjectMapper.toJsonString(iOptionType.getOptionTypes()));
    }

    @ApiOperation(value = "Get all options")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Option", response = Option.class, responseContainer = "List")}
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
    @WebAuth(permissions = {RoutePermissions.read_option}, roles = {RouteRole.admin, RouteRole.user})
    public Result getOptions() {
        return ok(myObjectMapper.toJsonString(iOption.getOptions()));
    }


    @ApiOperation(value = "Get options for an option type")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OptionType", response = Option.class, responseContainer = "List")}
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
    @WebAuth(permissions = {RoutePermissions.read_option_type, RoutePermissions.read_option}, roles = {RouteRole.admin, RouteRole.user})
    public Result getOptionsForType(String optionTypeId) {
        return ok(myObjectMapper.toJsonString(iOptionType.getOptionsForType(optionTypeId)));
    }

}
