package controllers.document_management;

import akka.http.javadsl.model.ResponseEntity;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.document.management.api.IDocumentManagementApi;
import com.esl.internship.staffsync.document.management.dto.DocumentDTO;
import com.esl.internship.staffsync.document.management.dto.SaveInfo;
import com.esl.internship.staffsync.document.management.model.Document;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.File;

public class DocumentController extends Controller {

    @Inject
    IDocumentManagementApi iDocumentManagementApi;

    @Inject
    MyObjectMapper objectMapper;

    public Result uploadADocument() {
        Http.MultipartFormData<File> formData = request().body().asMultipartFormData();

        if (formData != null) {
            Http.MultipartFormData.FilePart<File> filePart = formData.getFile("file");
            if (filePart != null) {
                File file = filePart.getFile();

                DocumentDTO documentDTO = new DocumentDTO();
                documentDTO.setFile(filePart.getFile());
                documentDTO.setDocumentName("Some Name");
                documentDTO.setDocumentDescription("Some Description");

                SaveInfo saveInfo = new SaveInfo("admin", "TestFileRenameTo");

                Response<Document> res =  iDocumentManagementApi.addDocument(documentDTO, saveInfo, getEmployee());
                if (res.requestHasErrors())
                    return badRequest(res.getErrorsAsJsonString());

                return ok(objectMapper.toJsonString(res.getValue()));
            }

        }

        return badRequest("Document Required");
    }

    /**
     * @author WARITH
     * @dateCreated 11/08/23
     * @description To return the authenticated and authorized Employee
     *
     * @return Employee
     */
    Employee getEmployee() {
        return new Employee("ESL-WA04052013-123", "Warith");
    }
}
