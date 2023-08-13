package controllers.employee_management;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.document.management.dto.SaveInfo;
import com.esl.internship.staffsync.employee.management.api.IEmployeeDocumentUploadApi;
import com.esl.internship.staffsync.employee.management.dto.EmployeeDocumentDTO;
import com.esl.internship.staffsync.employee.management.model.EmployeeDocument;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

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
            )
    })
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

        Response<EmployeeDocument> serviceResponse = iEmployeeDocumentUploadApi
                .addEmployeeDocument(employeeId, documentForm.value, saveInfo, getEmployee());

        if (serviceResponse.requestHasErrors())
            return badRequest(serviceResponse.getErrorsAsJsonString());

        return ok(objectMapper.toJsonString(serviceResponse.getValue()));
    }

    @ApiOperation(value = "Get all Documents uploaded by employee")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Documents", response = EmployeeDocument.class, responseContainer = "List")
    })
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
            @ApiResponse(code = 404, message = "File not found")
    })
    public Result getDocumentFile(String employeeDocumentId) {
        Optional<EmployeeDocument> document = iEmployeeDocumentUploadApi.getEmployeeDocument(employeeDocumentId);

        if (document.isPresent()) {
            File file = new File(document.get().getDocumentUploadPath());
            if (file.exists()) {
                return ok(file);
            }
        }        
        return notFound("Document not found");
    }

    @ApiOperation(value = "Delete Document")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Deleted", response = boolean.class)
    })
    public Result deleteADocument(String employeeDocumentId) {
        return ok(
                objectMapper.toJsonString(iEmployeeDocumentUploadApi.deleteEmployeeDocument(employeeDocumentId))
        );
    }

    /**
     * @author WARITH
     * @dateCreated 11/08/23
     * @description To return the authenticated and authorized Employee
     *
     * @return Employee
     */
    Employee getEmployee() {
        return new Employee();
    }
}
