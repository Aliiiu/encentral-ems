package controllers.employee_management;


import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.authentication.model.RouteRole;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.employee.management.api.IEmployeeApi;
import com.esl.internship.staffsync.employee.management.api.IEmployeeDocumentUploadApi;
import com.esl.internship.staffsync.employee.management.dto.EmployeeDTO;
import com.esl.internship.staffsync.employee.management.dto.EmployeeStatusUpdateDTO;
import com.esl.internship.staffsync.system.configuration.api.INotification;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import security.WebAuth;

import javax.inject.Inject;
import java.io.File;
import java.util.Optional;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;


@Transactional
@Api("Employee Management - Employee")
public class EmployeeController extends Controller {

    @Inject
    IEmployeeApi iEmployeeApi;

    @Inject
    IEmployeeDocumentUploadApi iEmployeeDocumentUploadApi;

    @Inject
    INotification iNotification;

    @Inject
    IAuthentication iAuthentication;

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
            ),
//            @ApiImplicitParam(
//                    name = "Authorization",
//                    value = "Authorization",
//                    paramType = "header",
//                    required = true,
//                    dataType = "string",
//                    dataTypeClass = String.class
//            )
    })
//    @WebAuth(permissions = {RoutePermissions.create_employee}, roles = {RouteRole.admin})
    public Result addEmployee() {
        final var employeeForm = validate(request().body().asJson(), EmployeeDTO.class);

        if (employeeForm.hasError) {
            return badRequest(employeeForm.error);
        }

//        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        Employee employee = new Employee();
        Response<Employee> serviceResponse = iEmployeeApi
                .addEmployee(employeeForm.value, employee);

        boolean result = serviceResponse.getValue() != null;

//        if (result) {
//            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), serviceResponse.getValue().getFullName(), "employee_creation_successful");
//        } else {
//            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "employee_creation_failure");
//        }
        if (serviceResponse.requestHasErrors())
            return badRequest(serviceResponse.getErrorsAsJsonString());
        return ok(objectMapper.toJsonString(serviceResponse.getValue()));

    }

    @ApiOperation(value = "Create Employee with profile picture")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee Created", response = Employee.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "employeeData",
                    value = "Employee data to create",
                    paramType = "formData",
                    required = true,
                    dataType = "com.esl.internship.staffsync.employee.management.dto.EmployeeDTO"
            ),
            @ApiImplicitParam(
                    name = "profilePicture",
                    value = "File to upload",
                    required = true,
                    dataType = "java.io.File",
                    paramType = "formData"
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
    @WebAuth(permissions = {RoutePermissions.create_employee}, roles = {RouteRole.admin})
    public Result addEmployeeWithProfilePicture() {
        Http.MultipartFormData<File> formData = request().body().asMultipartFormData();


        String employeeData = formData.asFormUrlEncoded().get("employeeData")[0];

        final var employeeForm = validate(Json.parse(employeeData), EmployeeDTO.class);

        if (employeeForm.hasError) {
            return badRequest(employeeForm.error);
        }

        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();

        Response<Employee> serviceResponse = iEmployeeApi
                .addEmployee(employeeForm.value, employee);

        if (serviceResponse.requestHasErrors())
            return badRequest(serviceResponse.getErrorsAsJsonString());
        Employee newEmployee = serviceResponse.getValue();

        boolean ppUploadSuccess = false;
        if (formData != null) {
            Http.MultipartFormData.FilePart<File> filePart = formData.getFile("profilePicture");
            if (filePart == null)
                return badRequest("'profilePicture' key not found.");

            ppUploadSuccess = iEmployeeApi.setEmployeeProfilePicture(
                    newEmployee.getEmployeeId(), filePart.getFile(), filePart.getFilename(), employee
            );
        }

        String employeeJson = objectMapper.toJsonString(newEmployee);
        if (newEmployee != null) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), newEmployee.getFullName(), "employee_creation_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "employee_creation_failure");
        }

        return ok(
                Json.newObject()
                        .put("employee", employeeJson)
                        .put("profilePictureUploadSuccess", ppUploadSuccess)
                        .toString()
        );
    }

    @ApiOperation(
            value = "Upload Employee Profile Picture",
            httpMethod = "POST"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "profilePicture",
                    value = "File to upload",
                    required = true,
                    dataType = "java.io.File",
                    paramType = "formData"
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
    @WebAuth(permissions= {RoutePermissions.update_employee }, roles = {RouteRole.admin, RouteRole.user})
    public Result setEmployeeProfilePicture(String employeeId) {
        Http.MultipartFormData<File> formData = request().body().asMultipartFormData();

        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        if (formData != null) {
            Http.MultipartFormData.FilePart<File> filePart = formData.getFile("profilePicture");
            if (filePart == null)
                return badRequest("'profilePicture' key not found.");

            boolean result = iEmployeeApi.setEmployeeProfilePicture(
                    employeeId, filePart.getFile(), filePart.getFilename(), employee
            );
            if (result) {
                iNotification.sendNotification(employee.getEmployeeId(), "system_employee",employee.getFullName(), "", "employee_update_successful");
            } else {
                iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "employee_update_failure");
            }
            return ok(objectMapper.toJsonString(
                    result
            ));
        }
        return badRequest("Image Required");
    }

    @ApiOperation(
            value = "Get Employee Profile Picture",
            httpMethod = "GET",
            produces = "*/*"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Profile Picture"),
            @ApiResponse(code = 404, message = "Image not found")
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
    @WebAuth(permissions= {RoutePermissions.read_employee }, roles = {RouteRole.admin, RouteRole.user})
    public Result getEmployeeProfilePicture(String employeeId) {

        Optional<File> res = iEmployeeApi.getEmployeeProfilePicture(employeeId);

        if (res.isPresent()) {
            File file = res.get();
            return ok(file);
        }
        return notFound("Employee does not exist or profile picture not set");
    }

    @ApiOperation(value = "Get Employee by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employee", response = Employee.class)
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
    @WebAuth(permissions= {RoutePermissions.read_employee }, roles = {RouteRole.admin, RouteRole.user})
    public Result getEmployeeById(@ApiParam(value = "Employee ID", required = true) String employeeId) {
        return iEmployeeApi.getEmployeeById(employeeId)
                .map(e -> ok(objectMapper.toJsonString(e))).orElseGet(Results::notFound);
    }

    @ApiOperation(value = "Get all Employees")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Employees", response = Employee.class, responseContainer = "List")
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
    @WebAuth(permissions= {RoutePermissions.read_employee }, roles = {RouteRole.admin, RouteRole.user})
    public Result getAllEmployee() {
        return ok(objectMapper.toJsonString(
                iEmployeeApi.getAllEmployee()
        ));
    }

    @ApiOperation(value = "Delete Employee")
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
    @WebAuth(permissions= {RoutePermissions.delete_employee }, roles = {RouteRole.admin})
    public Result deleteEmployee(String employeeId) {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean result = iEmployeeApi.deleteEmployee(employeeId);
        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee",employee.getFullName(), "", "employee_deletion_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "employee_deletion_failure");
        }

        return ok(objectMapper.toJsonString(result));
    }

    @ApiOperation(value = "Set the role of an Employee")
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
    @WebAuth(permissions= {RoutePermissions.update_employee }, roles = {RouteRole.admin})
    public Result setEmployeeRole(String employeeId, String roleId) {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean result =  iEmployeeApi.setEmployeeRole(employeeId, roleId, employee);
        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee",employee.getFullName(), "", "employee_update_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "employee_update_failure");
        }
        return ok(objectMapper.toJsonString(result));
    }

    @ApiOperation(value = "Mark an Employee active")
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
    @WebAuth(permissions= {RoutePermissions.update_employee }, roles = {RouteRole.admin})
    public Result markEmployeeActive(String employeeId) {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean result = iEmployeeApi.markEmployeeActive(employeeId, employee);
        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee",employee.getFullName(), "", "employee_update_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "employee_update_failure");
        }
        return ok(objectMapper.toJsonString(result));
    }

    @ApiOperation(value = "Mark an Employee inactive")
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
    @WebAuth(permissions= {RoutePermissions.update_employee }, roles = {RouteRole.admin})
    public Result markEmployeeInactive(String employeeId) {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean result = iEmployeeApi.markEmployeeActive(employeeId, employee);
        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee",employee.getFullName(), "", "employee_update_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "employee_update_failure");
        }
        return ok(objectMapper.toJsonString(result));
    }

    @ApiOperation(value = "Set Employee Status")
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
    @WebAuth(permissions= {RoutePermissions.update_employee }, roles = {RouteRole.admin})
    public Result setEmployeeStatus(String employeeId) {
        final var employeeStatusForm = validate(request().body().asJson(), EmployeeStatusUpdateDTO.class);

        if (employeeStatusForm.hasError) {
            return badRequest(employeeStatusForm.error);
        }

        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean result = iEmployeeApi.setEmployeeStatus(
                employeeId,
                employeeStatusForm.value.getCurrentStatus(),
                employee
        );
        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee",employee.getFullName(), "", "employee_update_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "employee_update_failure");
        }
        return ok(objectMapper.toJsonString(result));

    }

}
