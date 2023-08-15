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

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Add a new document
     *
     * @param documentDTO Document Data
     * @param saveInfo Saving information
     * @param employee Employee saving the document
     *
     * @return Response<Document>
     */
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
        jpaDocument.setDocumentName(saveInfo.getResolvedName());
        jpaDocument.setCreatedBy(stringifyEmployee(employee));
        jpaDocument.setDateCreated(Timestamp.from(Instant.now()));

        jpaApi.em().persist(jpaDocument);

        response.setValue(INSTANCE.mapDocument(jpaDocument));

        return response;
    }

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Get an uploaded document by ID
     *
     * @param documentId ID of the document
     *
     * @return Optional<Document>
     */
    @Override
    public Optional<Document> getDocumentById(String documentId) {
        return Optional.ofNullable(INSTANCE.mapDocument(getJpaDocumentById(documentId)));
    }

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Update a Document
     *
     * @param documentDTO Document Data
     * @param saveInfo Saving information
     * @param employee Employee saving the document
     *
     * @return boolean
     */
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
        document.setDocumentName(saveInfo.getResolvedName());
        document.setDateModified(Timestamp.from(Instant.now()));
        document.setModifiedBy(stringifyEmployee(employee, "Updated Document"));

        return true;
    }

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Get the actual document file
     *
     * @param documentId Document ID
     *
     * @return Response<DocumentDTO>
     */
    @Override
    public Optional<DocumentDTO> getFileById(String documentId) {
        JpaDocument document = getJpaDocumentById(documentId);
        if (document == null)
            return Optional.empty();

        File file = new File(document.getDocumentUploadPath());
        if (file.exists()){
            DocumentDTO documentDTO = INSTANCE.mapDocumentDTO(document);
            documentDTO.setFile(file);
            return Optional.of(documentDTO);
        }
        return Optional.empty();
    }

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Delete a Document
     *
     * @param documentId ID of document to delete
     *
     * @return boolean
     */
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

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Get the upload root path
     *
     * @return String
     */
    @Override
    public String documentUploadRootPath() {
        return documentRoot;
    }

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Set the upload root path
     *
     * @param pathName Path
     */
    @Override
    public void setDocumentUploadRootPath(String pathName) {
        documentRoot = pathName;
        createDirectory(documentRoot);
    }

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Helper function to create directories
     *
     * @param folderPath Folder path
     *
     * @return boolean (True on successful creation)
     */
    private boolean createDirectory(String folderPath) {
        Path path = Path.of(folderPath);
        return createDirectory(path);
    }

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Helper function to create directories
     *
     * @param folderPath Folder path
     *
     * @return boolean (True on successful creation)
     */
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

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Temporarily write file to disk by appending a UUID to file to be deleted
     *
     * @param saveInfo Saving information
     * @param file File to write to disk
     *
     * @return String Full path of the file written to disk
     */
    private String tempWriteToDisk(SaveInfo saveInfo, File file) {
        SaveInfo tempCopy = new SaveInfo(UUID.randomUUID() + saveInfo.getFileName());
        tempCopy.setRename(saveInfo.renameFile());
        tempCopy.setSaveDirectory(saveInfo.getSaveDirectory());
        tempCopy.setNewFileName(UUID.randomUUID() + saveInfo.getNewFileName());

        return writeToDisk(tempCopy, file);
    }

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Resolve destination file name
     *
     * @param saveInfo Saving information
     *
     * @return String Full destination path of the file
     */
    private Path resolveDestinationFilename(SaveInfo saveInfo) {
        Path parent = Paths.get(documentRoot).resolve(saveInfo.getSaveDirectory());
        createDirectory(parent);
        String destinationFilename;

        destinationFilename = saveInfo.getResolvedName();

        return parent.resolve(destinationFilename);
    }

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Write file to disk
     *
     * @param saveInfo Saving information
     * @param file File to write to disk
     *
     * @return String Full path of the file written to disk
     */
    private String writeToDisk(SaveInfo saveInfo, File file) {

        Path destinationPath = resolveDestinationFilename(saveInfo);

        try {
            FileUtils.copyFile(file, destinationPath.toFile());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return destinationPath.normalize().toString();
    }

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Helper function to move file to another location
     *
     * @param source Source file path
     * @param target Target Saving information
     *
     * @return String Full path of the file moved
     */
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

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Helper function to get JpaDocument by ID
     *
     * @param documentId ID of the document to fetch
     *
     * @return JpaDocument
     */
    private JpaDocument getJpaDocumentById(String documentId) {
        if (documentId == null)
            return null;
        return  new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaDocument)
                .where(qJpaDocument.documentId.eq(documentId))
                .fetchOne();
    }
}
