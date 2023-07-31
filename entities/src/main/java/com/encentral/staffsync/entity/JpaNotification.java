package com.encentral.staffsync.entity;

import com.encentral.staffsync.entity.enums.NotificationPriority;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;


/**
 * The persistent class for the notification database table.
 * 
 */
@Entity
@Table(name="notification")
public class JpaNotification implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="notification_id", unique=true, nullable=false, length=64)
	private String notificationId;

	@Column(name="created_by", nullable=false)
	private String createdBy;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Column(name="date_read")
	private Timestamp dateRead;

	@Column(name="delivery_status", nullable=false, length=2147483647)
	private String deliveryStatus;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="notification_message", length=2147483647)
	private String notificationMessage;

	@Column(name="notification_title", nullable=false, length=64)
	private String notificationTitle;

	@Column(nullable=false, length=2147483647)
	@Enumerated(EnumType.STRING)
	private NotificationPriority priority;

	//bidirectional many-to-one association to JpaEmployee
	@ManyToOne
	@JoinColumn(name="receiver_id", nullable=false)
	private JpaEmployee receiver;

	//bidirectional many-to-one association to JpaEmployee
	@ManyToOne
	@JoinColumn(name="sender_id", nullable=false)
	private JpaEmployee sender;

	//bidirectional many-to-one association to JpaNotificationTemplate
	@ManyToOne
	@JoinColumn(name="notification_template", nullable=false)
	private JpaNotificationTemplate notificationTemplateBean;

	public JpaNotification() {
	}

	public String getNotificationId() {
		return this.notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
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

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getNotificationMessage() {
		return this.notificationMessage;
	}

	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}

	public String getNotificationTitle() {
		return this.notificationTitle;
	}

	public void setNotificationTitle(String notificationTitle) {
		this.notificationTitle = notificationTitle;
	}

	public NotificationPriority getPriority() {
		return priority;
	}

	public void setPriority(NotificationPriority priority) {
		this.priority = priority;
	}

	public JpaEmployee getReceiver() {
		return receiver;
	}

	public void setReceiver(JpaEmployee receiver) {
		this.receiver = receiver;
	}

	public JpaEmployee getSender() {
		return sender;
	}

	public void setSender(JpaEmployee sender) {
		this.sender = sender;
	}

	public JpaNotificationTemplate getNotificationTemplateBean() {
		return this.notificationTemplateBean;
	}

	public void setNotificationTemplateBean(JpaNotificationTemplate notificationTemplateBean) {
		this.notificationTemplateBean = notificationTemplateBean;
	}

	@Override
	public int hashCode() {
		if (this.getNotificationId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getNotificationId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || this.getClass() != obj.getClass()) return false;
		JpaNotification notification = (JpaNotification) obj;
		return Objects.equals(this.getNotificationId(), notification.getNotificationId());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("Title", this.getNotificationTitle())
				.add("Priority", this.getPriority())
				.add("SentBy", this.getSender())
				.toString();
	}

}