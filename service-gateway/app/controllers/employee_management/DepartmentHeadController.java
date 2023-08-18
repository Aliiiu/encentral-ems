package controllers.employee_management;


import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.authentication.model.RouteRole;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.employee.management.api.IDepartmentHeadApi;
import com.esl.internship.staffsync.system.configuration.api.INotification;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import security.WebAuth;

import javax.inject.Inject;


@Transactional
@Api("Employee Management - Head of Department")
public class DepartmentHeadController extends Controller {

    @Inject
    IDepartmentHeadApi iDepartmentHeadApi;

    @Inject
    MyObjectMapper objectMapper;

    @Inject
    INotification iNotification;

    @Inject
    IAuthentication iAuthentication;

    @ApiOperation(value = "Set employee as Department Head")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful", response = boolean.class)
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
    @WebAuth(permissions= {RoutePermissions.update_department_head }, roles = {RouteRole.admin})
    public Result setEmployeeAsDepartmentHead(String employeeId, String departmentId) {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean result = iDepartmentHeadApi.setEmployeeAsDepartmentHead(employeeId, departmentId, employee);

        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee",employee.getFullName(), "", "department_head_creation_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "department_head_creation_failure");
        }
        return ok(objectMapper.toJsonString(
            result
        ));
    }

    @ApiOperation(value = "Remove employee as Department Head")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful", response = boolean.class)
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
    @WebAuth(permissions= {RoutePermissions.update_department_head }, roles = {RouteRole.admin})
    public Result removeEmployeeAsDepartmentHead(String employeeId, String departmentId) {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean result =  iDepartmentHeadApi.removeEmployeeAsDepartmentHead(employeeId, departmentId);
        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee",employee.getFullName(), "", "department_head_update_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "department_head_update_failure");
        }
        return ok(objectMapper.toJsonString(
                result
        ));
    }

    @ApiOperation(value = "Get all Department Heads")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "DepartmentHeads",
                    response = com.esl.internship.staffsync.employee.management.model.DepartmentHead.class,
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
    @WebAuth(permissions= {RoutePermissions.read_department_head }, roles = {RouteRole.admin, RouteRole.user})
    public Result getAllDepartmentHeads() {
        return ok(objectMapper.toJsonString(
                iDepartmentHeadApi.getAllDepartmentHeads()
        ));
    }

    @ApiOperation(value = "Get Department Head by Id")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "DepartmentHead",
                    response = com.esl.internship.staffsync.employee.management.model.DepartmentHead.class
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
    @WebAuth(permissions= {RoutePermissions.read_department_head }, roles = {RouteRole.admin, RouteRole.user})
    public Result getDepartmentHeadById(String departmentHeadId) {
        return ok(objectMapper.toJsonString(
                iDepartmentHeadApi.getDepartmentHeadById(departmentHeadId)
        ));
    }

    @ApiOperation(value = "Get the department head by department ID")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Employee",
                    response = Employee.class
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
    @WebAuth(permissions= {RoutePermissions.read_department_head }, roles = {RouteRole.admin, RouteRole.user})
    public Result getDepartmentHeadByDepartmentId(String departmentId) {
        return ok(objectMapper.toJsonString(
                iDepartmentHeadApi.getDepartmentHeadByDepartmentId(departmentId)
        ));
    }

    @ApiOperation(value = "Get all departments headed by employee")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Department",
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
    @WebAuth(permissions= {RoutePermissions.read_department_head }, roles = {RouteRole.admin, RouteRole.user})
    public Result getDepartmentsHeadByEmployee(String employeeId) {
        return ok(objectMapper.toJsonString(
                iDepartmentHeadApi.getDepartmentsHeadByEmployee(employeeId)
        ));
    }
}
