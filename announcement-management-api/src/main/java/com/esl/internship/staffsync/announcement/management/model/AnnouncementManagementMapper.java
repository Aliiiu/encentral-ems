package com.esl.internship.staffsync.announcement.management.model;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnnouncementManagementMapper {

    AnnouncementManagementMapper INSTANCE = Mappers.getMapper(AnnouncementManagementMapper.class);
}
