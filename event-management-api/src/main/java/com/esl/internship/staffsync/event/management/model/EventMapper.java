package com.esl.internship.staffsync.event.management.model;

import com.esl.internship.staffsync.entities.JpaEvent;
import com.esl.internship.staffsync.event.management.dto.EventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    JpaEvent mapEvent(Event model);

    Event mapEvent(JpaEvent entity);

    JpaEvent mapEventDto(EventDTO eventDto);

    EventDTO mapEventDto(JpaEvent jpaEvent);

}
