package com.esl.internship.staffsync.announcement.management.impl;

import com.esl.internship.staffsync.announcement.management.api.IAnnouncementManagementApi;
import com.esl.internship.staffsync.announcement.management.dto.CreateInstantAnnouncementDTO;
import com.esl.internship.staffsync.announcement.management.dto.CreateScheduledAnnouncementDTO;
import com.esl.internship.staffsync.announcement.management.model.Announcement;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.entities.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.esl.internship.staffsync.announcement.management.model.AnnouncementManagementMapper.INSTANCE;
import static com.esl.internship.staffsync.commons.util.Utility.stringifyEmployee;

public class DefaultAnnouncementManagementApiImpl implements IAnnouncementManagementApi {

    @Inject
    JPAApi jpaApi;

    private static final QJpaAnnouncement qJpaAnnouncement = QJpaAnnouncement.jpaAnnouncement;

    private static final QJpaEmployee qJpaEmployee = QJpaEmployee.jpaEmployee;

    private static final QJpaAnnouncementRecipient qJpaAnnouncementRecipient = QJpaAnnouncementRecipient.jpaAnnouncementRecipient;


    @Override
    public Announcement createInstantAnnouncement(CreateInstantAnnouncementDTO createInstantAnnouncementDTO, Employee employee) {
        JpaAnnouncement jpaAnnouncement = INSTANCE.mapAnnouncement(createInstantAnnouncementDTO);
        jpaAnnouncement.setAnnouncementId(UUID.randomUUID().toString());
        jpaAnnouncement.setDateCreated(Timestamp.from(Instant.now()));
        jpaAnnouncement.setDeliveryDate(Timestamp.from(Instant.now()));
        jpaAnnouncement.setCreatedBy(stringifyEmployee(employee));
        jpaAnnouncement.setSender();
        return null;
    }

    @Override
    public Announcement createScheduledAnnouncement(CreateScheduledAnnouncementDTO createScheduledAnnouncementDTO, Employee employee) {
        return null;
    }

    @Override
    public Optional<Announcement> getAnnouncementRecordById(String announcementId) {
        return Optional.empty();
    }

    @Override
    public List<Announcement> getAllAnnouncementRecords() {
        return null;
    }

    @Override
    public boolean updateAnAnnouncement(String announcementId, Employee employee) {
        return false;
    }

    @Override
    public boolean deleteAnAnnouncementRecord(String announcementId) {
        return false;
    }
    /**
     * @author WARITH
     * @dateCreated 15/08/2023
     * @description A helper method to fetch an employee record from the database
     *
     * @param employeeId ID of the employee to fetch
     *
     * @return JpaEmployee An employee record or null if not found
     */
    private JpaEmployee getJpaEmployee(String employeeId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .fetchOne();
    }
}
