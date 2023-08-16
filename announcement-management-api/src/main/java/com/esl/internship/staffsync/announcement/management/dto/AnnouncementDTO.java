package com.esl.internship.staffsync.announcement.management.dto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class AnnouncementDTO {

    @NotNull(message = "A title is required")
    private String announcementTitle;

    private String announcementMessage;

    @NotNull(message = "Recipient department must be specified")
    private String announcementFor;

    @Future(message = "Date must be in the future")
    private Date deliveryDate;

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

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
