package com.esl.internship.staffsync.announcement.management.model;


import com.esl.internship.staffsync.announcement.management.dto.AnnouncementDTO;
import com.esl.internship.staffsync.entities.JpaAnnouncement;
import com.esl.internship.staffsync.entities.JpaEmployee;
import com.esl.internship.staffsync.entities.stringify.Stringifier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnnouncementManagementMapper {

    AnnouncementManagementMapper INSTANCE = Mappers.getMapper(AnnouncementManagementMapper.class);

    JpaAnnouncement mapAnnouncement(AnnouncementDTO modelDto);

    @Mappings({
            @Mapping(target = "announcer", source = "sender", qualifiedByName = "stringifyEntity")
    })
    Announcement mapAnnouncement(JpaAnnouncement entity);

    @Named("stringifyEntity")
    public static String stringifyEntity(JpaEmployee entity) {
        return Stringifier.stringifyJpaEmployee(entity);
    }

}
