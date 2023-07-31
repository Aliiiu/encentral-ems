package com.encentral.staffsync.entity;

import com.encentral.staffsync.entity.enums.EmployeeRequestStatus;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;


/**
 * The persistent class for the employee_update_request database table.
 * 
 */
@Entity
@Table(name="employee_update_request")
public class JpaEmployeeUpdateRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="employee_update_request_id", unique=true, nullable=false, length=64)
	private String employeeUpdateRequestId;

	@Column(name="approval_status", nullable=false, length=2147483647)
	@Enumerated(EnumType.STRING)
	private EmployeeRequestStatus approvalStatus;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Column(name="old_value", length=2147483647)
	private String oldValue;

	@Column(length=2147483647)
	private String reason;

	@Column(length=2147483647)
	private String remarks;

	@Column(name="update_field_name", nullable=false, length=64)
	private String updateFieldName;

	@Column(name="update_new_value", nullable=false, length=2147483647)
	private String updateNewValue;

	//bidirectional many-to-one association to JpaEmployee
	@ManyToOne
	@JoinColumn(name="approver_id")
	private JpaEmployee approver;

	//bidirectional many-to-one association to JpaEmployee
	@ManyToOne
	@JoinColumn(name="employee_id", nullable=false)
	private JpaEmployee employee;

	public JpaEmployeeUpdateRequest() {
	}

	public String getEmployeeUpdateRequestId() {
		return this.employeeUpdateRequestId;
	}

	public void setEmployeeUpdateRequestId(String employeeUpdateRequestId) {
		this.employeeUpdateRequestId = employeeUpdateRequestId;
	}

	public EmployeeRequestStatus getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(EmployeeRequestStatus approvalStatus) {
		this.approvalStatus = approvalStatus;
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

	public String getOldValue() {
		return this.oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUpdateFieldName() {
		return this.updateFieldName;
	}

	public void setUpdateFieldName(String updateFieldName) {
		this.updateFieldName = updateFieldName;
	}

	public String getUpdateNewValue() {
		return this.updateNewValue;
	}

	public void setUpdateNewValue(String updateNewValue) {
		this.updateNewValue = updateNewValue;
	}

	public JpaEmployee getApprover() {
		return approver;
	}

	public void setApprover(JpaEmployee approver) {
		this.approver = approver;
	}

	public JpaEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(JpaEmployee employee) {
		this.employee = employee;
	}

	@Override
	public int hashCode() {
		if (this.getEmployeeUpdateRequestId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getEmployeeUpdateRequestId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		JpaEmployeeUpdateRequest employeeUpdateRequest = (JpaEmployeeUpdateRequest) obj;
		return Objects.equals(this.getEmployeeUpdateRequestId(), employeeUpdateRequest.getEmployeeUpdateRequestId());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("FieldName", this.getUpdateFieldName())
				.add("Employee", this.getEmployee().getEmployeeEmail())
				.add("NewValue", this.getUpdateNewValue())
				.add("Status", this.getApprovalStatus())
				.toString();
	}

}