package com.esl.internship.staffsync.system.configuration.impl;

import com.encentral.scaffold.commons.model.Employee;
import com.encentral.staffsync.entity.JpaNotification;
import com.encentral.staffsync.entity.JpaNotificationTemplate;
import com.encentral.staffsync.entity.QJpaNotification;
import com.encentral.staffsync.entity.QJpaNotificationTemplate;
import com.encentral.staffsync.entity.enums.NotificationStatus;
import com.esl.internship.staffsync.system.configuration.api.INotification;
import com.esl.internship.staffsync.system.configuration.model.Notification;
import com.esl.internship.staffsync.system.configuration.model.dto.CreateNotificationDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;
import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.encentral.scaffold.commons.util.Utility.stringifyEmployee;
import static com.esl.internship.staffsync.system.configuration.model.SystemConfigurationMapper.INSTANCE;

/**
 * @author DEMILADE
 * @dateCreated 03/07/2023
 * @description General implementation of INotification
 */
public class NotificationImpl implements INotification {
    private static final QJpaNotification qJpaNotification = QJpaNotification.jpaNotification;
    private static final QJpaNotificationTemplate qJpaNotificationTemplate = QJpaNotificationTemplate.jpaNotificationTemplate;
    @Inject
    JPAApi jpaApi;

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
                .where(qJpaNotification.receiver.employeeId.eq(employeeId))
                .where(qJpaNotification.deliveryStatus.eq(NotificationStatus.UNREAD))
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
                .fetch()
                .stream()
                .map(INSTANCE::jpaNotificationToNotification)
                .collect(Collectors.toList());
    }

    /**
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Marks a notification as read
     *
     * @param notificationId Notification id
     * @param employee       Employee marking notification as read
     * @return boolean indicating success
     */
    @Override
    public boolean markNotificationAsRead(String notificationId, Employee employee) {
        return new JPAQueryFactory(jpaApi.em()).update(qJpaNotification)
                .set(qJpaNotification.deliveryStatus, NotificationStatus.READ)
                .set(qJpaNotification.modifiedBy, stringifyEmployee(employee ,"Marked as read"))
                .set(qJpaNotification.dateModified, Timestamp.from(Instant.now()))
                .where(qJpaNotification.notificationId.eq(notificationId))
                .execute() == 1;
    }
    /**
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Marks all an employee notifications as read
     *
     * @param employeeId Employee id
     * @param employee Employee marking notifications as read
     * @return boolean indicating success
     */
    @Override
    public boolean markAllEmployeeNotificationAsRead(String employeeId, Employee employee){
        return new JPAQueryFactory(jpaApi.em()).update(qJpaNotification)
                .set(qJpaNotification.deliveryStatus, NotificationStatus.READ)
                .set(qJpaNotification.modifiedBy, stringifyEmployee(employee ,"Marked as read"))
                .set(qJpaNotification.dateModified, Timestamp.from(Instant.now()))
                .where(qJpaNotification.receiver.employeeId.eq(employeeId))
                .execute() == 1;
    }

    /**
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Marks all an employee notifications as deleted
     *
     * @param employeeId Employee id
     * @param employee  Employee marking notification as deleted
     * @return boolean indicating success
     */
    @Override
    public boolean markAllEmployeeNotificationAsDeleted(String employeeId, Employee employee){
        return new JPAQueryFactory(jpaApi.em()).update(qJpaNotification)
                .set(qJpaNotification.deliveryStatus, NotificationStatus.DELETED)
                .set(qJpaNotification.modifiedBy, stringifyEmployee(employee ,"Marked as read"))
                .set(qJpaNotification.dateModified, Timestamp.from(Instant.now()))
                .where(qJpaNotification.receiver.employeeId.eq(employeeId))
                .execute() == 1;
    }

    /**
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Deletes all notifications sent to an employee
     *
     * @param employeeId Employee id
     * @return boolean indicating success
     */
    @Override
    public boolean deleteAllEmployeeNotification(String employeeId){
        return new JPAQueryFactory(jpaApi.em()).delete(qJpaNotification)
                .where(qJpaNotification.receiver.employeeId.eq(employeeId))
                .execute() == 1;
    }

    /**
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Method to create a new notification
     *
     *  @param createNotificationDTO DTO for creating notifications
     *  @param employee Employee object
     *  @return notification object
     */
    @Override
    public Notification createNotification(CreateNotificationDTO createNotificationDTO, Employee employee) {

        JpaNotificationTemplate jpaNotificationTemplate = new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaNotificationTemplate)
                .where(qJpaNotificationTemplate.notificationTemplateId.eq(
                        createNotificationDTO.getNotificationTemplateBeanId())
                )
                .fetchOne();

        Notification n = INSTANCE.createNotificationToNotification(createNotificationDTO);
        JpaNotification jpaNotification = INSTANCE.notificationToJpaNotification(n);
        jpaNotification.setNotificationMessage(jpaNotificationTemplate.getNotificationTemplateContent());
        jpaNotification.setNotificationTitle(jpaNotificationTemplate.getNotificationTemplateName());
        jpaNotification.setDeliveryStatus(NotificationStatus.UNREAD);
        jpaNotification.setNotificationId(UUID.randomUUID().toString());
        jpaNotification.setCreatedBy(stringifyEmployee(employee));
        jpaNotification.setDateCreated(Timestamp.from(Instant.now()));

        jpaApi.em().persist(jpaNotification);

        return INSTANCE.jpaNotificationToNotification(jpaNotification);
    }

    /**
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Marks a notification as deleted
     *
     * @param notificationId Notification id
     * @param employee       Employee marking notification as deleted
     * @return boolean indicating success
     */
    @Override
    public boolean markNotificationAsDeleted(String notificationId, Employee employee) {
        return new JPAQueryFactory(jpaApi.em()).update(qJpaNotification)
                .set(qJpaNotification.deliveryStatus, NotificationStatus.DELETED)
                .where(qJpaNotification.notificationId.eq(notificationId))
                .execute() == 1;
    }

    /**
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Permanently deletes a notification
     *
     * @param notificationId Notification id
     * @return boolean indicating success
     */
    @Override
    public boolean deleteNotification(String notificationId) {
        return new JPAQueryFactory(jpaApi.em()).delete(qJpaNotification)
                .where(qJpaNotification.notificationId.eq(notificationId))
                .execute() == 1;
    }

    @Override
    public boolean verifyEmployee(String employeeId, String notificationId) {
        return getJpaNotificationById(notificationId).getReceiver().getEmployeeId().equals(employeeId);
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
}
