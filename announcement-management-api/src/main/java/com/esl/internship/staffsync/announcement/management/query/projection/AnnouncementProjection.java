package com.esl.internship.staffsync.announcement.management.query.projection;

import com.esl.internship.staffsync.announcement.management.model.EmployeeAnnouncement;
import com.esl.internship.staffsync.entities.JpaAnnouncement;
import com.esl.internship.staffsync.entities.JpaAnnouncementRecipient;

import static com.esl.internship.staffsync.entities.stringify.Stringifier.stringifyJpaEmployee;

public class AnnouncementProjection {
    private JpaAnnouncement announcement;
    private JpaAnnouncementRecipient announcementRecipient;

    public JpaAnnouncement getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(JpaAnnouncement announcement) {
        this.announcement = announcement;
    }

    public JpaAnnouncementRecipient getAnnouncementRecipient() {
        return announcementRecipient;
    }

    public void setAnnouncementRecipient(JpaAnnouncementRecipient announcementRecipient) {
        this.announcementRecipient = announcementRecipient;
    }

    public EmployeeAnnouncement mapEmployeeAnnouncement() {
        EmployeeAnnouncement employeeAnnouncement = new EmployeeAnnouncement();

        employeeAnnouncement.setAnnouncementMessage(this.announcement.getAnnouncementMessage());
        employeeAnnouncement.setAnnouncementTitle(this.announcement.getAnnouncementTitle());
        employeeAnnouncement.setStatus(this.announcementRecipient.getStatus());
        employeeAnnouncement.setDeliveryDate(this.announcement.getDeliveryDate());
        employeeAnnouncement.setAnnouncementRecipientId(this.announcementRecipient.getAnnouncementRecipientId());
        employeeAnnouncement.setDateRead(this.announcementRecipient.getDateRead());
        employeeAnnouncement.setAnnouncer(stringifyJpaEmployee(this.announcement.getSender()));

        return employeeAnnouncement;
    }

    public static EmployeeAnnouncement mapEmployeeAnnouncement(JpaAnnouncementRecipient announcementRecipient) {
        AnnouncementProjection p = new AnnouncementProjection();
        p.setAnnouncement(announcementRecipient.getAnnouncement());
        p.setAnnouncementRecipient(announcementRecipient);
        return p.mapEmployeeAnnouncement();
    }
}
