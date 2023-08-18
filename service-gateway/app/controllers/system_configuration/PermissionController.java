package controllers.system_configuration;


import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.authentication.model.RouteRole;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.system.configuration.api.INotification;
import com.esl.internship.staffsync.system.configuration.api.IPermissionApi;
import com.esl.internship.staffsync.system.configuration.dto.PermissionDTO;
import com.esl.internship.staffsync.system.configuration.model.Permission;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import security.WebAuth;

import javax.inject.Inject;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;


@Api("Permission Management")
@Transactional
public class PermissionController extends Controller {

    @Inject
    IPermissionApi iPermissionApi;

    @Inject
    MyObjectMapper myObjectMapper;

    @Inject
    INotification iNotification;

    @Inject
    IAuthentication iAuthentication;

    @ApiOperation(value = "Create a permission")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Permission Created", response = Permission.class)
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Permission data to create",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.system.configuration.dto.PermissionDTO"
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
    @WebAuth(permissions = {RoutePermissions.create_permission}, roles = {RouteRole.admin})
    public Result addPermission() {
        final var permissionDtoForm = validate(request().body().asJson(), PermissionDTO.class);
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        Permission permission = iPermissionApi.addPermission(permissionDtoForm.value, employee);
        if (permission != null) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), permission.getPermissionName(), "Permission_creation_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "Permission_creation_failure");
        }
        if (permissionDtoForm.hasError) {
            return badRequest(permissionDtoForm.error);
        }
        return ok(myObjectMapper.toJsonString(permission));
    }

    @ApiOperation(value = "Get permission by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Permission", response = Permission.class)}
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
    @WebAuth(permissions = {RoutePermissions.read_permission}, roles = {RouteRole.admin, RouteRole.user})
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
    @WebAuth(permissions = {RoutePermissions.update_permission}, roles = {RouteRole.admin})
    public Result updatePermission(String permissionId) {
        final var permissionDtoForm = validate(request().body().asJson(), PermissionDTO.class);
        if (permissionDtoForm.hasError) {
            return badRequest(permissionDtoForm.error);
        }
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean resp = iPermissionApi.updatePermission(permissionId, permissionDtoForm.value, employee);
        if (resp) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "Permission_update_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "Permission_update_failure");
        }
        return ok(myObjectMapper.toJsonString(resp));
    }

    @ApiOperation(value = "Get all permissions")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "permissions", response = Permission.class, responseContainer = "List")
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
    @WebAuth(permissions = {RoutePermissions.read_permission}, roles = {RouteRole.admin, RouteRole.user})
    public Result getAllPermissions() {
        return ok(myObjectMapper.toJsonString(iPermissionApi.getAllPermissions()));
    }

    @ApiOperation(value = "Delete permission")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Deleted", response = boolean.class)
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
    @WebAuth(permissions = {RoutePermissions.delete_permission}, roles = {RouteRole.admin})
    public Result deletePermission(String permissionId) {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean resp = iPermissionApi.deletePermission(permissionId);
        if (resp) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "Permission_deletion_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "Permission_deletion_failure");
        }
        return ok(myObjectMapper.toJsonString(resp));
    }


}
