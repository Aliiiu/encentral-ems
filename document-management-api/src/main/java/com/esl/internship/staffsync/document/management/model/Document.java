package com.esl.internship.staffsync.document.management.model;

import java.sql.Timestamp;

public class Document {

    private String documentId;

    private String createdBy;

    private Timestamp dateCreated;

    private Timestamp dateModified;

    private String documentDescription;

    private String documentName;

    private String documentUploadPath;

    private String modifiedBy;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateModified() {
        return dateModified;
    }

    public void setDateModified(Timestamp dateModified) {
        this.dateModified = dateModified;
    }

    public String getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentUploadPath() {
        return documentUploadPath;
    }

    public void setDocumentUploadPath(String documentUploadPath) {
        this.documentUploadPath = documentUploadPath;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public String toString() {
        return "Document{" +
                "documentId='" + documentId + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", dateCreated=" + dateCreated +
                ", documentDescription='" + documentDescription + '\'' +
                ", documentName='" + documentName + '\'' +
                ", documentUploadPath='" + documentUploadPath + '\'' +
                '}';
    }
}