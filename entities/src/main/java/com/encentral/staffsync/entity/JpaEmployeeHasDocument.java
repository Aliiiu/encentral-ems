package com.encentral.staffsync.entity;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;


/**
 * The persistent class for the employee_has_document database table.
 * 
 */
@Entity
@Table(name="employee_has_document")
public class JpaEmployeeHasDocument implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="document_id", unique=true, nullable=false, length=64)
	private String documentId;

	@Column(name="created_by", nullable=false)
	private String createdBy;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="employee_has_document_id", nullable=false, length=64)
	private String employeeHasDocumentId;

	//bidirectional one-to-one association to JpaDocument
	@OneToOne
	@JoinColumn(name="document_id", nullable=false, insertable=false, updatable=false)
	private JpaDocument document;

	//bidirectional many-to-one association to JpaEmployee
	@ManyToOne
	@JoinColumn(name="employee_id", nullable=false)
	private JpaEmployee employee;

	//bidirectional many-to-one association to JpaOption
	@ManyToOne
	@JoinColumn(name="option_value", nullable=false)
	private JpaOption documentType;

	public JpaEmployeeHasDocument() {
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

	public String getEmployeeHasDocumentId() {
		return this.employeeHasDocumentId;
	}

	public void setEmployeeHasDocumentId(String employeeHasDocumentId) {
		this.employeeHasDocumentId = employeeHasDocumentId;
	}

	public JpaDocument getDocument() {
		return this.document;
	}

	public void setDocument(JpaDocument document) {
		this.document = document;
	}

	public JpaEmployee getEmployee() {
		return this.employee;
	}

	public void setEmployee(JpaEmployee employee) {
		this.employee = employee;
	}

	public JpaOption getDocumentType() {
		return documentType;
	}

	public void setDocumentType(JpaOption documentType) {
		this.documentType = documentType;
	}

	@Override
	public int hashCode() {
		if (this.getEmployeeHasDocumentId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getEmployeeHasDocumentId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		JpaEmployeeHasDocument employeeHasDocument = (JpaEmployeeHasDocument) obj;
		return Objects.equals(this.getEmployeeHasDocumentId(), employeeHasDocument.getEmployeeHasDocumentId());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("DocumentName", this.getDocument().getDocumentName())
				.add("Owner", this.getEmployee().getEmployeeEmail())
				.add("Type", this.getDocumentType())
				.toString();
	}
}