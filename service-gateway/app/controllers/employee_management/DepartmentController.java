package controllers.employee_management;


import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.authentication.model.RouteRole;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.employee.management.api.IDepartmentApi;
import com.esl.internship.staffsync.employee.management.dto.DepartmentDTO;
import com.esl.internship.staffsync.employee.management.dto.UpdateDepartmentDTO;
import com.esl.internship.staffsync.employee.management.model.Department;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.system.configuration.api.INotification;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import security.WebAuth;

import javax.inject.Inject;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;


@Transactional
@Api("Employee Management - Department")
public class DepartmentController extends Controller {

    @Inject
    IDepartmentApi iDepartmentApi;

    @Inject
    IAuthentication iAuthentication;

    @Inject
    INotification iNotification;

    @Inject
    MyObjectMapper myObjectMapper;

    @ApiOperation(value = "Create Department")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Department Created",
                    response = com.esl.internship.staffsync.employee.management.model.Department.class
            )
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Department data to create",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.employee.management.dto.DepartmentDTO"
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
    @WebAuth(permissions= {RoutePermissions.create_department }, roles = {RouteRole.admin})
    public Result addDepartment() {
        final var departmentDtoForm = validate(request().body().asJson(), DepartmentDTO.class);
        if (departmentDtoForm.hasError) {
            return badRequest(departmentDtoForm.error);
        }
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        Response<Department> serviceResponse = iDepartmentApi.addDepartment(departmentDtoForm.value, employee);

        boolean result = serviceResponse.getValue() != null;

        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee",employee.getFullName(), serviceResponse.getValue().getDepartmentName(), "document_deletion_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "document_deletion_failure");
        }
        if (serviceResponse.requestHasErrors())
            return badRequest(serviceResponse.getErrorsAsJsonString());

        return ok(myObjectMapper.toJsonString(serviceResponse.getValue()));
    }

    @ApiOperation(value = "Get Department by id")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Department",
                    response = com.esl.internship.staffsync.employee.management.model.Department.class
            )
    })
    public Result getDepartmentById(String departmentId) {
        return iDepartmentApi.getDepartmentById(departmentId)
                .map(e -> ok(myObjectMapper.toJsonString(e))).orElseGet(Results::notFound);
    }

    @ApiOperation(value = "Update Department")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Department updates", response = boolean.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Department data to update",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.employee.management.dto.UpdateDepartmentDTO"
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
    @WebAuth(permissions= {RoutePermissions.update_department }, roles = {RouteRole.admin})
    public Result updateDepartment(String departmentId) {
        final var departmentDtoForm = validate(request().body().asJson(), UpdateDepartmentDTO.class);
        if (departmentDtoForm.hasError) {
            return badRequest(departmentDtoForm.error);
        }
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean result = iDepartmentApi.updateDepartment(departmentId, departmentDtoForm.value, employee);

        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee",employee.getFullName(), "", "department_update_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "department_update_failure");
        }
        return ok(myObjectMapper.toJsonString(result));
    }

    @ApiOperation(value = "Get all Departments")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Departments",
                    response = com.esl.internship.staffsync.employee.management.model.Department.class,
                    responseContainer = "List"
            )
    })
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
    @WebAuth(permissions= {RoutePermissions.read_department }, roles = {RouteRole.admin, RouteRole.user})
    public Result getAllDepartments() {
        return ok(myObjectMapper.toJsonString(iDepartmentApi.getAllDepartments()));
    }

    @ApiOperation(value = "Delete Department")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Deleted", response = boolean.class)
    })
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
    @WebAuth(permissions= {RoutePermissions.delete_department }, roles = {RouteRole.admin})
    public Result deleteDepartment(String departmentId) {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean result = iDepartmentApi.deleteDepartment(departmentId);

        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee",employee.getFullName(), "", "department_deletion_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "department_deletion_failure");
        }
        return ok(myObjectMapper.toJsonString(result));
    }
}
