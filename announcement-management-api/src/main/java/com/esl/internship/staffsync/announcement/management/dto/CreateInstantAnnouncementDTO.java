package com.esl.internship.staffsync.announcement.management.dto;

import com.esl.internship.staffsync.entities.enums.NotificationPriority;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;

public class CreateInstantAnnouncementDTO {

    @NotNull(message = "A title is required")
    private String announcementTitle;

    private String announcementMessage;

    @NotNull(message = "Recipient department must be specified")
    private String announcementFor;

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
}
