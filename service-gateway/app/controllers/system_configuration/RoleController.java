package controllers.system_configuration;

import com.encentral.scaffold.commons.model.Employee;
import com.encentral.scaffold.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.system.configuration.api.IRoleApi;
import com.esl.internship.staffsync.system.configuration.model.Role;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;

import static com.encentral.scaffold.commons.util.ImmutableValidator.validate;

@Api("Role Management")
@Transactional
public class RoleController extends Controller {
    @Inject
    IRoleApi iRoleApi;

    @Inject
    MyObjectMapper myObjectMapper;

    @ApiOperation(value = "Create a role")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Role", response = Role.class)
            }
    )
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(
                            name = "body",
                            value = "Invoice",
                            paramType = "body",
                            required = true,
                            dataType = "com.esl.internship.staffsync.system.configuration.model.Role"
                    )
            })
    public Result addRole() {
        final var roleForm = validate(request().body().asJson(), Role.class);
        if (roleForm.hasError) {
            return badRequest(roleForm.error);
        }
        return ok(myObjectMapper.toJsonString(iRoleApi.addRole(roleForm.value, getEmployee())));
    }

    @ApiOperation(value = "Get role by id")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Role", response = Role.class)}
    )
    public Result getRole(String roleId) {
        return iRoleApi.getRole(roleId)
                .map(e -> ok(myObjectMapper.toJsonString(e))).orElseGet(Results::notFound);
    }

    @ApiOperation(value = "Update a role")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Updated", response = boolean.class)
            }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Invoice",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.system.configuration.model.Role"
            )
    })
    public Result updateRole(String roleId) {
        final var roleForm = validate(request().body().asJson(), Role.class);
        if (roleForm.hasError) {
            return badRequest(roleForm.error);
        }
        return ok(myObjectMapper.toJsonString(iRoleApi.updateRole(roleId, roleForm.value, getEmployee())));
    }

    @ApiOperation(value = "Get all roles")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "roles", response = Role.class, responseContainer = "List")
            }
    )
    public Result getAllRole() {
        return ok(myObjectMapper.toJsonString(iRoleApi.getRoles()));
    }

    @ApiOperation(value = "Delete role")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Deleted", response = boolean.class)
            }
    )
    public Result deleteRole(String roleId) {
        return ok(myObjectMapper.toJsonString(iRoleApi.deleteRole(roleId)));
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
