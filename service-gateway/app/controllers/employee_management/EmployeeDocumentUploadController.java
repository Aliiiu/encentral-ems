package controllers.employee_management;

import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.authentication.model.RouteRole;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.document.management.dto.SaveInfo;
import com.esl.internship.staffsync.employee.management.api.IEmployeeDocumentUploadApi;
import com.esl.internship.staffsync.employee.management.dto.EmployeeDocumentDTO;
import com.esl.internship.staffsync.employee.management.model.EmployeeDocument;
import com.esl.internship.staffsync.system.configuration.api.INotification;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import security.WebAuth;

import javax.inject.Inject;
import java.io.File;
import java.util.Optional;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;

@Api("Employee Management - Document Upload")
@Transactional
public class EmployeeDocumentUploadController extends Controller {

    @Inject
    IEmployeeDocumentUploadApi iEmployeeDocumentUploadApi;

    @Inject
    MyObjectMapper objectMapper;

    @Inject
    INotification iNotification;

    @Inject
    IAuthentication iAuthentication;

    @ApiOperation(value = "Upload a Document")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Document Uploaded", response = EmployeeDocument.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "documentInfo",
                    value = "Document Information (as Json String)",
                    paramType = "formData",
                    required = true,
                    dataType = "com.esl.internship.staffsync.employee.management.dto.EmployeeDocumentDTO"
            ),
            @ApiImplicitParam(
                    name = "file",
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
    @WebAuth(permissions= {RoutePermissions.create_document }, roles = {RouteRole.admin, RouteRole.user})
    public Result uploadDocument(String employeeId) {
        Http.MultipartFormData<File> formData = request().body().asMultipartFormData();

        String documentInfo = formData.asFormUrlEncoded().get("documentInfo")[0];
        Http.MultipartFormData.FilePart<File> filePart = formData.getFile("file");

        final var documentForm = validate(Json.parse(documentInfo), EmployeeDocumentDTO.class);

        if (documentForm.hasError) {
            return badRequest(documentForm.error);
        }

        documentForm.value.setFile(filePart.getFile());
        SaveInfo saveInfo = new SaveInfo(filePart.getFilename());

        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        Response<EmployeeDocument> serviceResponse = iEmployeeDocumentUploadApi
                .addEmployeeDocument(employeeId, documentForm.value, saveInfo, employee);

        boolean result = serviceResponse.getValue() != null;

        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), serviceResponse.getValue().getDocumentName(), "document_creation_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "document_creation_failure");
        }
        if (serviceResponse.requestHasErrors())
            return badRequest(serviceResponse.getErrorsAsJsonString());

        return ok(objectMapper.toJsonString(serviceResponse.getValue()));
    }

    @ApiOperation(value = "Get all Documents uploaded by employee")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Documents", response = EmployeeDocument.class, responseContainer = "List")
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
    @WebAuth(permissions= {RoutePermissions.read_document}, roles = {RouteRole.admin})
    public Result retrieveAllEmployeeDocuments(String employeeId) {
        return ok(objectMapper.toJsonString(iEmployeeDocumentUploadApi.getDocumentsOwnedByEmployee(employeeId)));
    }

    @ApiOperation(
            value = "Get a document",
            httpMethod = "GET",
            produces = "*/*"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Document"),
            @ApiResponse(code = 404, message = "Document not found")
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
    @WebAuth(permissions= {RoutePermissions.read_document }, roles = {RouteRole.admin, RouteRole.user})
    public Result getDocumentFile(String employeeDocumentId) {
        Optional<File> document = iEmployeeDocumentUploadApi.getEmployeeActualDocument(employeeDocumentId);

        if (document.isPresent())
            return ok(document.get());
        return notFound("Document not found");
    }

    @ApiOperation(value = "Delete Document")
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
    @WebAuth(permissions= {RoutePermissions.delete_document }, roles = {RouteRole.admin})
    public Result deleteADocument(String employeeDocumentId) {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean result = iEmployeeDocumentUploadApi.deleteEmployeeDocument(employeeDocumentId);

        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "document_creation_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "document_creation_failure");
        }
        return ok(
                objectMapper.toJsonString(result)
        );
    }
}
