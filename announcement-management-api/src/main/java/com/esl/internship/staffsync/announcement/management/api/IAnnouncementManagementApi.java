package com.esl.internship.staffsync.announcement.management.api;

import com.esl.internship.staffsync.announcement.management.dto.CreateInstantAnnouncementDTO;
import com.esl.internship.staffsync.announcement.management.dto.CreateScheduledAnnouncementDTO;
import com.esl.internship.staffsync.announcement.management.model.Announcement;
import com.esl.internship.staffsync.commons.model.Employee;

import java.util.List;
import java.util.Optional;

public interface IAnnouncementManagementApi {

    Announcement createInstantAnnouncement(CreateInstantAnnouncementDTO createInstantAnnouncementDTO, Employee employee);

    Announcement createScheduledAnnouncement(CreateScheduledAnnouncementDTO createScheduledAnnouncementDTO, Employee employee);

    Optional<Announcement> getAnnouncementRecordById(String announcementId);

    List<Announcement> getAllAnnouncementRecords();

    boolean updateAnAnnouncement(String announcementId, Employee employee);

    boolean deleteAnAnnouncementRecord(String announcementId);

}
