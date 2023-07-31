package com.encentral.staffsync.entity;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;


/**
 * The persistent class for the document database table.
 * 
 */
@Entity
@Table(name="document")
public class JpaDocument implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="document_id", unique=true, nullable=false, length=64)
	private String documentId;

	@Column(name="created_by", nullable=false)
	private String createdBy;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Column(name="document_description", length=512)
	private String documentDescription;

	@Column(name="document_name", nullable=false, length=512)
	private String documentName;

	@Column(name="document_upload_path", nullable=false, length=2147483647)
	private String documentUploadPath;

	@Column(name="modified_by")
	private String modifiedBy;

	//bidirectional one-to-one association to JpaEmployeeHasDocument
	@OneToOne(mappedBy="document")
	private JpaEmployeeHasDocument employeeHasDocument;

	public JpaDocument() {
	}

	public String getDocumentId() {
		return this.documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Timestamp getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(Timestamp dateModified) {
		this.dateModified = dateModified;
	}

	public String getDocumentDescription() {
		return this.documentDescription;
	}

	public void setDocumentDescription(String documentDescription) {
		this.documentDescription = documentDescription;
	}

	public String getDocumentName() {
		return this.documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentUploadPath() {
		return this.documentUploadPath;
	}

	public void setDocumentUploadPath(String documentUploadPath) {
		this.documentUploadPath = documentUploadPath;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public JpaEmployeeHasDocument getEmployeeHasDocument() {
		return this.employeeHasDocument;
	}

	public void setEmployeeHasDocument(JpaEmployeeHasDocument employeeHasDocument) {
		this.employeeHasDocument = employeeHasDocument;
	}

	@Override
	public int hashCode() {
		if (this.getDocumentId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getDocumentId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		JpaDocument document = (JpaDocument) obj;
		return Objects.equals(this.getDocumentId(), document.getDocumentId());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("DocumentName", this.getDocumentName())
				.add("Path", this.getDocumentUploadPath())
				.add("OwnedBy",
						(this.getEmployeeHasDocument() != null) ?
								this.getEmployeeHasDocument().getEmployee().getEmployeeEmail() :
								null)
				.toString();
	}

}