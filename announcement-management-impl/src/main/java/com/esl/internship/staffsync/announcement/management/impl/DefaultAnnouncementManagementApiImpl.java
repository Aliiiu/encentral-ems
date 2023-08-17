package com.esl.internship.staffsync.announcement.management.impl;

import com.esl.internship.staffsync.announcement.management.api.IAnnouncementManagementApi;
import com.esl.internship.staffsync.announcement.management.dto.AnnouncementDTO;
import com.esl.internship.staffsync.announcement.management.model.Announcement;
import com.esl.internship.staffsync.announcement.management.model.EmployeeAnnouncement;
import com.esl.internship.staffsync.announcement.management.query.projection.AnnouncementProjection;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.entities.*;
import com.esl.internship.staffsync.entities.enums.NotificationPriority;
import com.esl.internship.staffsync.entities.enums.NotificationStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
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

    /**
     * @author WARITH
     * @dateCreated 16/08/2023
     * @description Create a new Announcement
     *
     * @param senderEmployeeId ID of the employee creating the Announcement
     * @param announcementDTO The Announcement information
     * @param employee The Employee object creating the record
     *
     * @return Response<Announcement>
     */
    @Override
    public Response<Announcement> createAnnouncement(String senderEmployeeId, AnnouncementDTO announcementDTO, Employee employee) {

        Response<Announcement> response = new Response<>();

        JpaDepartment department = null;

        if (!announcementDTO.getAnnouncementFor().equalsIgnoreCase("general")) {
            department =  getJpaDepartment(announcementDTO.getAnnouncementFor());

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

        Timestamp deliveryDate = announcementDTO.getDeliveryDate();

        if (deliveryDate == null)
            deliveryDate = Timestamp.from(Instant.now());

        JpaAnnouncement jpaAnnouncement = INSTANCE.mapAnnouncement(announcementDTO);
        jpaAnnouncement.setAnnouncementId(UUID.randomUUID().toString());
        jpaAnnouncement.setCreatedBy(stringifyEmployee(employee));
        jpaAnnouncement.setDateCreated(Timestamp.from(Instant.now()));
        jpaAnnouncement.setDeliveryDate(deliveryDate);
        jpaAnnouncement.setSender(jpaEmployee);
        jpaAnnouncement.setPriority(NotificationPriority.NORMAL);

        jpaApi.em().persist(jpaAnnouncement);

        dispatchAnnouncementToRecipients(jpaAnnouncement, department);

        return response.setValue(INSTANCE.mapAnnouncement(jpaAnnouncement));
    }

    /**
     * @author WARITH
     * @dateCreated 16/08/2023
     * @description Get an Announcement record by ID
     *
     * @param announcementId ID of the Announcement record to fetch
     *
     * @return Optional<Announcement>
     */
    @Override
    public Optional<Announcement> getAnnouncementRecordById(String announcementId) {
        return Optional.of(INSTANCE.mapAnnouncement(getJpaAnnouncement(announcementId)));
    }

    /**
     * @author WARITH
     * @dateCreated 16/08/2023
     * @description Get all Announcement record
     *
     * @return List<Announcement>
     */
    @Override
    public List<Announcement> getAllAnnouncementRecords() {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaAnnouncement)
                .fetch()
                .stream()
                .map(INSTANCE::mapAnnouncement)
                .collect(Collectors.toList());
    }

    /**
     * @author WARITH
     * @dateCreated 16/08/2023
     * @description Update an Announcement Record
     *
     * @param announcementId ID of the Announcement record to update
     * @param announcementDTO The Announcement information
     * @param employee The Employee object making the update
     *
     * @return boolean
     */
    @Override
    public boolean updateAnAnnouncement(String announcementId, AnnouncementDTO announcementDTO, Employee employee) {
        Timestamp deliveryDate = announcementDTO.getDeliveryDate();
        if (deliveryDate == null)
            deliveryDate = Timestamp.from(Instant.now());

        // TODO: Update for when the recipients (announcementFor) is changed

        if (
                new JPAQueryFactory(jpaApi.em())
                        .update(qJpaAnnouncement)
                        .where(qJpaAnnouncement.announcementId.eq(announcementId))
                        .set(qJpaAnnouncement.modifiedBy, stringifyEmployee(employee))
                        .set(qJpaAnnouncement.dateModified, Timestamp.from(Instant.now()))
                        .set(qJpaAnnouncement.announcementTitle, announcementDTO.getAnnouncementTitle())
                        .set(qJpaAnnouncement.announcementMessage, announcementDTO.getAnnouncementMessage())
                        .set(qJpaAnnouncement.deliveryDate, deliveryDate)
                        .execute() == 1
        ) {
            // Update all recipients (mark unread and set Date read to null)
            return new JPAQueryFactory(jpaApi.em())
                    .update(qJpaAnnouncementRecipient)
                    .where(qJpaAnnouncementRecipient.announcement.announcementId.eq(announcementId))
                    .set(qJpaAnnouncementRecipient.status, NotificationStatus.UNREAD)
                    .set(qJpaAnnouncementRecipient.dateModified, Timestamp.from(Instant.now()))
                    .setNull(qJpaAnnouncementRecipient.dateRead)
                    .execute() == 1;
        }
        return false;
    }

    /**
     * @author WARITH
     * @dateCreated 16/08/2023
     * @description Delete an Announcement Record
     *
     * @param announcementId ID of the Announcement record to delete
     *
     * @return boolean
     */
    @Override
    public boolean deleteAnAnnouncementRecord(String announcementId) {
        return new JPAQueryFactory(jpaApi.em())
                .delete(qJpaAnnouncement)
                .where(qJpaAnnouncement.announcementId.eq(announcementId))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 16/08/2023
     * @description Get all announcements for an employee
     *
     * @param employeeId ID of the employee to fetch announcments for
     *
     * @return List<EmployeeAnnouncement>
     */
    @Override
    public List<EmployeeAnnouncement> getAllEmployeeAnnouncementsOrderedByDate(String employeeId) {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaAnnouncementRecipient)
                .where(qJpaAnnouncementRecipient.employee.employeeId.eq(employeeId))
                .where(qJpaAnnouncementRecipient.announcement.deliveryDate.loe(Timestamp.from(Instant.now())))
                .orderBy(qJpaAnnouncementRecipient.announcement.deliveryDate.desc())
                .fetch()
                .stream()
                .map(AnnouncementProjection::mapEmployeeAnnouncement)
                .collect(Collectors.toList());
    }

    /**
     * @author WARITH
     * @dateCreated 16/08/2023
     * @description Get all announcements for an employee that are UNREAD
     *
     * @param employeeId ID of the employee to fetch announcments for
     *
     * @return List<EmployeeAnnouncement>
     */
    @Override
    public List<EmployeeAnnouncement> getAllUnreadEmployeeAnnouncementsOrderedByDate(String employeeId) {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaAnnouncementRecipient)
                .where(qJpaAnnouncementRecipient.employee.employeeId.eq(employeeId))
                .where(qJpaAnnouncementRecipient.status.eq(NotificationStatus.UNREAD))
                .where(qJpaAnnouncementRecipient.announcement.deliveryDate.loe(Timestamp.from(Instant.now())))
                .orderBy(qJpaAnnouncementRecipient.announcement.deliveryDate.desc())
                .fetch()
                .stream()
                .map(AnnouncementProjection::mapEmployeeAnnouncement)
                .collect(Collectors.toList());
    }

    /**
     * @author WARITH
     * @dateCreated 16/08/2023
     * @description Mark an employee announcement record READ
     *
     * @param announcementRecipientId ID of the employee announcement to mark READ;
     *
     * @return boolean
     */
    @Override
    public boolean markAnnouncementAsRead(String announcementRecipientId) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaAnnouncementRecipient)
                .where(qJpaAnnouncementRecipient.announcementRecipientId.eq(announcementRecipientId))
                .set(qJpaAnnouncementRecipient.status, NotificationStatus.READ)
                .set(qJpaAnnouncementRecipient.dateRead, Timestamp.from(Instant.now()))
                .set(qJpaAnnouncementRecipient.dateModified, Timestamp.from(Instant.now()))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 17/08/2023
     * @description Mark all employee's announcements record READ
     *
     * @param employeeId ID of the employee to mark all their announcements READ;
     *
     * @return boolean
     */
    @Override
    public boolean markAllAnnouncementsForEmployeeAsRead(String employeeId) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaAnnouncementRecipient)
                .where(qJpaAnnouncementRecipient.employee.employeeId.eq(employeeId))
                .set(qJpaAnnouncementRecipient.status, NotificationStatus.READ)
                .set(qJpaAnnouncementRecipient.dateRead, Timestamp.from(Instant.now()))
                .set(qJpaAnnouncementRecipient.dateModified, Timestamp.from(Instant.now()))
                .execute() == 1;
    }

    /**
     * @author WARITH
     * @dateCreated 16/08/2023
     * @description Mark an employee announcement record UNREAD (I'm not sure why we'd ever need this, lol)
     *
     * @param announcementRecipientId ID of the employee announcement to mark UNREAD;
     *
     * @return boolean
     */
    @Override
    public boolean markAnnouncementAsUnRead(String announcementRecipientId) {
        return new JPAQueryFactory(jpaApi.em())
                .update(qJpaAnnouncementRecipient)
                .where(qJpaAnnouncementRecipient.announcementRecipientId.eq(announcementRecipientId))
                .set(qJpaAnnouncementRecipient.status, NotificationStatus.UNREAD)
                .setNull(qJpaAnnouncementRecipient.dateRead)
                .set(qJpaAnnouncementRecipient.dateModified, Timestamp.from(Instant.now()))
                .execute() == 1;
    }

    private void dispatchAnnouncementToRecipients(JpaAnnouncement announcement, JpaDepartment department) {

        Collection<JpaEmployee> recipients;
        if (department == null)
            recipients = new JPAQueryFactory(jpaApi.em())
                    .selectFrom(qJpaEmployee)
                    .fetch();
        else
            recipients = department.getEmployees();

        for (JpaEmployee employee : recipients) {
            JpaAnnouncementRecipient recipient = new JpaAnnouncementRecipient();
            recipient.setAnnouncementRecipientId(UUID.randomUUID().toString());
            recipient.setAnnouncement(announcement);
            recipient.setEmployee(employee);
            recipient.setStatus(NotificationStatus.UNREAD);
            recipient.setDateCreated(Timestamp.from(Instant.now()));

            jpaApi.em().persist(recipient);
        }
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
