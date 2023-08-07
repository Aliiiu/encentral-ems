package com.esl.internship.staffsync.system.configuration.model;

import com.encentral.staffsync.entity.enums.NotificationPriority;

import java.sql.Timestamp;

public class Notification {
    private String notificationId;

    private String createdBy;

    private Timestamp dateCreated;

    private Timestamp dateModified;

    private Timestamp dateRead;

    private String deliveryStatus;

    private String modifiedBy;

    private String notificationMessage;

    private String notificationTitle;

    private NotificationPriority priority;

    private NotificationEmployee receiver;

    private NotificationEmployee sender;

    private String notificationTemplateBeanId;


    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
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

    public Timestamp getDateRead() {
        return dateRead;
    }

    public void setDateRead(Timestamp dateRead) {
        this.dateRead = dateRead;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getNotificationTitle() {
        return notificationTitle;
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

    public NotificationEmployee getReceiver() {
        return receiver;
    }

    public void setReceiver(NotificationEmployee receiver) {
        this.receiver = receiver;
    }

    public NotificationEmployee getSender() {
        return sender;
    }

    public void setSender(NotificationEmployee sender) {
        this.sender = sender;
    }

    public String getNotificationTemplateBeanId() {
        return notificationTemplateBeanId;
    }

    public void setNotificationTemplateBeanId(String notificationTemplateBeanId) {
        this.notificationTemplateBeanId = notificationTemplateBeanId;
    }
}