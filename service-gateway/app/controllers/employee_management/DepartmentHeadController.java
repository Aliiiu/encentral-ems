package controllers.employee_management;


import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.employee.management.api.IDepartmentHeadApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;


@Transactional
@Api("Employee Management - Head of Department")
public class DepartmentHeadController extends Controller {

    @Inject
    IDepartmentHeadApi iDepartmentHeadApi;

    @Inject
    MyObjectMapper objectMapper;

    @ApiOperation(value = "Set employee as Department Head")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful", response = boolean.class)
    })
    public Result setEmployeeAsDepartmentHead(String employeeId, String departmentId) {
        return ok(objectMapper.toJsonString(
            iDepartmentHeadApi.setEmployeeAsDepartmentHead(employeeId, departmentId, getEmployee())
        ));
    }

    @ApiOperation(value = "Remove employee as Department Head")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful", response = boolean.class)
    })
    public Result removeEmployeeAsDepartmentHead(String employeeId, String departmentId) {
        return ok(objectMapper.toJsonString(
                iDepartmentHeadApi.removeEmployeeAsDepartmentHead(employeeId, departmentId)
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
    public Result getDepartmentsHeadByEmployee(String employeeId) {
        return ok(objectMapper.toJsonString(
                iDepartmentHeadApi.getDepartmentsHeadByEmployee(employeeId)
        ));
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/23
     * @description To return the authenticated and authorized Employee
     *
     * @return Employee
     */
    Employee getEmployee() {
        return new Employee();
    }

}
