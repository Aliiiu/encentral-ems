package com.encentral.staffsync.entity;

import com.encentral.staffsync.entity.enums.NotificationPriority;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;


/**
 * The persistent class for the announcement database table.
 * 
 */
@Entity
@Table(name="announcement")
public class JpaAnnouncement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="announcement_id", unique=true, nullable=false, length=64)
	private String announcementId;

	@Column(name="announcement_message", length=2147483647)
	private String announcementMessage;

	@Column(name="announcement_title", nullable=false, length=60)
	private String announcementTitle;

	@Column(name="created_by", nullable=false)
	private String createdBy;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Temporal(TemporalType.DATE)
	@Column(name="delivery_date")
	private Date deliveryDate;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private NotificationPriority priority;

	//bidirectional many-to-one association to JpaEmployee
	@ManyToOne
	@JoinColumn(name="sender_id", nullable=false)
	private JpaEmployee employee;

	//bidirectional many-to-one association to JpaAnnouncementRecipient
	@OneToMany(mappedBy="announcement")
	private Set<JpaAnnouncementRecipient> announcementRecipients;

	public JpaAnnouncement() {
	}

	public String getAnnouncementId() {
		return this.announcementId;
	}

	public void setAnnouncementId(String announcementId) {
		this.announcementId = announcementId;
	}

	public String getAnnouncementMessage() {
		return this.announcementMessage;
	}

	public void setAnnouncementMessage(String announcementMessage) {
		this.announcementMessage = announcementMessage;
	}

	public String getAnnouncementTitle() {
		return this.announcementTitle;
	}

	public void setAnnouncementTitle(String announcementTitle) {
		this.announcementTitle = announcementTitle;
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

	public Date getDeliveryDate() {
		return this.deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public NotificationPriority getPriority() {
		return this.priority;
	}

	public void setPriority(NotificationPriority priority) {
		this.priority = priority;
	}

	public JpaEmployee getEmployee() {
		return this.employee;
	}

	public void setEmployee(JpaEmployee employee) {
		this.employee = employee;
	}

	public Set<JpaAnnouncementRecipient> getAnnouncementRecipients() {
		return this.announcementRecipients;
	}

	public void setAnnouncementRecipients(Set<JpaAnnouncementRecipient> announcementRecipients) {
		this.announcementRecipients = announcementRecipients;
	}

	public JpaAnnouncementRecipient addAnnouncementRecipient(JpaAnnouncementRecipient announcementRecipient) {
		getAnnouncementRecipients().add(announcementRecipient);
		announcementRecipient.setAnnouncement(this);

		return announcementRecipient;
	}

	public JpaAnnouncementRecipient removeAnnouncementRecipient(JpaAnnouncementRecipient announcementRecipient) {
		getAnnouncementRecipients().remove(announcementRecipient);
		announcementRecipient.setAnnouncement(null);

		return announcementRecipient;
	}

	@Override
	public int hashCode() {
		if (this.getAnnouncementId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getAnnouncementId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || this.getClass() != obj.getClass()) return false;
		JpaAnnouncement announcement = (JpaAnnouncement) obj;
		return Objects.equals(this.getAnnouncementId(), announcement.getAnnouncementId());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("Title", this.getAnnouncementTitle())
				.add("Priority", this.getPriority())
				.add("Delivery Date", this.getDeliveryDate())
				.toString();
	}
}