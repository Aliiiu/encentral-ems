package com.esl.internship.staffsync.announcement.management.dto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;

public class AnnouncementDTO {

    @NotNull(message = "A title is required")
    private String announcementTitle;

    private String announcementMessage;

    @NotNull(message = "Recipient department must be specified")
    private String announcementFor;

    @Future(message = "Date must be in the future")
    private Timestamp deliveryDate;

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

    public String getAnnouncementFor() {
        return announcementFor;
    }

    public void setAnnouncementFor(String announcementFor) {
        this.announcementFor = announcementFor;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
