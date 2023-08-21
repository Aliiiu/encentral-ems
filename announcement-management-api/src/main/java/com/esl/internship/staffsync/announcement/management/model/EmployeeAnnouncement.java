package com.esl.internship.staffsync.announcement.management.model;

import com.esl.internship.staffsync.entities.attribute.converter.NotificationStatusConverter;
import com.esl.internship.staffsync.entities.enums.NotificationStatus;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Date;

public class EmployeeAnnouncement {

    private String announcementRecipientId;

    private String announcementMessage;

    private String announcementTitle;

    private Date deliveryDate;

    private Timestamp dateRead;

    private NotificationStatus status;

    private String announcer;

    public String getAnnouncementRecipientId() {
        return announcementRecipientId;
    }

    public void setAnnouncementRecipientId(String announcementRecipientId) {
        this.announcementRecipientId = announcementRecipientId;
    }

    public String getAnnouncementMessage() {
        return announcementMessage;
    }

    public void setAnnouncementMessage(String announcementMessage) {
        this.announcementMessage = announcementMessage;
    }

    public String getAnnouncementTitle() {
        return announcementTitle;
    }

    public void setAnnouncementTitle(String announcementTitle) {
        this.announcementTitle = announcementTitle;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Timestamp getDateRead() {
        return dateRead;
    }

    public void setDateRead(Timestamp dateRead) {
        this.dateRead = dateRead;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public String getAnnouncer() {
        return announcer;
    }

    public void setAnnouncer(String announcer) {
        this.announcer = announcer;
    }
}
