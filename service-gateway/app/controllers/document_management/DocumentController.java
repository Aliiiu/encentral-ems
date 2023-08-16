package controllers.document_management;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.document.management.api.IDocumentManagementApi;
import com.esl.internship.staffsync.document.management.dto.DocumentDTO;
import com.esl.internship.staffsync.document.management.dto.SaveInfo;
import com.esl.internship.staffsync.document.management.model.Document;
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

@Api("Document Management")
@Transactional
public class DocumentController extends Controller {

    @Inject
    IDocumentManagementApi iDocumentManagementApi;

    @Inject
    MyObjectMapper objectMapper;


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
            )
    })
    public Result uploadADocument() {;
        Http.MultipartFormData<File> formData = request().body().asMultipartFormData();

        String documentInfo = formData.asFormUrlEncoded().get("documentInfo")[0];
        Http.MultipartFormData.FilePart<File> filePart = formData.getFile("file");

        final var documentForm = validate(Json.parse(documentInfo), DocumentDTO.class);

        if (documentForm.hasError) {
            return badRequest(documentForm.error);
        }

        documentForm.value.setFile(filePart.getFile());
        SaveInfo saveInfo = new SaveInfo(filePart.getFilename());
        saveInfo.setSaveDirectory("admin");

        Response<Document> serviceResponse = iDocumentManagementApi
                .addDocument(documentForm.value, saveInfo, getEmployee());

        if (serviceResponse.requestHasErrors())
            return badRequest(serviceResponse.getErrorsAsJsonString());

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
    public Result deleteADocument(String documentId) {
        return ok(
                objectMapper.toJsonString(iDocumentManagementApi.deleteDocument(documentId))
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
        return new Employee("system", "system");
    }
}
