package com.esl.internship.staffsync.event.management.impl;


import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.entities.JpaEvent;
import com.esl.internship.staffsync.entities.QJpaEvent;
import com.esl.internship.staffsync.event.management.api.IEventApi;
import com.esl.internship.staffsync.event.management.dto.EventDTO;
import com.esl.internship.staffsync.event.management.model.Event;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.esl.internship.staffsync.commons.util.Utility.stringifyEmployee;
import static com.esl.internship.staffsync.event.management.model.EventMapper.INSTANCE;


public class DefaultEventApiImpl implements IEventApi {

    @Inject
    JPAApi jpaApi;

    private static final QJpaEvent qJpaEvent = QJpaEvent.jpaEvent;

    /**
     * @author ALIU
     * @dateCreated 03/08/2023
     * @description Adds a new Event
     *
     * @param event event data to be added
     * @param employee The employee adding this record
     *
     * @return Event
     */

    @Override
    public Event addEvent(EventDTO eventDto, Employee employee) {
        final JpaEvent jpaEvent = INSTANCE.mapEventDto(eventDto);
        jpaEvent.setEventId(UUID.randomUUID().toString());
        jpaEvent.setCreatedBy(stringifyEmployee(employee));
        jpaEvent.setDateCreated(Timestamp.from(Instant.now()));

        jpaApi.em().persist(jpaEvent);

        return INSTANCE.mapEvent(jpaEvent);
    }

    /**
     * @author ALIU
     * @dateCreated 03/08/2023
     * @description Gets event by Id
     *
     * @param eventId Id of the event
     *
     * @return Optional<Event>
     */
    @Override
    public Optional<Event> getEventById(String eventId) {
        final var jpaEvent = new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEvent)
                .where(qJpaEvent.eventId.eq(eventId))
                .fetchOne();
        return Optional.ofNullable(INSTANCE.mapEvent(jpaEvent));
    }

    /**
     * @author ALIU
     * @dateCreated 03/08/2023
     * @description Gets event by Title
     *
     * @param eventTitle Title of the event
     *
     * @return Optional<Event>
     */
    public Optional<Event> getEventByTitle(String eventTitle) {
        final var jpaEvent = new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEvent)
                .where(qJpaEvent.eventTitle.eq(eventTitle))
                .fetchOne();
        return Optional.ofNullable(INSTANCE.mapEvent(jpaEvent));
    }

    /**
     * @author ALIU
     * @dateCreated 03/08/2023
     * @description Gets all events
     *
     * @return List<Event>
     */
    @Override
    public List<Event> getAllEvents() {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEvent)
                .fetch()
                .stream()
                .map(INSTANCE::mapEvent)
                .collect(Collectors.toList());
    }

    /**
     * @author ALIU
     * @dateCreated 03/08/2023
     * @description Gets event between two dates interval
     *
     * @param startDate Start date
     * @param endDate End date
     *
     * @return List<Event>
     */
    @Override
    public List<Event> getEventsBetweenDate(Date startDate, Date endDate) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        return queryFactory.selectFrom(qJpaEvent)
                .where(qJpaEvent.startDate.between(startDate, endDate))
                .fetch()
                .stream()
                .map(INSTANCE::mapEvent)
                .collect(Collectors.toList());
    }

    /**
     * @author ALIU
     * @dateCreated 03/08/2023
     * @description Updates an  event
     *
     * @param eventId Id of the event
     * @param event Event data to update
     * @param employee Employee updating the record
     *
     * @return boolean
     */

    @Override
    public boolean updateEvent(String eventId, EventDTO eventDto, Employee employee) {
        return new JPAQueryFactory(jpaApi.em()).update(qJpaEvent)
                .set(qJpaEvent.eventTitle, eventDto.getEventTitle())
                .set(qJpaEvent.eventDescription, eventDto.getEventDescription())
                .set(qJpaEvent.eventStatus, eventDto.getEventStatus())
                .set(qJpaEvent.startDate, eventDto.getStartDate())
                .set(qJpaEvent.endDate, eventDto.getEndDate())
                .set(qJpaEvent.dateModified, Timestamp.from(Instant.now()))
                .set(qJpaEvent.modifiedBy, stringifyEmployee(employee, "Updated event"))
                .where(qJpaEvent.eventId.eq(eventId))
                .execute() == 1;
    }

    /**
     * @author ALIU
     * @dateCreated 03/08/2023
     * @description Deletes an event
     *
     * @param eventId Id of the event
     *
     * @return boolean
     */
    @Override
    public boolean deleteEvent(String eventId) {
        return new JPAQueryFactory(jpaApi.em()).delete(qJpaEvent)
                .where(qJpaEvent.eventId.eq(eventId))
                .execute() == 1;
    }
}
