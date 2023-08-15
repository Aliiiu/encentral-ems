package com.esl.internship.staffsync.employee.management.impl;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.document.management.api.IDocumentManagementApi;
import com.esl.internship.staffsync.document.management.dto.DocumentDTO;
import com.esl.internship.staffsync.document.management.dto.DocumentUpdateDTO;
import com.esl.internship.staffsync.document.management.dto.SaveInfo;
import com.esl.internship.staffsync.document.management.model.Document;
import com.esl.internship.staffsync.document.management.model.DocumentMapper;
import com.esl.internship.staffsync.employee.management.api.IEmployeeDocumentUploadApi;
import com.esl.internship.staffsync.employee.management.dto.EmployeeDocumentDTO;
import com.esl.internship.staffsync.employee.management.model.EmployeeDocument;
import com.esl.internship.staffsync.entities.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.esl.internship.staffsync.employee.management.model.EmployeeManagementMapper.INSTANCE;

public class DefaultEmployeeDocumentUploadApiImpl implements IEmployeeDocumentUploadApi {

    @Inject
    JPAApi jpaApi;

    @Inject
    IDocumentManagementApi iDocumentManagementApi;

    String employeeRootUploadDirectory = "employee/";

    private static final QJpaOption qJpaOption = QJpaOption.jpaOption;

    private static final QJpaEmployeeHasDocument qJpaEmployeeHasDocument = QJpaEmployeeHasDocument.jpaEmployeeHasDocument;

    private static final QJpaEmployee qJpaEmployee = QJpaEmployee.jpaEmployee;

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Add a new document
     *
     * @param employeeId ID of the employee
     * @param employeeDocumentDTO  Upload information
     * @param saveInfo  Saving information
     * @param employee Employee making the upload
     *
     * @return Response<EmployeeDocument>
     */
    @Override
    public Response<EmployeeDocument> addEmployeeDocument(String employeeId, EmployeeDocumentDTO employeeDocumentDTO, SaveInfo saveInfo, Employee employee) {

        Response<EmployeeDocument> response = new Response<>();

        JpaEmployee jpaEmployee = getJpaEmployee(employeeId);
        if (jpaEmployee == null)
            response.putError("employeeId", "Employee does not exist");

        JpaOption documentType = getJpaOption(employeeDocumentDTO.getDocumentTypeOptionId());
        if (documentType == null)
            response.putError("documentTypeOptionId", "Option does not exist");

        if (response.requestHasErrors())
            return response;

        DocumentDTO documentDTO = INSTANCE.mapDocumentDTO(employeeDocumentDTO);
        saveInfo.setSaveDirectory(
                Path.of(resolveEmployeeDirectory(jpaEmployee), saveInfo.getSaveDirectory()).toString()
        );
        Response<Document> res = iDocumentManagementApi.addDocument(documentDTO, saveInfo, employee);

        if (res.requestHasErrors()) {
            response.putError("document", res.getErrorsAsJsonString());
            return response;
        }

        Document document = res.getValue();

        JpaDocument jpaDocument = jpaApi.em().find(JpaDocument.class, document.getDocumentId());

        JpaEmployeeHasDocument jpaEmployeeHasDocument = new JpaEmployeeHasDocument();
        jpaEmployeeHasDocument.setEmployeeHasDocumentId(UUID.randomUUID().toString());
        jpaEmployeeHasDocument.setEmployee(jpaEmployee);
        jpaEmployeeHasDocument.setCreatedBy(document.getCreatedBy());
        jpaEmployeeHasDocument.setDateCreated(document.getDateCreated());
        jpaEmployeeHasDocument.setDocumentType(documentType);
        jpaEmployeeHasDocument.setDocument(jpaDocument);

        jpaApi.em().persist(jpaEmployeeHasDocument);

        response.setValue(INSTANCE.mapEmployeeDocument(jpaEmployeeHasDocument));

        return response;

    }

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Get an employee document by ID
     *
     * @param employeeDocumentId ID of the employee document
     *
     * @return Optional<EmployeeDocument>
     */
    @Override
    public Optional<EmployeeDocument> getEmployeeDocument(String employeeDocumentId) {

        return Optional.ofNullable(INSTANCE.mapEmployeeDocument(getJpaEmployeeHasDocument(employeeDocumentId)));
    }

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Get the actual employee document file
     *
     * @param employeeDocumentId ID of the employee document
     *
     * @return Optional<File>
     */
    @Override
    public Optional<File> getEmployeeActualDocument(String employeeDocumentId) {
        JpaEmployeeHasDocument employeeDocument = getJpaEmployeeHasDocument(employeeDocumentId);
        String path = employeeDocument.getDocument().getDocumentUploadPath();

        File file = new File(path);
        if (file.exists())
            return Optional.of(file);
        return Optional.empty();
    }

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Get all Employee documents
     *
     * @param employeeId ID of the employee
     *
     * @return Optional<EmployeeDocument>
     */
    @Override
    public List<EmployeeDocument> getDocumentsOwnedByEmployee(String employeeId) {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaEmployeeHasDocument)
                .where(qJpaEmployeeHasDocument.employee.employeeId.eq(employeeId))
                .fetch()
                .stream()
                .map(INSTANCE::mapEmployeeDocument)
                .collect(Collectors.toList());
    }

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Get all Employee documents
     *
     * @return List<EmployeeDocument>
     */
    @Override
    public List<EmployeeDocument> getAllEmployeeDocuments() {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaEmployeeHasDocument)
                .fetch()
                .stream()
                .map(INSTANCE::mapEmployeeDocument)
                .collect(Collectors.toList());
    }

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Update an employee Document
     *
     * @param employeeDocumentId ID of the employee document
     * @param employeeDocumentDTO New data to update
     * @param saveInfo Saving Information
     *
     * @return boolean
     */
    @Override
    public boolean updateEmployeeDocument(String employeeDocumentId, EmployeeDocumentDTO employeeDocumentDTO, SaveInfo saveInfo, Employee employee) {

        JpaEmployeeHasDocument jpaEmployeeHasDocument = getJpaEmployeeHasDocument(employeeDocumentId);
        JpaOption documentType = getJpaOption(employeeDocumentDTO.getDocumentTypeOptionId());

        if (jpaEmployeeHasDocument == null || documentType == null)
            return false;

        DocumentUpdateDTO documentDTO = INSTANCE.mapDocumentUpdateDTO(employeeDocumentDTO);
        saveInfo.setSaveDirectory(
                Path.of(
                        resolveEmployeeDirectory(jpaEmployeeHasDocument.getEmployee()),
                        saveInfo.getSaveDirectory()
                ).toString()
        );

        if (iDocumentManagementApi.updateDocument(
                jpaEmployeeHasDocument.getDocument().getDocumentId(),
                documentDTO,
                saveInfo,
                employee)) {
            jpaEmployeeHasDocument.setDocumentType(documentType);
            return true;
        }
        return false;
    }


    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Delete an Employee Document
     *
     * @param employeeDocumentId ID of the employee document
     *
     * @return boolean
     */
    @Override
    public boolean deleteEmployeeDocument(String employeeDocumentId) {
        String documentId = getJpaEmployeeHasDocument(employeeDocumentId).getDocument().getDocumentId();
        if( new JPAQueryFactory(jpaApi.em())
                .delete(qJpaEmployeeHasDocument)
                .where(qJpaEmployeeHasDocument.employeeHasDocumentId.eq(employeeDocumentId))
                .execute() == 1
        ) {
            return iDocumentManagementApi.deleteDocument(documentId);
        }
        return false;
    }

    /**
     * @author WARITH
     * @dateCreated 12/08/2023
     * @description Get the root employee document upload path
     *
     * @return String
     */
    @Override
    public String getEmployeeDocumentUploadRoot() {
        return employeeRootUploadDirectory;
    }

    private String resolveEmployeeDirectory(JpaEmployee employee) {
        return Path.of(employeeRootUploadDirectory,
                String.format("%s(%s)", employee.getEmployeeId(), employee.getEmployeeEmail())
        ).toString();
    }

    /**
     * @author WARITH
     * @dateCreated 11/08/2023
     * @description A helper method to fetch an option record from the database
     *
     * @param optionId ID of the option to fetch
     *
     * @return JpaOption An option record or null if not found
     */
    private JpaOption getJpaOption(String optionId) {
        if (optionId == null)
            return null;
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaOption)
                .where(qJpaOption.optionId.eq(optionId))
                .fetchOne();
    }

    /**
     * @author WARITH
     * @dateCreated 11/08/2023
     * @description A helper method to fetch an employee record from the database
     *
     * @param employeeId ID of the employee to fetch
     *
     * @return JpaEmployee An employee record or null if not found
     */
    private JpaEmployee getJpaEmployee(String employeeId) {
        if (employeeId == null)
            return null;
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .fetchOne();
    }

    /**
     * @author WARITH
     * @dateCreated 11/08/2023
     * @description A helper method to fetch an employeeHasDocument record from the database
     *
     * @param employeeHasDocumentId ID of the employeeHasDocument to fetch
     *
     * @return JpaEmployee An employeeHasDocument record or null if not found
     */
    private JpaEmployeeHasDocument getJpaEmployeeHasDocument(String employeeHasDocumentId) {
        if (employeeHasDocumentId == null)
            return null;
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEmployeeHasDocument)
                .where(qJpaEmployeeHasDocument.employeeHasDocumentId.eq(employeeHasDocumentId))
                .fetchOne();
    }
}
