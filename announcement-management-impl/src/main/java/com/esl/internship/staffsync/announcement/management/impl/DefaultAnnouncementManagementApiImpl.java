package com.esl.internship.staffsync.announcement.management.impl;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.esl.internship.staffsync.announcement.management.api.IAnnouncementManagementApi;
import com.esl.internship.staffsync.announcement.management.dto.CreateInstantAnnouncementDTO;
import com.esl.internship.staffsync.announcement.management.dto.CreateScheduledAnnouncementDTO;
import com.esl.internship.staffsync.announcement.management.impl.actor.AnnouncementDispatchActor;
import com.esl.internship.staffsync.announcement.management.model.Announcement;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.entities.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.esl.internship.staffsync.announcement.management.model.AnnouncementManagementMapper.INSTANCE;
import static com.esl.internship.staffsync.commons.util.Utility.stringifyEmployee;

public class DefaultAnnouncementManagementApiImpl implements IAnnouncementManagementApi {

    @Inject
    JPAApi jpaApi;

    private static final QJpaAnnouncement qJpaAnnouncement = QJpaAnnouncement.jpaAnnouncement;

    private static final QJpaEmployee qJpaEmployee = QJpaEmployee.jpaEmployee;

    private static final QJpaDepartment qJpaDepartment = QJpaDepartment.jpaDepartment;

    private static final QJpaAnnouncementRecipient qJpaAnnouncementRecipient = QJpaAnnouncementRecipient.jpaAnnouncementRecipient;


    @Override
    public Response<Announcement> createInstantAnnouncement(String senderEmployeeId, CreateInstantAnnouncementDTO createInstantAnnouncementDTO, Employee employee) {

        Response<Announcement> response = new Response<>();

        JpaDepartment department = null;

        if (!createInstantAnnouncementDTO.getAnnouncementFor().equalsIgnoreCase("general")) {
            department =  getJpaDepartment(createInstantAnnouncementDTO.getAnnouncementFor());

            if (department == null) {
                response.putError("announcementFor", "Department not found");
                return response;
            }
        }

        JpaEmployee jpaEmployee = getJpaEmployee(senderEmployeeId);

        if (jpaEmployee == null) {
            response.putError("senderEmployeeId", "Employee not found");
            return response;
        }

        JpaAnnouncement jpaAnnouncement = INSTANCE.mapAnnouncement(createInstantAnnouncementDTO);
        jpaAnnouncement.setAnnouncementId(UUID.randomUUID().toString());
        jpaAnnouncement.setCreatedBy(stringifyEmployee(employee));
        jpaAnnouncement.setDateCreated(Timestamp.from(Instant.now()));
        jpaAnnouncement.setDeliveryDate(Timestamp.from(Instant.now()));
        jpaAnnouncement.setSender(jpaEmployee);

        jpaApi.em().persist(jpaAnnouncement);

        dispatchAnnouncementToRecipients(jpaAnnouncement, department);

        return response.setValue(INSTANCE.mapAnnouncement(jpaAnnouncement));
    }


    @Override
    public Response<Announcement> createScheduledAnnouncement(String senderEmployeeId, CreateScheduledAnnouncementDTO createScheduledAnnouncementDTO, Employee employee) {
        Response<Announcement> response = new Response<>();

        JpaDepartment department = null;

        if (!createScheduledAnnouncementDTO.getAnnouncementFor().equalsIgnoreCase("general")) {
            department =  getJpaDepartment(createScheduledAnnouncementDTO.getAnnouncementFor());

            if (department == null) {
                response.putError("announcementFor", "Department not found");
                return response;
            }
        }

        JpaEmployee jpaEmployee = getJpaEmployee(senderEmployeeId);

        if (jpaEmployee == null) {
            response.putError("senderEmployeeId", "Employee not found");
            return response;
        }

        JpaAnnouncement jpaAnnouncement = INSTANCE.mapAnnouncement(createScheduledAnnouncementDTO);
        jpaAnnouncement.setAnnouncementId(UUID.randomUUID().toString());
        jpaAnnouncement.setCreatedBy(stringifyEmployee(employee));
        jpaAnnouncement.setDateCreated(Timestamp.from(Instant.now()));
        jpaAnnouncement.setDeliveryDate(createScheduledAnnouncementDTO.getDeliveryDate());
        jpaAnnouncement.setSender(jpaEmployee);

        jpaApi.em().persist(jpaAnnouncement);

        dispatchAnnouncementToRecipients(jpaAnnouncement, department);

        return response.setValue(INSTANCE.mapAnnouncement(jpaAnnouncement));
    }

    @Override
    public Optional<Announcement> getAnnouncementRecordById(String announcementId) {
        return Optional.of(INSTANCE.mapAnnouncement(getJpaAnnouncement(announcementId)));
    }

    @Override
    public List<Announcement> getAllAnnouncementRecords() {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaAnnouncement)
                .fetch()
                .stream()
                .map(INSTANCE::mapAnnouncement)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateAnAnnouncement(String senderEmployeeId, String announcementId, Employee employee) {
        return false;
    }

    @Override
    public boolean deleteAnAnnouncementRecord(String announcementId) {
        return new JPAQueryFactory(jpaApi.em())
                .delete(qJpaAnnouncement)
                .where(qJpaAnnouncement.announcementId.eq(announcementId))
                .execute() == 1;
    }

    private void dispatchAnnouncementToRecipients(JpaAnnouncement announcement, JpaDepartment department) {

        ActorSystem akkaSystem = ActorSystem.create("system");
        ActorRef announcementDispatchActor = akkaSystem.actorOf(AnnouncementDispatchActor.create(), "AnnouncementDispatchActor");

        announcementDispatchActor.tell(
                new AnnouncementDispatchActor.AnnouncementDispatchData(
                        announcement, department, jpaApi
                ),
                ActorRef.noSender()
        );
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

    /**
     * @author WARITH
     * @dateCreated 15/08/2023
     * @description A helper method to fetch an announcement record from the database
     *
     * @param announcementId ID of the announcement record to fetch
     *
     * @return JpaAnnouncement An announcement record or null if not found
     */
    private JpaAnnouncement getJpaAnnouncement(String announcementId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaAnnouncement)
                .where(qJpaAnnouncement.announcementId.eq(announcementId))
                .fetchOne();
    }

    /**
     * @author WARITH
     * @dateCreated 15/08/2023
     * @description A helper method to fetch a department record from the database
     *
     * @param departmentId ID of the employee to fetch
     *
     * @return JpaDepartment An employee record or null if not found
     */
    private JpaDepartment getJpaDepartment(String departmentId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaDepartment)
                .where(qJpaDepartment.departmentId.eq(departmentId))
                .fetchOne();
    }

}
