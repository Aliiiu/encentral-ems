package com.esl.internship.staffsync.system.configuration.dto;

import com.esl.internship.staffsync.entities.enums.NotificationPriority;

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

    private String subject="";

    private String object = "";

    @NotNull(message = "Notification template id field cannot be null")
    private String notificationTemplateBeanId;

    public CreateNotificationDTO() {
    }

    public CreateNotificationDTO(NotificationPriority priority, String receiverId, String senderId, String subject, String object, String notificationTemplateBeanId) {
        this.priority = priority;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.subject = subject;
        this.object = object;
        this.notificationTemplateBeanId = notificationTemplateBeanId;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getNotificationTemplateBeanId() {
        return notificationTemplateBeanId;
    }

    public void setNotificationTemplateBeanId(String notificationTemplateBeanId) {
        this.notificationTemplateBeanId = notificationTemplateBeanId;
    }
}