package controllers.system_configuration;


import com.encentral.scaffold.commons.model.Employee;
import com.encentral.scaffold.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.system.configuration.api.IPermissionApi;
import com.esl.internship.staffsync.system.configuration.dto.PermissionDTO;
import com.esl.internship.staffsync.system.configuration.model.Permission;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;

import static com.encentral.scaffold.commons.util.ImmutableValidator.validate;


@Api("Permission Management")
@Transactional
public class PermissionController extends Controller {

    @Inject
    IPermissionApi iPermissionApi;

    @Inject
    MyObjectMapper myObjectMapper;

    @ApiOperation(value = "Create a permission")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Permission Created", response = Permission.class)
            }
    )
    @ApiImplicitParams(
            {
            @ApiImplicitParam(
                    name = "body",
                    value = "Permission data to create",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.system.configuration.dto.PermissionDTO"
            )
    })
    public Result addPermission() {
        final var permissionDtoForm = validate(request().body().asJson(), PermissionDTO.class);
        if (permissionDtoForm.hasError) {
            return badRequest(permissionDtoForm.error);
        }
        return ok(myObjectMapper.toJsonString(iPermissionApi.addPermission(permissionDtoForm.value, getEmployee())));
    }

    @ApiOperation(value = "Get permission by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Permission", response = Permission.class)}
    )
    public Result getPermission(String permissionId) {
        return iPermissionApi.getPermissionById(permissionId)
                .map(e -> ok(myObjectMapper.toJsonString(e))).orElseGet(Results::notFound);
    }

    @ApiOperation(value = "Update a permission")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Permission Updated", response = boolean.class)
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Permission data to update",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.system.configuration.dto.PermissionDTO"
            )
    })
    public Result updatePermission(String permissionId) {
        final var permissionDtoForm = validate(request().body().asJson(), PermissionDTO.class);
        if (permissionDtoForm.hasError) {
            return badRequest(permissionDtoForm.error);
        }
        return ok(myObjectMapper.toJsonString(iPermissionApi.updatePermission(permissionId, permissionDtoForm.value, getEmployee())));
    }

    @ApiOperation(value = "Get permission")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "permissions", response = Permission.class, responseContainer = "List")
            }
    )
    public Result getAllPermissions() {
        return ok(myObjectMapper.toJsonString(iPermissionApi.getAllPermissions()));
    }

    @ApiOperation(value = "Delete permission")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Deleted", response = boolean.class)
            }
    )
    public Result deletePermission(String permissionId) {
        return ok(myObjectMapper.toJsonString(iPermissionApi.deletePermission(permissionId)));
    }

    /**
     * @author WARITH
     * @dateCreated 04/08/23
     * @description To return the authenticated and authorized Employee
     *
     * @return Employee
     */
    Employee getEmployee() {
        return new Employee("Test-001-EMP", "Test Employee");
    }

}
