package com.encentral.staffsync.entity;

import com.encentral.staffsync.entity.enums.Action;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;


/**
 * The persistent class for the audit_log database table.
 * 
 */
@Entity
@Table(name="audit_log")
public class JpaAuditLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="audit_log_id", unique=true, nullable=false, length=64)
	private String auditLogId;

	@Column(name="action_type", nullable=false)
	@Enumerated(EnumType.STRING)
	private Action actionType;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Column(name="field_name", nullable=false, length=60)
	private String fieldName;

	@Column(name="initial_value", length=2147483647)
	private String initialValue;

	@Column(name="new_value", nullable=false, length=2147483647)
	private String newValue;

	@Column(name="row_id", nullable=false, length=64)
	private String rowId;

	@Column(name="table_name", nullable=false, length=60)
	private String tableName;

	//bidirectional many-to-one association to JpaEmployee
	@ManyToOne
	@JoinColumn(name="employee_id", nullable=false)
	private JpaEmployee employee;

	public JpaAuditLog() {
	}

	public String getAuditLogId() {
		return this.auditLogId;
	}

	public void setAuditLogId(String auditLogId) {
		this.auditLogId = auditLogId;
	}

	public Action getActionType() {
		return this.actionType;
	}

	public void setActionType(Action actionType) {
		this.actionType = actionType;
	}

	public Timestamp getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(Timestamp dateModified) {
		this.dateModified = dateModified;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getInitialValue() {
		return this.initialValue;
	}

	public void setInitialValue(String initialValue) {
		this.initialValue = initialValue;
	}

	public String getNewValue() {
		return this.newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getRowId() {
		return this.rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public JpaEmployee getEmployee() {
		return this.employee;
	}

	public void setEmployee(JpaEmployee employee) {
		this.employee = employee;
	}

	@Override
	public int hashCode() {
		if (this.getAuditLogId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getAuditLogId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || this.getClass() != obj.getClass()) return false;
		JpaAuditLog auditLog = (JpaAuditLog) obj;
		return Objects.equals(this.getAuditLogId(), auditLog.getAuditLogId());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("ActionType", this.getActionType())
				.add("TableName", this.getTableName())
				.add("OldValue", this.getInitialValue())
				.add("NewValue", this.getNewValue())
				.add("Action", this.getActionType())
				.toString();
	}

}