package com.encentral.staffsync.entity;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;


/**
 * The persistent class for the employee database table.
 * 
 */
@Entity
@Table(name="employee")
public class JpaEmployee implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="employee_id", unique=true, nullable=false, length=64)
	private String employeeId;

	@Column(nullable=false, length=225)
	private String address;

	@Column(name="created_by", nullable=false)
	private String createdBy;

	@Column(name="current_status", nullable=false, length=2147483647)
	private String currentStatus;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Temporal(TemporalType.DATE)
	@Column(name="date_hired", nullable=false)
	private Date dateHired;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Temporal(TemporalType.DATE)
	@Column(name="date_of_birth", nullable=false)
	private Date dateOfBirth;

	@Column(name="employee_active", nullable=false)
	private Boolean employeeActive;

	@Column(name="employee_email", nullable=false, length=60)
	private String employeeEmail;

	@Column(name="first_name", nullable=false, length=64)
	private String firstName;

	@Column(name="job_title", nullable=false, length=60)
	private String jobTitle;

	@Column(name="last_login")
	private Timestamp lastLogin;

	@Column(name="last_name", nullable=false, length=64)
	private String lastName;

	@Column(name="leave_days", nullable=false)
	private Integer leaveDays;

	@Column(name="login_attempts", nullable=false)
	private Integer loginAttempts;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(nullable=false, length=256)
	private String password;

	@Column(name="phone_number", nullable=false, length=20)
	private String phoneNumber;

	@Column(name="profile_picture_url", length=2147483647)
	private String profilePictureUrl;

	//bidirectional many-to-one association to JpaAnnouncement
	@OneToMany(mappedBy="employee")
	private Set<JpaAnnouncement> announcements;

	//bidirectional many-to-one association to JpaAnnouncementRecipient
	@OneToMany(mappedBy="employee")
	private Set<JpaAnnouncementRecipient> announcementsReceived;

	//bidirectional many-to-one association to JpaAttendance
	@OneToMany(mappedBy="employee")
	private Set<JpaAttendance> attendances;

	//bidirectional many-to-one association to JpaAuditLog
	@OneToMany(mappedBy="employee")
	private Set<JpaAuditLog> auditLogs;

	//bidirectional many-to-one association to JpaDepartmentHead
	@OneToMany(mappedBy="employee")
	private Set<JpaDepartmentHead> departmentHeads;

	//bidirectional many-to-one association to JpaEmergencyContact
	@OneToMany(mappedBy="employee")
	private Set<JpaEmergencyContact> emergencyContacts;

	//bidirectional many-to-one association to JpaDepartment
	@ManyToOne
	@JoinColumn(name="department_id")
	private JpaDepartment department;

	//bidirectional many-to-one association to JpaOption
	@ManyToOne
	@JoinColumn(name="option_value", nullable=false)
	private JpaOption countryOfOrigin;

	//bidirectional many-to-one association to JpaOption
	@ManyToOne
	@JoinColumn(name="option_value", nullable=false)
	private JpaOption employeeGender;

	//bidirectional many-to-one association to JpaOption
	@ManyToOne
	@JoinColumn(name="option_value")
	private JpaOption employeeMaritalStatus;

	//bidirectional many-to-one association to JpaOption
	@ManyToOne
	@JoinColumn(name="option_value")
	private JpaOption highestCertification;

	//bidirectional many-to-one association to JpaOption
	@ManyToOne
	@JoinColumn(name="option_value")
	private JpaOption stateOfOrigin;

	//bidirectional many-to-one association to JpaRole
	@ManyToOne
	@JoinColumn(name="role_id", nullable=false)
	private JpaRole role;

	//bidirectional many-to-one association to JpaEmployeeHasDocument
	@OneToMany(mappedBy="employee")
	private Set<JpaEmployeeHasDocument> employeeHasDocuments;

	//bidirectional many-to-one association to JpaEmployeeUpdateRequest
	@OneToMany(mappedBy="approver")
	private Set<JpaEmployeeUpdateRequest> employeeUpdateRequestsApprovedByMe;

	//bidirectional many-to-one association to JpaEmployeeUpdateRequest
	@OneToMany(mappedBy="employee")
	private Set<JpaEmployeeUpdateRequest> employeeUpdateRequests;

	//bidirectional many-to-one association to JpaLeaveRequest
	@OneToMany(mappedBy="employee")
	private Set<JpaLeaveRequest> leaveRequests;

	//bidirectional many-to-one association to JpaLeaveRequest
	@OneToMany(mappedBy="approver")
	private Set<JpaLeaveRequest> leaveRequestsApprovedByMe;

	//bidirectional many-to-one association to JpaNotification
	@OneToMany(mappedBy="receiver")
	private Set<JpaNotification> receivedNotifications;

	//bidirectional many-to-one association to JpaNotification
	@OneToMany(mappedBy="sender")
	private Set<JpaNotification> sentNotifications;

	public JpaEmployee() {
	}

	public String getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
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

	public String getCurrentStatus() {
		return this.currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public Timestamp getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateHired() {
		return this.dateHired;
	}

	public void setDateHired(Date dateHired) {
		this.dateHired = dateHired;
	}

	public Timestamp getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(Timestamp dateModified) {
		this.dateModified = dateModified;
	}

	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Boolean getEmployeeActive() {
		return this.employeeActive;
	}

	public void setEmployeeActive(Boolean employeeActive) {
		this.employeeActive = employeeActive;
	}

	public String getEmployeeEmail() {
		return this.employeeEmail;
	}

	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getJobTitle() {
		return this.jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public Timestamp getLastLogin() {
		return this.lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getLeaveDays() {
		return this.leaveDays;
	}

	public void setLeaveDays(Integer leaveDays) {
		this.leaveDays = leaveDays;
	}

	public Integer getLoginAttempts() {
		return this.loginAttempts;
	}

	public void setLoginAttempts(Integer loginAttempts) {
		this.loginAttempts = loginAttempts;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getProfilePictureUrl() {
		return this.profilePictureUrl;
	}

	public void setProfilePictureUrl(String profilePictureUrl) {
		this.profilePictureUrl = profilePictureUrl;
	}

	public Set<JpaAnnouncement> getAnnouncements() {
		return this.announcements;
	}

	public void setAnnouncements(Set<JpaAnnouncement> announcements) {
		this.announcements = announcements;
	}

	public JpaAnnouncement addAnnouncement(JpaAnnouncement announcement) {
		getAnnouncements().add(announcement);
		announcement.setEmployee(this);

		return announcement;
	}

	public JpaAnnouncement removeAnnouncement(JpaAnnouncement announcement) {
		getAnnouncements().remove(announcement);
		announcement.setEmployee(null);

		return announcement;
	}

	public Set<JpaAnnouncementRecipient> getAnnouncementsReceived() {
		return this.announcementsReceived;
	}

	public void setAnnouncementsReceived(Set<JpaAnnouncementRecipient> announcementsReceived) {
		this.announcementsReceived = announcementsReceived;
	}

	public JpaAnnouncementRecipient addAnnouncementReceived(JpaAnnouncementRecipient announcementReceived) {
		getAnnouncementsReceived().add(announcementReceived);
		announcementReceived.setEmployee(this);

		return announcementReceived;
	}

	public JpaAnnouncementRecipient removeAnnouncementReceived(JpaAnnouncementRecipient announcementReceived) {
		getAnnouncementsReceived().remove(announcementReceived);
		announcementReceived.setEmployee(null);

		return announcementReceived;
	}

	public Set<JpaAttendance> getAttendances() {
		return this.attendances;
	}

	public void setAttendances(Set<JpaAttendance> attendances) {
		this.attendances = attendances;
	}

	public JpaAttendance addAttendance(JpaAttendance attendance) {
		getAttendances().add(attendance);
		attendance.setEmployee(this);

		return attendance;
	}

	public JpaAttendance removeAttendance(JpaAttendance attendance) {
		getAttendances().remove(attendance);
		attendance.setEmployee(null);

		return attendance;
	}

	public Set<JpaAuditLog> getAuditLogs() {
		return this.auditLogs;
	}

	public void setAuditLogs(Set<JpaAuditLog> auditLogs) {
		this.auditLogs = auditLogs;
	}

	public JpaAuditLog addAuditLog(JpaAuditLog auditLog) {
		getAuditLogs().add(auditLog);
		auditLog.setEmployee(this);

		return auditLog;
	}

	public JpaAuditLog removeAuditLog(JpaAuditLog auditLog) {
		getAuditLogs().remove(auditLog);
		auditLog.setEmployee(null);

		return auditLog;
	}

	public Set<JpaDepartmentHead> getDepartmentHeads() {
		return this.departmentHeads;
	}

	public void setDepartmentHeads(Set<JpaDepartmentHead> departmentHeads) {
		this.departmentHeads = departmentHeads;
	}

	public JpaDepartmentHead addDepartmentHead(JpaDepartmentHead departmentHead) {
		getDepartmentHeads().add(departmentHead);
		departmentHead.setEmployee(this);

		return departmentHead;
	}

	public JpaDepartmentHead removeDepartmentHead(JpaDepartmentHead departmentHead) {
		getDepartmentHeads().remove(departmentHead);
		departmentHead.setEmployee(null);

		return departmentHead;
	}

	public Set<JpaEmergencyContact> getEmergencyContacts() {
		return this.emergencyContacts;
	}

	public void setEmergencyContacts(Set<JpaEmergencyContact> emergencyContacts) {
		this.emergencyContacts = emergencyContacts;
	}

	public JpaEmergencyContact addEmergencyContact(JpaEmergencyContact emergencyContact) {
		getEmergencyContacts().add(emergencyContact);
		emergencyContact.setEmployee(this);

		return emergencyContact;
	}

	public JpaEmergencyContact removeEmergencyContact(JpaEmergencyContact emergencyContact) {
		getEmergencyContacts().remove(emergencyContact);
		emergencyContact.setEmployee(null);

		return emergencyContact;
	}

	public JpaDepartment getDepartment() {
		return this.department;
	}

	public void setDepartment(JpaDepartment department) {
		this.department = department;
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

	public JpaRole getRole() {
		return this.role;
	}

	public void setRole(JpaRole role) {
		this.role = role;
	}

	public Set<JpaEmployeeHasDocument> getEmployeeHasDocuments() {
		return this.employeeHasDocuments;
	}

	public void setEmployeeHasDocuments(Set<JpaEmployeeHasDocument> employeeHasDocuments) {
		this.employeeHasDocuments = employeeHasDocuments;
	}

	public JpaEmployeeHasDocument addEmployeeHasDocument(JpaEmployeeHasDocument employeeHasDocument) {
		getEmployeeHasDocuments().add(employeeHasDocument);
		employeeHasDocument.setEmployee(this);

		return employeeHasDocument;
	}

	public JpaEmployeeHasDocument removeEmployeeHasDocument(JpaEmployeeHasDocument employeeHasDocument) {
		getEmployeeHasDocuments().remove(employeeHasDocument);
		employeeHasDocument.setEmployee(null);

		return employeeHasDocument;
	}

	public Set<JpaEmployeeUpdateRequest> getEmployeeUpdateRequestsApprovedByMe() {
		return employeeUpdateRequestsApprovedByMe;
	}

	public void setEmployeeUpdateRequestsApprovedByMe(Set<JpaEmployeeUpdateRequest> employeeUpdateRequestsApprovedByMe) {
		this.employeeUpdateRequestsApprovedByMe = employeeUpdateRequestsApprovedByMe;
	}

	public JpaEmployeeUpdateRequest addEmployeeUpdateRequestsApprovedByMe(JpaEmployeeUpdateRequest employeeUpdateRequest) {
		getEmployeeUpdateRequestsApprovedByMe().add(employeeUpdateRequest);
		employeeUpdateRequest.setApprover(this);

		return employeeUpdateRequest;
	}

	public JpaEmployeeUpdateRequest removeEmployeeUpdateRequestsApprovedByMe(JpaEmployeeUpdateRequest employeeUpdateRequest) {
		getEmployeeUpdateRequestsApprovedByMe().remove(employeeUpdateRequest);
		employeeUpdateRequest.setApprover(null);

		return employeeUpdateRequest;
	}

	public Set<JpaEmployeeUpdateRequest> getEmployeeUpdateRequests() {
		return employeeUpdateRequests;
	}

	public void setEmployeeUpdateRequests(Set<JpaEmployeeUpdateRequest> employeeUpdateRequests) {
		this.employeeUpdateRequests = employeeUpdateRequests;
	}

	public JpaEmployeeUpdateRequest addEmployeeUpdateRequests(JpaEmployeeUpdateRequest employeeUpdateRequest) {
		getEmployeeUpdateRequests().add(employeeUpdateRequest);
		employeeUpdateRequest.setEmployee(this);

		return employeeUpdateRequest;
	}

	public JpaEmployeeUpdateRequest removeEmployeeUpdateRequests(JpaEmployeeUpdateRequest employeeUpdateRequest) {
		getEmployeeUpdateRequests().remove(employeeUpdateRequest);
		employeeUpdateRequest.setEmployee(null);

		return employeeUpdateRequest;
	}

	public Set<JpaLeaveRequest> getLeaveRequests() {
		return this.leaveRequests;
	}

	public void setLeaveRequests(Set<JpaLeaveRequest> leaveRequests) {
		this.leaveRequests = leaveRequests;
	}

	public JpaLeaveRequest addLeaveRequest(JpaLeaveRequest leaveRequest) {
		getLeaveRequests().add(leaveRequest);
		leaveRequest.setEmployee(this);

		return leaveRequest;
	}

	public JpaLeaveRequest removeLeaveRequest(JpaLeaveRequest leaveRequest) {
		getLeaveRequests().remove(leaveRequest);
		leaveRequest.setEmployee(null);

		return leaveRequest;
	}

	public Set<JpaLeaveRequest> getLeaveRequestsApprovedByMe() {
		return this.leaveRequestsApprovedByMe;
	}

	public void setLeaveRequestsApprovedByMe(Set<JpaLeaveRequest> leaveRequestsApprovedByMe) {
		this.leaveRequestsApprovedByMe = leaveRequestsApprovedByMe;
	}

	public JpaLeaveRequest addLeaveRequestApprovedByMe(JpaLeaveRequest leaveRequest) {
		getLeaveRequestsApprovedByMe().add(leaveRequest);
		leaveRequest.setApprover(this);

		return leaveRequest;
	}

	public JpaLeaveRequest removeLeaveRequestApprovedByMe(JpaLeaveRequest leaveRequest) {
		getLeaveRequestsApprovedByMe().remove(leaveRequest);
		leaveRequest.setApprover(null);

		return leaveRequest;
	}

	public Set<JpaNotification> getReceivedNotifications() {
		return this.receivedNotifications;
	}

	public void setReceivedNotifications(Set<JpaNotification> notifications) {
		this.receivedNotifications = notifications;
	}

	public JpaNotification addReceivedNotification(JpaNotification notification) {
		getReceivedNotifications().add(notification);
		notification.setReceiver(this);

		return notification;
	}

	public JpaNotification removeReceivedNotification(JpaNotification notification) {
		getReceivedNotifications().remove(notification);
		notification.setReceiver(null);

		return notification;
	}

	public Set<JpaNotification> getSentNotifications() {
		return this.sentNotifications;
	}

	public void setSentNotifications(Set<JpaNotification> notifications) {
		this.sentNotifications = notifications;
	}

	public JpaNotification addSentNotification(JpaNotification notification) {
		getSentNotifications().add(notification);
		notification.setSender(this);

		return notification;
	}

	public JpaNotification removeSentNotification(JpaNotification notification) {
		getSentNotifications().remove(notification);
		notification.setSender(null);

		return notification;
	}

	@Override
	public int hashCode() {
		if (this.getEmployeeId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getEmployeeId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		JpaEmployee employee = (JpaEmployee) obj;
		return Objects.equals(this.getEmployeeId(), employee.getEmployeeId());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("FirstName", this.getFirstName())
				.add("LastName", this.getLastName())
				.add("Email", this.getEmployeeEmail())
				.add("DateHired", this.getDateHired())
				.add("Status", this.getCurrentStatus())
				.toString();
	}

}