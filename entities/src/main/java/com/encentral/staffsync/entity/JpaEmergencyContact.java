package com.encentral.staffsync.entity;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;


/**
 * The persistent class for the emergency_contact database table.
 * 
 */
@Entity
@Table(name="emergency_contact")
public class JpaEmergencyContact implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="emergency_contact_id", unique=true, nullable=false, length=64)
	private String emergencyContactId;

	@Column(nullable=false, length=64)
	private String address;

	@Column(name="created_by", nullable=false)
	private String createdBy;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Column(nullable=false, length=64)
	private String email;

	@Column(name="first_name", nullable=false, length=64)
	private String firstName;

	@Column(name="last_name", nullable=false, length=64)
	private String lastName;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="phone_number", nullable=false, length=20)
	private String phoneNumber;

	@Column(nullable=false, length=64)
	private String relationship;

	//bidirectional many-to-one association to JpaEmployee
	@ManyToOne
	@JoinColumn(name="employee_id", nullable=false)
	private JpaEmployee employee;

	//bidirectional many-to-one association to JpaOption
	@ManyToOne
	@JoinColumn(name="option_value")
	private JpaOption contactGender;

	public JpaEmergencyContact() {
	}

	public String getEmergencyContactId() {
		return this.emergencyContactId;
	}

	public void setEmergencyContactId(String emergencyContactId) {
		this.emergencyContactId = emergencyContactId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getRelationship() {
		return this.relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public JpaEmployee getEmployee() {
		return this.employee;
	}

	public void setEmployee(JpaEmployee employee) {
		this.employee = employee;
	}

	public JpaOption getContactGender() {
		return contactGender;
	}

	public void setContactGender(JpaOption contactGender) {
		this.contactGender = contactGender;
	}

	@Override
	public int hashCode() {
		if (this.getEmergencyContactId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getEmergencyContactId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		JpaEmergencyContact emergencyContact = (JpaEmergencyContact) obj;
		return Objects.equals(this.getEmergencyContactId(), emergencyContact.getEmergencyContactId());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("FirstName", this.getFirstName())
				.add("Email", this.getEmail())
				.add("Relationship", this.getRelationship())
				.toString();
	}

}