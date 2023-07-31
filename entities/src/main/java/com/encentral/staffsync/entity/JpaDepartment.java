package com.encentral.staffsync.entity;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;


/**
 * The persistent class for the department database table.
 * 
 */
@Entity
@Table(name="department")
public class JpaDepartment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="department_id", unique=true, nullable=false, length=64)
	private String departmentId;

	@Column(name="created_by", nullable=false)
	private String createdBy;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Column(name="department_name", nullable=false, length=64)
	private String departmentName;

	@Column(name="description", length=2147483647)
	private String description;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="working_hours", nullable=false)
	private Integer workingHours;

	//bidirectional many-to-one association to JpaDepartmentHead
	@OneToOne(mappedBy="department")
	private JpaDepartmentHead departmentHead;

	//bidirectional many-to-one association to JpaEmployee
	@OneToMany(mappedBy="department")
	private Set<JpaEmployee> employees;

	public JpaDepartment() {
	}

	public String getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
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

	public String getDepartmentName() {
		return this.departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Integer getWorkingHours() {
		return this.workingHours;
	}

	public void setWorkingHours(Integer workingHours) {
		this.workingHours = workingHours;
	}

	public JpaDepartmentHead getDepartmentHead() {
		return this.departmentHead;
	}

	public void setDepartmentHead(JpaDepartmentHead departmentHead) {
		this.departmentHead = departmentHead;
	}

	public Set<JpaEmployee> getEmployees() {
		return this.employees;
	}

	public void setEmployees(Set<JpaEmployee> employees) {
		this.employees = employees;
	}

	public JpaEmployee addEmployee(JpaEmployee employee) {
		getEmployees().add(employee);
		employee.setDepartment(this);

		return employee;
	}

	public JpaEmployee removeEmployee(JpaEmployee employee) {
		getEmployees().remove(employee);
		employee.setDepartment(null);

		return employee;
	}

	@Override
	public int hashCode() {
		if (this.getDepartmentId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getDepartmentId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || this.getClass() != obj.getClass()) return false;
		JpaDepartment department = (JpaDepartment) obj;
		return Objects.equals(this.getDepartmentId(), department.getDepartmentId());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("Name", this.getDepartmentName())
				.add("Head", this.getDepartmentHead().getEmployee().getEmployeeEmail())
				.toString();
	}

}