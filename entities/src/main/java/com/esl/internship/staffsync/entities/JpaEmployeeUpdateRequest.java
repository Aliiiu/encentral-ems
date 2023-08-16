package com.esl.internship.staffsync.entities;


import com.esl.internship.staffsync.entities.attribute.converter.EmployeeRequestStatusConverter;
import com.esl.internship.staffsync.entities.enums.EmployeeRequestStatus;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
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

	@Convert(converter = EmployeeRequestStatusConverter.class)
	@Column(name="approval_status", nullable=false, length=2147483647)
	private EmployeeRequestStatus approvalStatus;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Column(length=2147483647)
	private String remarks;

	@Column(name="address", nullable=false, length=2147483647)
	private String address;

	@Temporal(TemporalType.DATE)
	@Column(name="date_of_birth", nullable=false)
	private Date dateOfBirth;

	@Column(name="first_name", nullable=false, length=64)
	private String firstName;

	@Column(name="last_name", nullable=false, length=64)
	private String lastName;

	@Column(name="phone_number", nullable=false, length=20)
	private String phoneNumber;

	//bidirectional many-to-one association to JpaOption
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="country_of_origin", referencedColumnName = "option_id", nullable=false)
	private JpaOption countryOfOrigin;

	//bidirectional many-to-one association to JpaOption
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="employee_gender", referencedColumnName = "option_id", nullable=false)
	private JpaOption employeeGender;

	//bidirectional many-to-one association to JpaOption
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="employee_marital_status", referencedColumnName = "option_id")
	private JpaOption employeeMaritalStatus;

	//bidirectional many-to-one association to JpaOption
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="highest_certification", referencedColumnName = "option_id")
	private JpaOption highestCertification;

	//bidirectional many-to-one association to JpaOption
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="state_of_origin", referencedColumnName = "option_id")
	private JpaOption stateOfOrigin;

	//bidirectional many-to-one association to JpaEmployee
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="approver_id")
	private JpaEmployee approver;

	//bidirectional many-to-one association to JpaEmployee
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="employee_id", nullable=false)
	private JpaEmployee employee;

	// Emergency Contact
	@Column(name="emergency_contact_full_name", nullable=false, length=64)
	private String emergencyContactFullName;

	@Column(name="emergency_contact_relationship", nullable=false, length=64)
	private String emergencyContactRelationship;

	@Column(name="emergency_contact_phone_number", nullable=false, length=20)
	private String emergencyContactPhoneNumber;

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

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public JpaOption getCountryOfOrigin() {
		return countryOfOrigin;
	}

	public void setCountryOfOrigin(JpaOption countryOfOrigin) {
		this.countryOfOrigin = countryOfOrigin;
	}

	public JpaOption getEmployeeGender() {
		return employeeGender;
	}

	public void setEmployeeGender(JpaOption employeeGender) {
		this.employeeGender = employeeGender;
	}

	public JpaOption getEmployeeMaritalStatus() {
		return employeeMaritalStatus;
	}

	public void setEmployeeMaritalStatus(JpaOption employeeMaritalStatus) {
		this.employeeMaritalStatus = employeeMaritalStatus;
	}

	public JpaOption getHighestCertification() {
		return highestCertification;
	}

	public void setHighestCertification(JpaOption highestCertification) {
		this.highestCertification = highestCertification;
	}

	public JpaOption getStateOfOrigin() {
		return stateOfOrigin;
	}

	public void setStateOfOrigin(JpaOption stateOfOrigin) {
		this.stateOfOrigin = stateOfOrigin;
	}

	public String getEmergencyContactFullName() {
		return emergencyContactFullName;
	}

	public void setEmergencyContactFullName(String emergencyContactFullName) {
		this.emergencyContactFullName = emergencyContactFullName;
	}

	public String getEmergencyContactRelationship() {
		return emergencyContactRelationship;
	}

	public void setEmergencyContactRelationship(String emergencyContactRelationship) {
		this.emergencyContactRelationship = emergencyContactRelationship;
	}

	public String getEmergencyContactPhoneNumber() {
		return emergencyContactPhoneNumber;
	}

	public void setEmergencyContactPhoneNumber(String emergencyContactPhoneNumber) {
		this.emergencyContactPhoneNumber = emergencyContactPhoneNumber;
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
		return MoreObjects.toStringHelper(this)
				.add("employeeUpdateRequestId", employeeUpdateRequestId)
				.add("approvalStatus", approvalStatus)
				.add("employee", employee)
				.toString();
	}
}