package com.encentral.staffsync.entity;

import com.encentral.staffsync.entity.enums.NotificationStatus;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;


/**
 * The persistent class for the announcement_recipient database table.
 * 
 */
@Entity
@Table(name="announcement_recipient")
public class JpaAnnouncementRecipient implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="announcement_recipient_id", unique=true, nullable=false, length=64)
	private String announcementRecipientId;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Column(name="date_read")
	private Timestamp dateRead;

	@Column(name="delivery_status", nullable=false)
	private String deliveryStatus;

	//bidirectional many-to-one association to JpaAnnouncement
	@ManyToOne
	@JoinColumn(name="announcement_id", nullable=false)
	private JpaAnnouncement announcement;

	//bidirectional many-to-one association to JpaEmployee
	@ManyToOne
	@JoinColumn(name="employee_id", nullable=false)
	private JpaEmployee employee;

	public JpaAnnouncementRecipient() {
	}

	public String getAnnouncementRecipientId() {
		return this.announcementRecipientId;
	}

	public void setAnnouncementRecipientId(String announcementRecipientId) {
		this.announcementRecipientId = announcementRecipientId;
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

	public Timestamp getDateRead() {
		return this.dateRead;
	}

	public void setDateRead(Timestamp dateRead) {
		this.dateRead = dateRead;
	}

	public String getDeliveryStatus() {
		return this.deliveryStatus;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public JpaAnnouncement getAnnouncement() {
		return this.announcement;
	}

	public void setAnnouncement(JpaAnnouncement announcement) {
		this.announcement = announcement;
	}

	public JpaEmployee getEmployee() {
		return this.employee;
	}

	public void setEmployee(JpaEmployee employee) {
		this.employee = employee;
	}

	@Override
	public int hashCode() {
		if (this.getAnnouncementRecipientId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getAnnouncementRecipientId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || this.getClass() != obj.getClass()) return false;
		JpaAnnouncementRecipient announcementRecipient = (JpaAnnouncementRecipient) obj;
		return Objects.equals(this.getAnnouncementRecipientId(), announcementRecipient.getAnnouncementRecipientId());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("Announcement", this.getAnnouncement())
				.add("Recipient", this.getEmployee().getEmployeeEmail())
				.add("Delivery Status", this.getDeliveryStatus())
				.toString();
	}

}