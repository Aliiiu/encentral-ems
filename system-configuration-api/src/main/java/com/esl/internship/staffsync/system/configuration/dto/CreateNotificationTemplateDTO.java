package com.esl.internship.staffsync.system.configuration.dto;

/**
 * @author Demilade
 * @dateCreated 01/08/2023
 * @description DTO class for creating notification templates
 */
public class CreateNotificationTemplateDTO {

    private String notificationDescription;

    private String notificationTemplateContent;

    private String notificationTemplateName;

    public CreateNotificationTemplateDTO() {
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
}
