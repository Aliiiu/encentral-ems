package controllers.employee_management;


import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.employee.management.api.IEmployeeApi;
import com.esl.internship.staffsync.employee.management.dto.EmployeeDTO;
import com.esl.internship.staffsync.employee.management.dto.EmployeeStatusUpdateDTO;
import com.esl.internship.staffsync.commons.service.response.Response;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;


@Transactional
@Api("Employee Management - Employee")
public class EmployeeController extends Controller {

    @Inject
    IEmployeeApi iEmployeeApi;

    @Inject
    MyObjectMapper objectMapper;

    @ApiOperation(value = "Create Employee")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Created", response = Employee.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Employee data to create",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.employee.management.dto.EmployeeDTO"
            )
    })
    public Result addEmployee() {
        final var employeeForm = validate(request().body().asJson(), EmployeeDTO.class);

        if (employeeForm.hasError) {
            return badRequest(employeeForm.error);
        }

        Response<Employee> serviceResponse = iEmployeeApi
                .addEmployee(employeeForm.value, getEmployee());

        if (serviceResponse.requestHasErrors())
            return badRequest(serviceResponse.getErrorsAsJsonString());
        return ok(objectMapper.toJsonString(serviceResponse.getValue()));

    }

    @ApiOperation(value = "Create Employee")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Created", response = Employee.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Employee data to create",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.employee.management.dto.CreateEmployeeDTO"
            )
    })
    public Result addEmployeeWithProfilePicture() {
        Http.MultipartFormData<File> formData = request().body().asMultipartFormData();
        if (formData == null) {
            System.out.println("Image Required");
            System.out.println(request().body().asJson().toString());
            return ok();
        }
        Http.MultipartFormData.FilePart<File> fp = formData.getFile("profilePicture");

        if (fp != null) {
            System.out.println("\n\nFile Upload succesfull");
            File pp = fp.getFile();

            String fileName = UUID.randomUUID().toString() + "_" + fp.getFilename();
            String uploadFilePath = "scripts/";
            Path destinationPath = Paths.get(uploadFilePath, fileName);

            try {
                Files.move(pp.toPath(), destinationPath);
                System.out.println("\nFile Written successfully");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("\nUnable to write file");
            }
        } else {
            System.out.println("File upload failed");
        }

        System.out.println(request().body().asJson().toString());


        return ok();
    }

    public Result setEmployeeProfilePicture(String employeeId) {
        Http.MultipartFormData<File> formData = request().body().asMultipartFormData();
        return ok();
    }

    @ApiOperation(value = "Get Employee by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee", response = Employee.class)
    })
    public Result getEmployeeById(@ApiParam(value = "Employee ID", required = true) String employeeId) {
        return ok(objectMapper.toJsonString(
                iEmployeeApi.getEmployeeById(employeeId)
        ));
    }

    @ApiOperation(value = "Get all Employees")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employees", response = Employee.class, responseContainer = "List")
    })
    public Result getAllEmployee() {
        return ok(objectMapper.toJsonString(
                iEmployeeApi.getAllEmployee()
        ));
    }

    @ApiOperation(value = "Delete Employee")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Deleted", response = boolean.class)
    })
    public Result deleteEmployee(String employeeId) {
        return ok(objectMapper.toJsonString(iEmployeeApi.deleteEmployee(employeeId)));
    }

    @ApiOperation(value = "Set the role of an Employee")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful", response = boolean.class)
    })
    public Result setEmployeeRole(String employeeId, String roleId) {
        return ok(objectMapper.toJsonString(
                iEmployeeApi.setEmployeeRole(employeeId, roleId, getEmployee())
        ));
    }

    @ApiOperation(value = "Mark an Employee active")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful", response = boolean.class)
    })
    public Result markEmployeeActive(String employeeId) {
        return ok(objectMapper.toJsonString(
                iEmployeeApi.markEmployeeActive(employeeId, getEmployee())
        ));
    }

    @ApiOperation(value = "Mark an Employee inactive")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful", response = boolean.class)
    })
    public Result markEmployeeInactive(String employeeId) {
        return ok(objectMapper.toJsonString(
                iEmployeeApi.markEmployeeActive(employeeId, getEmployee())
        ));
    }

    @ApiOperation(value = "Set Employee Status")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successful", response = boolean.class)
    })
    public Result setEmployeeStatus(String employeeId) {
        final var employeeStatusForm = validate(request().body().asJson(), EmployeeStatusUpdateDTO.class);

        if (employeeStatusForm.hasError) {
            return badRequest(employeeStatusForm.error);
        }

        return ok(objectMapper.toJsonString(
                iEmployeeApi.setEmployeeStatus(
                        employeeId,
                        employeeStatusForm.value.getCurrentStatus(),
                        getEmployee()
                )
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
