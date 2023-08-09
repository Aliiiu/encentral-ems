package com.esl.internship.staffsync.system.configuration.dto;

import com.encentral.staffsync.entity.enums.NotificationPriority;

import javax.validation.constraints.NotNull;

/**
 * @author Demilade
 * @dateCreated 01/08/2023
 * @description DTO class for creating notifications
 */
public class CreateNotificationDTO {

    @NotNull(message = "Notification priority cannot be null")
    private NotificationPriority priority;

    @NotNull(message = "Receiver id field cannot be null")
    private String receiverId;

    @NotNull(message = "Sender id field cannot be null")
    private String senderId;

    @NotNull(message = "Notification template id field cannot be null")
    private String notificationTemplateBeanId;

    public CreateNotificationDTO() {
    }

    public NotificationPriority getPriority() {
        return priority;
    }

    public void setPriority(NotificationPriority priority) {
        this.priority = priority;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getNotificationTemplateBeanId() {
        return notificationTemplateBeanId;
    }

    public void setNotificationTemplateBeanId(String notificationTemplateBeanId) {
        this.notificationTemplateBeanId = notificationTemplateBeanId;
    }
}