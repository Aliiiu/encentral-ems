package controllers.employee_management;


import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.employee.management.api.IDepartmentApi;
import com.esl.internship.staffsync.employee.management.dto.DepartmentDTO;
import com.esl.internship.staffsync.employee.management.dto.UpdateDepartmentDTO;
import com.esl.internship.staffsync.employee.management.model.Department;
import com.esl.internship.staffsync.employee.management.service.response.Response;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;


@Transactional
@Api("Employee Management - Department")
public class DepartmentController extends Controller {

    @Inject
    IDepartmentApi iDepartmentApi;

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
            )
    })
    public Result addDepartment() {
        final var departmentDtoForm = validate(request().body().asJson(), DepartmentDTO.class);
        if (departmentDtoForm.hasError) {
            return badRequest(departmentDtoForm.error);
        }

        Response<Department> serviceResponse = iDepartmentApi.addDepartment(departmentDtoForm.value, getEmployee());

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
            )
    })
    public Result updateDepartment(String departmentId) {
        final var departmentDtoForm = validate(request().body().asJson(), UpdateDepartmentDTO.class);
        if (departmentDtoForm.hasError) {
            return badRequest(departmentDtoForm.error);
        }
        return ok(myObjectMapper.toJsonString(iDepartmentApi.updateDepartment(departmentId, departmentDtoForm.value, getEmployee())));
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
    public Result getAllDepartments() {
        return ok(myObjectMapper.toJsonString(iDepartmentApi.getAllDepartments()));
    }

    @ApiOperation(value = "Delete Department")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Deleted", response = boolean.class)
    })
    public Result deleteDepartment(String departmentId) {
        return ok(myObjectMapper.toJsonString(iDepartmentApi.deleteDepartment(departmentId)));
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/23
     * @description To return the authenticated and authorized Employee
     *
     * @return Employee
     */
    Employee getEmployee() {
        return new Employee("Test-001-EMP", "Test Employee");
    }

}
