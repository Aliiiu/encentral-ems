package com.esl.internship.staffsync.system.configuration.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Demilade
 * @dateCreated 01/08/2023
 * @description DTO class for creating notification templates
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateNotificationTemplateDTO {

    private String notificationDescription;

    private String notificationTemplateContent;

    private String notificationTemplateName;

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
}
