package com.esl.internship.staffsync.system.configuration.impl;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.employee.management.api.IEmployeeApi;
import com.esl.internship.staffsync.entities.*;
import com.esl.internship.staffsync.entities.enums.NotificationPriority;
import com.esl.internship.staffsync.entities.enums.NotificationStatus;
import com.esl.internship.staffsync.system.configuration.api.INotification;
import com.esl.internship.staffsync.system.configuration.dto.CreateNotificationDTO;
import com.esl.internship.staffsync.system.configuration.model.Notification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.esl.internship.staffsync.commons.util.Utility.stringifyEmployee;
import static com.esl.internship.staffsync.system.configuration.model.SystemConfigurationMapper.INSTANCE;

/**
 * @author DEMILADE
 * @dateCreated 03/07/2023
 * @description General implementation of INotification
 */
public class DefaultNotificationImpl implements INotification {
    private static final QJpaNotification qJpaNotification = QJpaNotification.jpaNotification;
    private static final QJpaNotificationTemplate qJpaNotificationTemplate = QJpaNotificationTemplate.jpaNotificationTemplate;
    @Inject
    JPAApi jpaApi;

    @Inject
    IEmployeeApi iEmployee;

    /**
     * @param notificationId Id of the notification
     * @return Optional containing Notification
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Fetches a notification
     */
    @Override
    public Optional<Notification> getNotification(String notificationId) {
        return Optional.ofNullable(INSTANCE.jpaNotificationToNotification(getJpaNotificationById(notificationId)));
    }

    /**
     * @param employeeId Employee id
     * @return List containing Notifications
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Fetches a list of all notifications sent to an employee
     */
    @Override
    public List<Notification> getEmployeeReceivedNotifications(String employeeId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        return queryFactory.selectFrom(qJpaNotification)
                .where(qJpaNotification.receiver.employeeId.eq(employeeId))
                .orderBy(qJpaNotification.dateCreated.desc())
                .fetch()
                .stream()
                .map(INSTANCE::jpaNotificationToNotification)
                .collect(Collectors.toList());
    }

    /**
     * @param employeeId Employee id
     * @return List containing Notifications
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Fetches a list of all notifications sent by an employee
     */
    @Override
    public List<Notification> getEmployeeSentNotifications(String employeeId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        return queryFactory.selectFrom(qJpaNotification)
                .where(qJpaNotification.sender.employeeId.eq(employeeId))
                .orderBy(qJpaNotification.dateCreated.desc())
                .fetch()
                .stream()
                .map(INSTANCE::jpaNotificationToNotification)
                .collect(Collectors.toList());
    }

    /**
     * @param employeeId Employee id
     * @return List containing Notifications
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Fetches a list of all unread notifications sent to an employee
     */
    @Override
    public List<Notification> getEmployeeUnreadNotifications(String employeeId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        return queryFactory.selectFrom(qJpaNotification)
                .where(qJpaNotification.receiver.employeeId.eq(employeeId)
                        .and(qJpaNotification.deliveryStatus.eq(NotificationStatus.UNREAD)))
                .orderBy(qJpaNotification.dateCreated.desc())
                .fetch()
                .stream()
                .map(INSTANCE::jpaNotificationToNotification)
                .collect(Collectors.toList());
    }

    /**
     * @return List containing Notifications
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Fetches a list of all notifications in the system
     */
    @Override
    public List<Notification> getSystemNotifications() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        return queryFactory.selectFrom(qJpaNotification)
                .orderBy(qJpaNotification.dateCreated.desc())
                .fetch()
                .stream()
                .map(INSTANCE::jpaNotificationToNotification)
                .collect(Collectors.toList());
    }

    /**
     * @param notificationId Notification id
     * @param employee       Employee marking notification as read
     * @return boolean indicating success
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Marks a notification as read
     */
    @Override
    public boolean markNotificationAsRead(String notificationId, Employee employee) {
        return new JPAQueryFactory(jpaApi.em()).update(qJpaNotification)
                .set(qJpaNotification.deliveryStatus, NotificationStatus.READ)
                .set(qJpaNotification.modifiedBy, stringifyEmployee(employee, "Marked as read"))
                .set(qJpaNotification.dateModified, Timestamp.from(Instant.now()))
                .where(qJpaNotification.notificationId.eq(notificationId))
                .execute() == 1;
    }

    /**
     * @param employeeId Employee id
     * @param employee   Employee marking notifications as read
     * @return boolean indicating success
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Marks all an employee notifications as read
     */
    @Override
    public boolean markAllEmployeeNotificationAsRead(String employeeId, Employee employee) {
        return new JPAQueryFactory(jpaApi.em()).update(qJpaNotification)
                .where(qJpaNotification.receiver.employeeId.eq(employeeId))
                .set(qJpaNotification.deliveryStatus, NotificationStatus.READ)
                .set(qJpaNotification.modifiedBy, stringifyEmployee(employee, "Marked as read"))
                .set(qJpaNotification.dateModified, Timestamp.from(Instant.now()))
                .execute() >= 1;
    }

    /**
     * @param employeeId Employee id
     * @param employee   Employee marking notification as deleted
     * @return boolean indicating success
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Marks all an employee notifications as deleted
     */
    @Override
    public boolean markAllEmployeeNotificationAsDeleted(String employeeId, Employee employee) {
        return new JPAQueryFactory(jpaApi.em()).update(qJpaNotification)
                .where(qJpaNotification.receiver.employeeId.eq(employeeId))
                .set(qJpaNotification.deliveryStatus, NotificationStatus.DELETED)
                .set(qJpaNotification.modifiedBy, stringifyEmployee(employee, "Marked as read"))
                .set(qJpaNotification.dateModified, Timestamp.from(Instant.now()))
                .execute() >= 1;
    }

    /**
     * @param employeeId Employee id
     * @return boolean indicating success
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Deletes all notifications sent to an employee
     */
    @Override
    public boolean deleteAllEmployeeNotification(String employeeId) {
        return new JPAQueryFactory(jpaApi.em()).delete(qJpaNotification)
                .where(qJpaNotification.receiver.employeeId.eq(employeeId))
                .execute() >= 1;
    }

    /**
     * @param createNotificationDTO DTO for creating notifications
     * @param employeeId            Id of employee creating notification
     * @return notification object
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Method to create a new notification
     */
    @Override
    public Notification createNotification(CreateNotificationDTO createNotificationDTO, String employeeId) {
        JpaNotificationTemplate jpaNotificationTemplate = new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaNotificationTemplate)
                .where(qJpaNotificationTemplate.notificationTemplateId.eq(
                        createNotificationDTO.getNotificationTemplateBeanId())
                )
                .fetchOne();

        Employee employee = iEmployee.getEmployeeById(employeeId).orElseThrow();

        Notification n = INSTANCE.createNotificationToNotification(createNotificationDTO);
        JpaNotification jpaNotification = INSTANCE.notificationToJpaNotification(n);
        jpaNotification.setNotificationTitle(jpaNotificationTemplate.getNotificationTemplateName());
        jpaNotification.setDeliveryStatus(NotificationStatus.UNREAD);
        jpaNotification.setNotificationId(UUID.randomUUID().toString());
        jpaNotification.setCreatedBy(stringifyEmployee(employee));
        jpaNotification.setDateCreated(Timestamp.from(Instant.now()));

        String formattedMessage = formatNotificationMessage(
                createNotificationDTO.getSubject(),
                createNotificationDTO.getObject(),
                jpaNotificationTemplate.getNotificationTemplateContent()
        );
        jpaNotification.setNotificationMessage(formattedMessage);
        jpaApi.em().persist(jpaNotification);

        return INSTANCE.jpaNotificationToNotification(jpaNotification);
    }

    /**
     * @param notificationId Notification id
     * @param employee       Employee marking notification as deleted
     * @return boolean indicating success
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Marks a notification as deleted
     */
    @Override
    public boolean markNotificationAsDeleted(String notificationId, Employee employee) {
        return new JPAQueryFactory(jpaApi.em()).update(qJpaNotification)
                .set(qJpaNotification.deliveryStatus, NotificationStatus.DELETED)
                .where(qJpaNotification.notificationId.eq(notificationId))
                .execute() == 1;
    }

    /**
     * @param notificationId Notification id
     * @return boolean indicating success
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Permanently deletes a notification
     */
    @Override
    public boolean deleteNotification(String notificationId) {
        return new JPAQueryFactory(jpaApi.em()).delete(qJpaNotification)
                .where(qJpaNotification.notificationId.eq(notificationId))
                .execute() == 1;
    }

    /**
     * @param employeeId     Employee id
     * @param notificationId Notification id
     * @return boolean indicating ownership
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Checks if a notification was sent to an employee
     */
    @Override
    public boolean verifyEmployee(String employeeId, String notificationId) {
        return getJpaNotificationById(notificationId).getReceiver().getEmployeeId().equals(employeeId);
    }


    @Override
    public boolean sendNotification(String receiverId, String senderId, String subject, String object, String templateId) {
        JpaNotificationTemplate jpaNotificationTemplate = new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaNotificationTemplate)
                .where(qJpaNotificationTemplate.notificationTemplateId.eq(templateId))
                .fetchOne();
        JpaNotification jpaNotification = new JpaNotification();
        jpaNotification.setNotificationId(UUID.randomUUID().toString());
        jpaNotification.setNotificationTitle(jpaNotificationTemplate.getNotificationTemplateName());
        jpaNotification.setDeliveryStatus(NotificationStatus.UNREAD);
        jpaNotification.setPriority(NotificationPriority.NORMAL);

        String formattedMessage = formatNotificationMessage(
                subject,
                object,
                jpaNotificationTemplate.getNotificationTemplateContent()
        );

        jpaNotification.setNotificationMessage(formattedMessage);

        JpaEmployee receiver = new JpaEmployee();
        receiver.setEmployeeId(receiverId);

        JpaEmployee sender = new JpaEmployee();
        sender.setEmployeeId(senderId);


        jpaNotification.setReceiver(receiver);
        jpaNotification.setSender(sender);
        jpaNotification.setNotificationTemplateBean(jpaNotificationTemplate);

        Employee employee = iEmployee.getEmployeeById("system_employee").orElseThrow();
        jpaNotification.setCreatedBy(stringifyEmployee(employee));
        jpaNotification.setDateCreated(Timestamp.from(Instant.now()));
        jpaApi.em().persist(jpaNotification);

        return true;
    }

    /**
     * @param notificationId JpaNotification id
     * @return JpaNotification object
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Fetches a JpaNotification via its id
     */
    private JpaNotification getJpaNotificationById(String notificationId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaNotification)
                .where(qJpaNotification.notificationId.eq(notificationId))
                .fetchOne();
    }

    /**
     * @param subject         Subject of notification message
     * @param object          Object of notification message
     * @param templateMessage Message from notification template
     * @return Formatted String
     * @author DEMILADE
     * @description Method to format notification message
     * @dateCreated 17/08/2023
     */
    private String formatNotificationMessage(String subject, String object, String templateMessage) {
        return String.format(templateMessage, subject, object);
    }
}
