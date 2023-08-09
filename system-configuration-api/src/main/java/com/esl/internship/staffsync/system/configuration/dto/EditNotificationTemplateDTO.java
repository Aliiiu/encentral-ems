package com.esl.internship.staffsync.system.configuration.dto;

import javax.validation.constraints.NotNull;

/**
 * @author Demilade
 * @dateCreated 01/08/2023
 * @description DTO class for editing notification templates
 */
public class EditNotificationTemplateDTO {

    @NotNull
    private String notificationTemplateId;
    private String notificationDescription= "";

    private String notificationTemplateContent = "";

    private String notificationTemplateName = "";

    public EditNotificationTemplateDTO() {
    }

    public String getNotificationTemplateId() {
        return notificationTemplateId;
    }

    public void setNotificationTemplateId(String notificationTemplateId) {
        this.notificationTemplateId = notificationTemplateId;
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
