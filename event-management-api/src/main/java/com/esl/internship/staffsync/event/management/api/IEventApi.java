package com.esl.internship.staffsync.event.management.api;


import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.event.management.dto.EventDTO;
import com.esl.internship.staffsync.event.management.model.Event;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IEventApi {

    Event addEvent(EventDTO eventDto, Employee employee);

    Optional<Event> getEventById(String eventId);

    List<Event> getAllEvents();

    List<Event> getEventsBetweenDate(Date startDate, Date endDate);

    boolean updateEvent(String eventId, EventDTO eventDto, Employee employee);

    boolean deleteEvent(String eventId);

}
