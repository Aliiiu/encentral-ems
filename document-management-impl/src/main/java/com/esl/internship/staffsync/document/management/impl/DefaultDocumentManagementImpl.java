package com.esl.internship.staffsync.document.management.impl;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.document.management.api.IDocumentManagementApi;
import com.esl.internship.staffsync.document.management.dto.DocumentDTO;
import com.esl.internship.staffsync.document.management.dto.DocumentUpdateDTO;
import com.esl.internship.staffsync.document.management.dto.SaveInfo;
import com.esl.internship.staffsync.document.management.model.Document;
import com.esl.internship.staffsync.entities.JpaDocument;
import com.esl.internship.staffsync.entities.QJpaDocument;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.io.FileUtils;
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

    private static final QJpaDocument qJpaDocument = QJpaDocument.jpaDocument;

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

        System.out.println("\n\t" + jpaDocument);
        System.out.println("\tDocument " + jpaDocument.getDocumentId());

        jpaApi.em().persist(jpaDocument);

        response.setValue(INSTANCE.mapDocument(jpaDocument));

        return response;
    }

    @Override
    public Optional<Document> getDocumentById(String documentId) {
        return Optional.ofNullable(INSTANCE.mapDocument(getJpaDocumentById(documentId)));
    }

    @Override
    public boolean updateDocument(String documentId, DocumentDTO documentDTO, SaveInfo saveInfo, Employee employee) {
        JpaDocument document = getJpaDocumentById(documentId);
        if (document == null)
            return false;

        File file = documentDTO.getFile();

        if (file != null) {
            String path = tempWriteToDisk(saveInfo, file);
            if (path == null)
                return false;
            File oldFile = new File(document.getDocumentUploadPath());
            oldFile.delete();
            path = moveToLocation(path, saveInfo);
            document.setDocumentUploadPath(path);
        }

        document.setDocumentDescription(documentDTO.getDocumentDescription());
        document.setDocumentName(documentDTO.getDocumentName());
        document.setDateModified(Timestamp.from(Instant.now()));
        document.setModifiedBy(stringifyEmployee(employee, "Updated Document"));

        return true;
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

    private String tempWriteToDisk(SaveInfo saveInfo, File file) {
        SaveInfo tempCopy = new SaveInfo(UUID.randomUUID() + saveInfo.getFileName());
        tempCopy.setRename(saveInfo.renameFile());
        tempCopy.setSaveDirectory(saveInfo.getSaveDirectory());
        tempCopy.setNewFileName(UUID.randomUUID() + saveInfo.getNewFileName());

        return writeToDisk(tempCopy, file);
    }

    private Path resolveDestinationFilename(SaveInfo saveInfo) {
        Path parent = Paths.get(documentRoot).resolve(saveInfo.getSaveDirectory());
        createDirectory(parent);
        String destinationFilename;

        if (saveInfo.renameFile()) {
            String extension = "";
            int dotIndex = saveInfo.getFileName().lastIndexOf(".");
            if (dotIndex >= 0) {
                extension = saveInfo.getFileName().substring(dotIndex);
            }
            destinationFilename = saveInfo.getNewFileName() + extension;
        } else {
            destinationFilename = saveInfo.getFileName();
        }

        return parent.resolve(destinationFilename);
    }

    private String writeToDisk(SaveInfo saveInfo, File file) {

        Path parent = Paths.get(documentRoot).resolve(saveInfo.getSaveDirectory());

        Path destinationPath = resolveDestinationFilename(saveInfo);

        try {
            FileUtils.copyFile(file, destinationPath.toFile());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return destinationPath.normalize().toString();
    }

    private String moveToLocation(String source, SaveInfo target) {
        Path destinationPath = resolveDestinationFilename(target);

        try {
            FileUtils.moveFile(new File(source), destinationPath.toFile());
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
                .selectFrom(qJpaDocument)
                .where(qJpaDocument.documentId.eq(documentId))
                .fetchOne();
    }
}
