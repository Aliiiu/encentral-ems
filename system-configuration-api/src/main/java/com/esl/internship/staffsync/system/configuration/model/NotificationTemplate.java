package com.esl.internship.staffsync.system.configuration.model;

import java.sql.Timestamp;
import java.util.Set;

public class NotificationTemplate {
    private String notificationTemplateId;

    private String createdBy;

    private Timestamp dateCreated;

    private Timestamp dateModified;

    private String modifiedBy;

    private String notificationDescription;

    private String notificationTemplateContent;

    private String notificationTemplateName;

    private Set<String> notificationList;

    public String getNotificationTemplateId() {
        return notificationTemplateId;
    }

    public void setNotificationTemplateId(String notificationTemplateId) {
        this.notificationTemplateId = notificationTemplateId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateModified() {
        return dateModified;
    }

    public void setDateModified(Timestamp dateModified) {
        this.dateModified = dateModified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getNotificationDescription() {
        return notificationDescription;
    }

    public void setNotificationDescription(String notificationDescription) {
        this.notificationDescription = notificationDescription;
    }

    public String getNotificationTemplateContent() {
        return notificationTemplateContent;
    }

    public void setNotificationTemplateContent(String notificationTemplateContent) {
        this.notificationTemplateContent = notificationTemplateContent;
    }

    public String getNotificationTemplateName() {
        return notificationTemplateName;
    }

    public void setNotificationTemplateName(String notificationTemplateName) {
        this.notificationTemplateName = notificationTemplateName;
    }

    public Set<String> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(Set<String> notificationList) {
        this.notificationList = notificationList;
    }
}