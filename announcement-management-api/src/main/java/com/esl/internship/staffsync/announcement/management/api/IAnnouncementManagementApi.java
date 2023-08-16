package com.esl.internship.staffsync.announcement.management.api;

import com.esl.internship.staffsync.announcement.management.dto.CreateInstantAnnouncementDTO;
import com.esl.internship.staffsync.announcement.management.dto.CreateScheduledAnnouncementDTO;
import com.esl.internship.staffsync.announcement.management.model.Announcement;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;

import java.util.List;
import java.util.Optional;

public interface IAnnouncementManagementApi {

    Response<Announcement> createInstantAnnouncement(String senderEmployeeId, CreateInstantAnnouncementDTO createInstantAnnouncementDTO, Employee employee);

    Response<Announcement> createScheduledAnnouncement(String senderEmployeeId, CreateScheduledAnnouncementDTO createScheduledAnnouncementDTO, Employee employee);

    Optional<Announcement> getAnnouncementRecordById(String announcementId);

    List<Announcement> getAllAnnouncementRecords();

    boolean updateAnAnnouncement(String senderEmployeeId, String announcementId, Employee employee);

    boolean deleteAnAnnouncementRecord(String announcementId);

}
