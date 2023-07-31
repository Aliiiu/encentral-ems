package com.encentral.staffsync.entity;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;


/**
 * The persistent class for the department_head database table.
 * 
 */
@Entity
@Table(name="department_head")
@NamedQuery(name="JpaDepartmentHead.findAll", query="SELECT j FROM JpaDepartmentHead j")
public class JpaDepartmentHead implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="department_head_id", unique=true, nullable=false, length=64)
	private String departmentHeadId;

	@Column(name="created_by", nullable=false)
	private String createdBy;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Column(name="modified_by")
	private String modifiedBy;

	//bidirectional many-to-one association to JpaDepartment
	@ManyToOne
	@JoinColumn(name="department_id", nullable=false)
	private JpaDepartment department;

	//bidirectional many-to-one association to JpaEmployee
	@OneToOne
	@JoinColumn(name="employee_id", nullable=false)
	private JpaEmployee employee;

	public JpaDepartmentHead() {
	}

	public String getDepartmentHeadId() {
		return this.departmentHeadId;
	}

	public void setDepartmentHeadId(String departmentHeadId) {
		this.departmentHeadId = departmentHeadId;
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

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public JpaDepartment getDepartment() {
		return this.department;
	}

	public void setDepartment(JpaDepartment department) {
		this.department = department;
	}

	public JpaEmployee getEmployee() {
		return this.employee;
	}

	public void setEmployee(JpaEmployee employee) {
		this.employee = employee;
	}

	@Override
	public int hashCode() {
		if (this.getDepartmentHeadId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getDepartmentHeadId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		JpaDepartmentHead departmentHead = (JpaDepartmentHead) obj;
		return Objects.equals(this.getDepartmentHeadId(), departmentHead.getDepartmentHeadId());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("Department", this.getDepartment().getDepartmentName())
				.add("Head", this.getEmployee().getEmployeeEmail())
				.toString();
	}

}