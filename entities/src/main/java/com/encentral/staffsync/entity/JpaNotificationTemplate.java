package com.encentral.staffsync.entity;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;


/**
 * The persistent class for the notification_template database table.
 * 
 */
@Entity
@Table(name="notification_template")
public class JpaNotificationTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="notification_template_id", unique=true, nullable=false, length=64)
	private String notificationTemplateId;

	@Column(name="created_by", nullable=false)
	private String createdBy;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="notification_description", length=2147483647)
	private String notificationDescription;

	@Column(name="notification_template_content", nullable=false, length=2147483647)
	private String notificationTemplateContent;

	@Column(name="notification_template_name", nullable=false, length=64)
	private String notificationTemplateName;

	//bidirectional many-to-one association to JpaNotification
	@OneToMany(mappedBy="notificationTemplateBean")
	private Set<JpaNotification> notifications;

	public JpaNotificationTemplate() {
	}

	public String getNotificationTemplateId() {
		return this.notificationTemplateId;
	}

	public void setNotificationTemplateId(String notificationTemplateId) {
		this.notificationTemplateId = notificationTemplateId;
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

	public String getNotificationDescription() {
		return this.notificationDescription;
	}

	public void setNotificationDescription(String notificationDescription) {
		this.notificationDescription = notificationDescription;
	}

	public String getNotificationTemplateContent() {
		return this.notificationTemplateContent;
	}

	public void setNotificationTemplateContent(String notificationTemplateContent) {
		this.notificationTemplateContent = notificationTemplateContent;
	}

	public String getNotificationTemplateName() {
		return this.notificationTemplateName;
	}

	public void setNotificationTemplateName(String notificationTemplateName) {
		this.notificationTemplateName = notificationTemplateName;
	}

	public Set<JpaNotification> getNotifications() {
		return this.notifications;
	}

	public void setNotifications(Set<JpaNotification> notifications) {
		this.notifications = notifications;
	}

	public JpaNotification addNotification(JpaNotification notification) {
		getNotifications().add(notification);
		notification.setNotificationTemplateBean(this);

		return notification;
	}

	public JpaNotification removeNotification(JpaNotification notification) {
		getNotifications().remove(notification);
		notification.setNotificationTemplateBean(null);

		return notification;
	}

	@Override
	public int hashCode() {
		if (this.getNotificationTemplateId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getNotificationTemplateId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || this.getClass() != obj.getClass()) return false;
		JpaNotificationTemplate notificationTemplate = (JpaNotificationTemplate) obj;
		return Objects.equals(this.getNotificationTemplateId(), notificationTemplate.getNotificationTemplateId());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("TemplateName", this.getNotificationTemplateName())
				.toString();
	}

}