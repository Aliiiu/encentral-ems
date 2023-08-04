package controllers.system_configuration;

import com.encentral.scaffold.commons.model.Employee;
import com.encentral.scaffold.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.system.configuration.api.IRoleHasPermissionApi;
import com.esl.internship.staffsync.system.configuration.model.Permission;
import com.esl.internship.staffsync.system.configuration.model.Role;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

@Api("Role-Permission Management")
@Transactional
public class RolePermissionController extends Controller {
    @Inject
    IRoleHasPermissionApi iRoleHasPermissionApi;

    @Inject
    MyObjectMapper myObjectMapper;

    @ApiOperation(value = "Grant permission to role")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Deleted", response = boolean.class)
            }
    )
    public Result grantPermissionToRole(String roleId, String permissionId) {
        return ok(myObjectMapper.toJsonString(
                iRoleHasPermissionApi.givePermissionToRole(roleId, permissionId, getEmployee())
                ));
    }

    @ApiOperation(value = "Revoke permission from role")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Revoked", response = boolean.class)
            }
    )
    public Result revokePermissionFromRole(String roleId, String permissionId) {
        return ok(myObjectMapper.toJsonString(
                iRoleHasPermissionApi.removePermissionFromRole(roleId, permissionId)
        ));
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
    public Result getAllRolePermission() {
        return ok(myObjectMapper.toJsonString(
                iRoleHasPermissionApi.getAllRoleHasPermission()
        ));
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
