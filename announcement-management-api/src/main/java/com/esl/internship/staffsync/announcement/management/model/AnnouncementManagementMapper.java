package com.esl.internship.staffsync.announcement.management.model;


import com.esl.internship.staffsync.announcement.management.dto.CreateInstantAnnouncementDTO;
import com.esl.internship.staffsync.announcement.management.dto.CreateScheduledAnnouncementDTO;
import com.esl.internship.staffsync.entities.JpaAnnouncement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnnouncementManagementMapper {

    AnnouncementManagementMapper INSTANCE = Mappers.getMapper(AnnouncementManagementMapper.class);

    JpaAnnouncement mapAnnouncement(CreateInstantAnnouncementDTO modelDto);

    JpaAnnouncement mapAnnouncement(CreateScheduledAnnouncementDTO modelDto);

    @Mappings({
            @Mapping(target = "senderEmployeeId", source = "sender.employeeId")
    })
    Announcement mapAnnouncement(JpaAnnouncement entity);
}
