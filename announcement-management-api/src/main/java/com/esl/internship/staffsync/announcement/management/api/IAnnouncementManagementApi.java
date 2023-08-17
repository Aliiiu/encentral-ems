package com.esl.internship.staffsync.announcement.management.api;

import com.esl.internship.staffsync.announcement.management.dto.AnnouncementDTO;
import com.esl.internship.staffsync.announcement.management.model.Announcement;
import com.esl.internship.staffsync.announcement.management.model.EmployeeAnnouncement;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;

import java.util.List;
import java.util.Optional;

public interface IAnnouncementManagementApi {

    Response<Announcement> createAnnouncement(String senderEmployeeId, AnnouncementDTO announcementDTO, Employee employee);

    Optional<Announcement> getAnnouncementRecordById(String announcementId);

    List<Announcement> getAllAnnouncementRecords();

    boolean updateAnAnnouncement(String announcementId, AnnouncementDTO announcementDTO, Employee employee);

    boolean deleteAnAnnouncementRecord(String announcementId);

    List<EmployeeAnnouncement> getAllEmployeeAnnouncementsOrderedByDate(String employeeId);

    List<EmployeeAnnouncement> getAllUnreadEmployeeAnnouncementsOrderedByDate(String employeeId);

    boolean markAnnouncementAsRead(String announcementId);

    public boolean markAllAnnouncementsForEmployeeAsRead(String employeeId);

    boolean markAnnouncementAsUnRead(String announcementId);


}
