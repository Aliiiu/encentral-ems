package com.esl.internship.staffsync.document.management.api;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.document.management.dto.DocumentDTO;
import com.esl.internship.staffsync.document.management.dto.DocumentUpdateDTO;
import com.esl.internship.staffsync.document.management.dto.SaveInfo;
import com.esl.internship.staffsync.document.management.model.Document;

import java.io.File;
import java.util.Optional;

public interface IDocumentManagementApi {

    Response<Document> addDocument(DocumentDTO documentDTO, SaveInfo saveInfo, Employee employee);

    Optional<Document> getDocumentById(String documentId);

    boolean updateDocument(String documentId, DocumentDTO documentDTO, SaveInfo info, Employee employee);

    Optional<DocumentDTO> getFileById(String documentId);

    boolean deleteDocument(String documentId);

    String documentUploadRootPath();

    void setDocumentUploadRootPath(String pathName);

}
