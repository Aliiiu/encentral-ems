package controllers.document_management;

import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.authentication.model.RouteRole;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.document.management.api.IDocumentManagementApi;
import com.esl.internship.staffsync.document.management.dto.DocumentDTO;
import com.esl.internship.staffsync.document.management.dto.SaveInfo;
import com.esl.internship.staffsync.document.management.model.Document;
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

@Api("Document Management")
@Transactional
public class DocumentController extends Controller {

    @Inject
    IDocumentManagementApi iDocumentManagementApi;

    @Inject
    MyObjectMapper objectMapper;

    @Inject
    INotification iNotification;

    @Inject
    IAuthentication iAuthentication;


    @ApiOperation(value = "Upload a Document")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Document Added", response = Document.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "documentInfo",
                    value = "Document Information",
                    paramType = "formData",
                    required = true,
                    dataType = "com.esl.internship.staffsync.document.management.dto.DocumentDTO"
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
    public Result uploadADocument() {;
        Http.MultipartFormData<File> formData = request().body().asMultipartFormData();

        String documentInfo = formData.asFormUrlEncoded().get("documentInfo")[0];
        Http.MultipartFormData.FilePart<File> filePart = formData.getFile("file");

        final var documentForm = validate(Json.parse(documentInfo), DocumentDTO.class);

        if (documentForm.hasError) {
            return badRequest(documentForm.error);
        }
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();

        documentForm.value.setFile(filePart.getFile());
        SaveInfo saveInfo = new SaveInfo(filePart.getFilename());
        saveInfo.setSaveDirectory("admin");

        Response<Document> serviceResponse = iDocumentManagementApi
                .addDocument(documentForm.value, saveInfo, employee);

        if (serviceResponse.requestHasErrors())
            return badRequest(serviceResponse.getErrorsAsJsonString());


        boolean result = serviceResponse.getValue() != null;

        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee",employee.getFullName(), serviceResponse.getValue().getDocumentName(), "document_creation_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "document_creation_failure");
        }
        return ok(objectMapper.toJsonString(serviceResponse.getValue()));
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
    public Result retrieveDocument(String documentId) {
        Optional<Document> res = iDocumentManagementApi.getDocumentById(documentId);

        if (res.isPresent()) {
            String path = res.get().getDocumentUploadPath();

            File file = new File(path);
            if (file.exists())
                return ok(file);
        }
        return notFound("File not found");
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
    @WebAuth(permissions= {RoutePermissions.delete_document }, roles = {RouteRole.admin})
    public Result deleteADocument(String documentId) {

        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        boolean result = iDocumentManagementApi.deleteDocument(documentId);

        if (result) {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee",employee.getFullName(), "", "document_deletion_successful");
        } else {
            iNotification.sendNotification(employee.getEmployeeId(), "system_employee", employee.getFullName(), "", "document_deletion_failure");
        }
        return ok(
                objectMapper.toJsonString(result)
        );
    }
}
