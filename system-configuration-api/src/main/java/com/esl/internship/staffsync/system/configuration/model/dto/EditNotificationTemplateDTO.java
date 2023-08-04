package com.esl.internship.staffsync.system.configuration.model.dto;

import java.util.Optional;

/**
 * @author Demilade
 * @dateCreated 01/08/2023
 * @description DTO class for editing notification templates
 */
public class EditNotificationTemplateDTO {

    private String notificationTemplateId;

    @com.typesafe.config.Optional
    private Optional<String>  notificationDescription;

    private Optional<String> notificationTemplateContent;

    private Optional<String>  notificationTemplateName;

    public String getNotificationTemplateId() {
        return notificationTemplateId;
    }

    public void setNotificationTemplateId(String notificationTemplateId) {
        this.notificationTemplateId = notificationTemplateId;
    }

    public Optional<String> getNotificationDescription() {
        return notificationDescription;
    }

    public void setNotificationDescription(Optional<String> notificationDescription) {
        this.notificationDescription = notificationDescription;
    }

    public Optional<String> getNotificationTemplateContent() {
        return notificationTemplateContent;
    }

    public void setNotificationTemplateContent(Optional<String> notificationTemplateContent) {
        this.notificationTemplateContent = notificationTemplateContent;
    }

    public Optional<String> getNotificationTemplateName() {
        return notificationTemplateName;
    }

    public void setNotificationTemplateName(Optional<String> notificationTemplateName) {
        this.notificationTemplateName = notificationTemplateName;
    }
}
