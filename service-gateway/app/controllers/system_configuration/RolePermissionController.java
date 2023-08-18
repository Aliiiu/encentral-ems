package controllers.system_configuration;


import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.authentication.model.RouteRole;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.system.configuration.api.INotification;
import com.esl.internship.staffsync.system.configuration.api.IRoleHasPermissionApi;
import com.esl.internship.staffsync.system.configuration.model.Permission;
import com.esl.internship.staffsync.system.configuration.model.Role;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import security.WebAuth;

import javax.inject.Inject;


@Api("Role-Permission Management")
@Transactional
public class RolePermissionController extends Controller {

    @Inject
    IRoleHasPermissionApi iRoleHasPermissionApi;

    @Inject
    MyObjectMapper myObjectMapper;

    @Inject
    IAuthentication iAuthentication;

    @Inject
    INotification iNotification;


    @ApiOperation(value = "Grant permission to role")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Permission granted to Role", response = boolean.class)
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
    @WebAuth(permissions = {RoutePermissions.create_role_has_permission}, roles = {RouteRole.admin})
    public Result grantPermissionToRole(String roleId, String permissionId) {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean resp = iRoleHasPermissionApi.givePermissionToRole(roleId, permissionId, employee);
        if (resp) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), permissionId + "to role " + roleId, "Role_Permission_creation_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), permissionId + "to role " + roleId, "Role_Permission_creation_failure");
        }
        return ok(myObjectMapper.toJsonString(resp));
    }

    @ApiOperation(value = "Revoke permission from role")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Permission revoked from Role", response = boolean.class)
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
    @WebAuth(permissions = {RoutePermissions.delete_role_has_permission}, roles = {RouteRole.admin})
    public Result revokePermissionFromRole(String roleId, String permissionId) {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean resp = iRoleHasPermissionApi.removePermissionFromRole(roleId, permissionId);
        if (resp) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), permissionId + "from role " + roleId, "Role_Permission_deletion_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), permissionId + "from role " + roleId, "Role_Permission_deletion_failure");
        }
        return ok(myObjectMapper.toJsonString(resp));

    }

    @ApiOperation(value = "Get all permissions assigned to role")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200, message = "Permissions",
                            response = Permission.class,
                            responseContainer = "LIST"
                    )}
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
    @WebAuth(permissions = {RoutePermissions.read_role_has_permission}, roles = {RouteRole.admin, RouteRole.user})
    public Result getPermissionsForRole(String roleId) {
        return ok(myObjectMapper.toJsonString(
                iRoleHasPermissionApi.getPermissionForRole(roleId)
        ));
    }

    @ApiOperation(value = "Get all roles that has permission")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200, message = "Roles",
                            response = Role.class,
                            responseContainer = "LIST"
                    )}
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
    @WebAuth(permissions = {RoutePermissions.read_role_has_permission}, roles = {RouteRole.admin, RouteRole.user})
    public Result getRolesForPermission(String permissionId) {
        return ok(myObjectMapper.toJsonString(
                iRoleHasPermissionApi.getRolesForPermissionApi(permissionId)
        ));
    }

    @ApiOperation(value = "Get all Role-Permission map")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200, message = "Role-Permission",
                            response = RolePermissionController.class,
                            responseContainer = "LIST"
                    )}
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
    @WebAuth(permissions = {RoutePermissions.read_role_has_permission}, roles = {RouteRole.admin})
    public Result getAllRolePermission() {
        return ok(myObjectMapper.toJsonString(
                iRoleHasPermissionApi.getAllRoleHasPermission()
        ));
    }
}
