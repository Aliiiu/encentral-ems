package controllers.system_configuration;

import com.encentral.scaffold.commons.model.Employee;
import com.encentral.scaffold.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.system.configuration.api.IOption;
import com.esl.internship.staffsync.system.configuration.api.IOptionType;
import com.esl.internship.staffsync.system.configuration.model.Option;
import com.esl.internship.staffsync.system.configuration.model.OptionType;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;

import static com.encentral.scaffold.commons.util.ImmutableValidator.validate;


@Api("Options and Types")
@Transactional
public class OptionsAndTypesController extends Controller {

    @Inject
    IOptionType iOptionType;

    @Inject
    IOption iOption;

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
            )
    })
    public Result addOption() {
        final var optionForm = validate(request().body().asJson(), Option.class);
        if (optionForm.hasError) {
            return badRequest(optionForm.error);
        }
        return ok(myObjectMapper.toJsonString(iOption.addOption(optionForm.value, getTestEmployee())));
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
            )
    })
    public Result editOptionType(String optionTypeId) {
        final var optionTypeForm = validate(request().body().asJson(), OptionType.class);
        if (optionTypeForm.hasError) {
            return badRequest(optionTypeForm.error);
        }
        return ok(myObjectMapper.toJsonString(iOptionType.editOptionType(optionTypeId, optionTypeForm.value, getTestEmployee())));
    }

    @ApiOperation(value = "Update option value")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Updated", response = boolean.class)}
    )
    public Result editOption(String optionId, String value) {
        return ok(myObjectMapper.toJsonString(iOption.editOption(optionId, value, getTestEmployee())));
    }

    @ApiOperation(value = "Get option type by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OptionType", response = OptionType.class)}
    )
    public Result getOptionType(String optionTypeId) {
        return iOptionType.getOptionType(optionTypeId)
                .map(e -> ok(myObjectMapper.toJsonString(e))).orElseGet(Results::notFound);
    }

    @ApiOperation(value = "Get option by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Option", response = Option.class)}
    )
    public Result getOption(String optionId) {
        return iOption.getOption(optionId)
                .map(e -> ok(myObjectMapper.toJsonString(e))).orElseGet(Results::notFound);
    }

    @ApiOperation(value = "Get all option types")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OptionType", response = OptionType.class, responseContainer = "List")}
    )
    public Result getOptionTypes() {
        return ok(myObjectMapper.toJsonString(iOptionType.getOptionTypes()));
    }

    @ApiOperation(value = "Get all options")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Option", response = Option.class, responseContainer = "List")}
    )
    public Result getOptions() {
        return ok(myObjectMapper.toJsonString(iOption.getOptions()));
    }


    @ApiOperation(value = "Get options for an option type")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OptionType", response = Option.class, responseContainer = "List")}
    )
    public Result getOptionsForType(String optionTypeId) {
        return ok(myObjectMapper.toJsonString(iOptionType.getOptionsForType(optionTypeId)));
    }

    private Employee getTestEmployee() {
        return new Employee("12345", "employee");
    }

}
