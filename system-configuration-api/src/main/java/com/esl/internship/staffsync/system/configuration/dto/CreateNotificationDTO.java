package com.esl.internship.staffsync.system.configuration.dto;

import com.encentral.staffsync.entity.enums.NotificationPriority;

/**
 * @author Demilade
 * @dateCreated 01/08/2023
 * @description DTO class for creating notifications
 */
public class CreateNotificationDTO {
    private NotificationPriority priority;
    private String receiverId;
    private String senderId;
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