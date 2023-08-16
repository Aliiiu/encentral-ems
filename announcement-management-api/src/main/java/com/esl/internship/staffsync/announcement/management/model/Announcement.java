package com.esl.internship.staffsync.announcement.management.model;

import com.esl.internship.staffsync.entities.enums.NotificationPriority;

import java.sql.Timestamp;
import java.util.Date;

public class Announcement {

    private String announcementId;

    private String announcementMessage;

    private String announcementTitle;

    private String announcementFor;

    private String createdBy;

    private Timestamp dateCreated;

    private Timestamp dateModified;

    private Date deliveryDate;

    private String modifiedBy;

    private String senderEmployeeId;

}
