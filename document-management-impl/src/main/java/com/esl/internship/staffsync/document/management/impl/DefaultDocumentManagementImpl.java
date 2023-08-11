package com.esl.internship.staffsync.document.management.impl;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.document.management.api.IDocumentManagementApi;
import com.esl.internship.staffsync.document.management.dto.DocumentDTO;
import com.esl.internship.staffsync.document.management.dto.SaveInfo;
import com.esl.internship.staffsync.document.management.model.Document;
import com.esl.internship.staffsync.entities.JpaDocument;
import com.esl.internship.staffsync.entities.QJpaDocument;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.esl.internship.staffsync.commons.util.Utility.stringifyEmployee;
import static com.esl.internship.staffsync.document.management.model.DocumentMapper.INSTANCE;

public class DefaultDocumentManagementImpl implements IDocumentManagementApi {
    String documentRoot = "resource/uploads/";

    @Inject
    JPAApi jpaApi;

    private static final QJpaDocument qJpaDocumment = QJpaDocument.jpaDocument;

    public DefaultDocumentManagementImpl() {
        createDirectory(documentRoot);
    }

    @Override
    public Response<Document> addDocument(DocumentDTO documentDTO, SaveInfo saveInfo, Employee employee) {
        JpaDocument jpaDocument = INSTANCE.mapDocumentDTO(documentDTO);

        Response<Document> response = new Response<>();

        String path = writeToDisk(saveInfo, documentDTO.getFile());

        if (path == null) {
            response.putError("file", "Unable to save file");
            return response;
        }

        jpaDocument.setDocumentId(UUID.randomUUID().toString());
        jpaDocument.setDocumentUploadPath(path);
        jpaDocument.setCreatedBy(stringifyEmployee(employee));
        jpaDocument.setDateCreated(Timestamp.from(Instant.now()));

        jpaApi.em().persist(jpaDocument);

        response.setValue(INSTANCE.mapDocument(jpaDocument));

        return response;
    }

    @Override
    public Optional<Document> getDocumentById(String documentId) {
        return Optional.ofNullable(INSTANCE.mapDocument(getJpaDocumentById(documentId)));
    }

    @Override
    public Optional<DocumentDTO> getFileById(String documentId) {
        JpaDocument document = getJpaDocumentById(documentId);
        if (document == null)
            return Optional.empty();

        DocumentDTO documentDTO = INSTANCE.mapDocumentDTO(document);
        documentDTO.setFile(openFile(document.getDocumentUploadPath()));
        return Optional.of(documentDTO);
    }

    @Override
    public boolean deleteDocument(String documentId) {
        JpaDocument document = getJpaDocumentById(documentId);

        if (document == null)
            return false;

        File file = openFile(document.getDocumentUploadPath());
        if (file.delete()) {
            jpaApi.em().remove(document);
            return true;
        }
        return false;
    }

    @Override
    public String documentUploadRootPath() {
        return documentRoot;
    }

    @Override
    public void setDocumentUploadRootPath(String pathName) {
        documentRoot = pathName;
        createDirectory(documentRoot);
    }

    private boolean createDirectory(String folderPath) {
        Path path = Path.of(folderPath);
        return createDirectory(path);
    }

    private boolean createDirectory(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private String writeToDisk(SaveInfo saveInfo, File file) {
        Path parent = Paths.get(documentRoot).resolve(saveInfo.getSaveDirectory());
        Path destinationPath;

        if (saveInfo.renameFile()) {
            String extension = "";
            int dotIndex = file.getName().lastIndexOf(".");
            if (dotIndex >= 0) {
                extension = file.getName().substring(dotIndex);
            }
            destinationPath = parent.resolve(saveInfo.getFileName() + extension);
        } else {
            destinationPath = parent.resolve(file.getName());
        }

        try {
            Files.copy(file.toPath(), destinationPath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return destinationPath.normalize().toString();
    }

    private File openFile(String fileName) {
        return new File(fileName);
    }

    private JpaDocument getJpaDocumentById(String documentId) {
        return  new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaDocumment)
                .where(qJpaDocumment.documentId.eq(documentId))
                .fetchOne();
    }
}
