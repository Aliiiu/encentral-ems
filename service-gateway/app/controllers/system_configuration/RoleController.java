package controllers.system_configuration;


import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.authentication.model.RouteRole;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.system.configuration.api.INotification;
import com.esl.internship.staffsync.system.configuration.api.IRoleApi;
import com.esl.internship.staffsync.system.configuration.dto.RoleDTO;
import com.esl.internship.staffsync.system.configuration.model.Permission;
import com.esl.internship.staffsync.system.configuration.model.Role;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import security.WebAuth;

import javax.inject.Inject;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;


@Api("Role Management")
@Transactional
public class RoleController extends Controller {

    @Inject
    IRoleApi iRoleApi;

    @Inject
    MyObjectMapper myObjectMapper;

    @Inject
    IAuthentication iAuthentication;

    @Inject
    INotification iNotification;

    @ApiOperation(value = "Create a role")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Role Created", response = Role.class)
            }
    )
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(
                            name = "body",
                            value = "Role data",
                            paramType = "body",
                            required = true,
                            dataType = "com.esl.internship.staffsync.system.configuration.dto.RoleDTO"
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
    @WebAuth(permissions = {RoutePermissions.create_role}, roles = {RouteRole.admin})
    public Result addRole() {
        final var roleDtoForm = validate(request().body().asJson(), RoleDTO.class);
        if (roleDtoForm.hasError) {
            return badRequest(roleDtoForm.error);
        }
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        Role role = iRoleApi.addRole(roleDtoForm.value, employee);
        if (role != null) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), role.getRoleName(), "Role_creation_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "Role_creation_failure");
        }
        return ok(myObjectMapper.toJsonString(role));
    }

    @ApiOperation(value = "Get role by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Role", response = Role.class)}
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
    @WebAuth(permissions = {RoutePermissions.read_role}, roles = {RouteRole.admin, RouteRole.user})
    public Result getRole(String roleId) {
        return iRoleApi.getRole(roleId)
                .map(e -> ok(myObjectMapper.toJsonString(e))).orElseGet(Results::notFound);
    }

    @ApiOperation(value = "Update a role")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Role Updated", response = boolean.class)
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Role Data to update",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.system.configuration.dto.RoleDTO"
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
    @WebAuth(permissions = {RoutePermissions.update_role}, roles = {RouteRole.admin})
    public Result updateRole(String roleId) {
        final var roleDtoForm = validate(request().body().asJson(), RoleDTO.class);
        if (roleDtoForm.hasError) {
            return badRequest(roleDtoForm.error);
        }
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean resp = iRoleApi.updateRole(roleId, roleDtoForm.value, employee);
        if (resp) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "Role_update_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "Role_update_failure");
        }
        return ok(myObjectMapper.toJsonString(resp));
    }

    @ApiOperation(value = "Get all roles")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "roles", response = Role.class, responseContainer = "List")
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
    @WebAuth(permissions = {RoutePermissions.read_role}, roles = {RouteRole.admin})
    public Result getAllRole() {
        return ok(myObjectMapper.toJsonString(iRoleApi.getRoles()));
    }

    @ApiOperation(value = "Delete role")
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
    @WebAuth(permissions = {RoutePermissions.delete_role}, roles = {RouteRole.admin})
    public Result deleteRole(String roleId) {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean resp = iRoleApi.deleteRole(roleId);
        if (resp) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "Role_deletion_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "Role_deletion_failure");
        }
        return ok(myObjectMapper.toJsonString(resp));
    }


}
