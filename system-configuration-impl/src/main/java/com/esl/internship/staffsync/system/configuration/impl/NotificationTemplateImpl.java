package com.esl.internship.staffsync.system.configuration.impl;

import com.encentral.scaffold.commons.model.Employee;
import com.encentral.staffsync.entity.JpaNotificationTemplate;
import com.encentral.staffsync.entity.QJpaNotification;
import com.encentral.staffsync.entity.QJpaNotificationTemplate;
import com.esl.internship.staffsync.system.configuration.api.INotificationTemplate;
import com.esl.internship.staffsync.system.configuration.dto.CreateNotificationTemplateDTO;
import com.esl.internship.staffsync.system.configuration.dto.EditNotificationTemplateDTO;
import com.esl.internship.staffsync.system.configuration.model.Notification;
import com.esl.internship.staffsync.system.configuration.model.NotificationTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.encentral.scaffold.commons.util.Utility.stringifyEmployee;
import static com.esl.internship.staffsync.system.configuration.model.SystemConfigurationMapper.INSTANCE;

/**
 * @author DEMILADE
 * @dateCreated 03/07/2023
 * @description General implementation of INotificationTemplate
 */
public class NotificationTemplateImpl implements INotificationTemplate {

    private static final QJpaNotification qJpaNotification = QJpaNotification.jpaNotification;
    private static final QJpaNotificationTemplate qJpaNotificationTemplate = QJpaNotificationTemplate.jpaNotificationTemplate;
    @Inject
    JPAApi jpaApi;

    @Override
    public Optional<NotificationTemplate> getNotificationTemplate(String notificationTemplateId) {
        return Optional.ofNullable(INSTANCE.jpaNotificationTemplateToNotificationTemplate(
                getJpaNotificationTemplateById(notificationTemplateId))
        );
    }

    /**
     * @return List containing Notification templates
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Fetches a list of all notification templates
     */
    @Override
    public List<NotificationTemplate> getAllNotificationTemplates() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        return queryFactory.selectFrom(qJpaNotificationTemplate)
                .fetch()
                .stream()
                .map(INSTANCE::jpaNotificationTemplateToNotificationTemplate)
                .collect(Collectors.toList());
    }

    /**
     * @param notificationTemplateId Notification template id
     * @return List containing Notifications
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Fetches a list of all notifications created with a template
     */
    @Override
    public List<Notification> getNotifications(String notificationTemplateId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());
        return queryFactory.selectFrom(qJpaNotification)
                .where(qJpaNotification.notificationTemplateBean
                        .notificationTemplateId.eq(notificationTemplateId))
                .orderBy(qJpaNotification.dateCreated.desc())
                .fetch()
                .stream()
                .map(INSTANCE::jpaNotificationToNotification)
                .collect(Collectors.toList());
    }

    /**
     * @param notificationTemplateDTO DTO for creating notification template
     * @param employee                Employee creating the template
     * @return Notification template object
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Creates a new notification template
     */
    @Override
    public NotificationTemplate createNotificationTemplate(CreateNotificationTemplateDTO notificationTemplateDTO, Employee employee) {
        NotificationTemplate nt = INSTANCE.dtoToNotificationTemplate(notificationTemplateDTO);
        JpaNotificationTemplate jpaNotificationTemplate = INSTANCE.notificationTemplateToJpaNotificationTemplate(nt);
        jpaNotificationTemplate.setNotificationTemplateId(UUID.randomUUID().toString());
        jpaNotificationTemplate.setCreatedBy(stringifyEmployee(employee));
        jpaNotificationTemplate.setDateCreated(Timestamp.from(Instant.now()));

        jpaApi.em().merge(jpaNotificationTemplate);
        return INSTANCE.jpaNotificationTemplateToNotificationTemplate(jpaNotificationTemplate);
    }

    /**
     * @param notificationTemplateDTO DTO for editing notification template
     * @param employee                Employee creating the template
     * @return boolean indicating success
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Adds changes to an existing notification template
     */
    @Override
    public boolean editNotificationTemplate(EditNotificationTemplateDTO notificationTemplateDTO, Employee employee) {
        AtomicBoolean isTransactionSuccessful = new AtomicBoolean(false);
        jpaApi.withTransaction(em->{
            JpaNotificationTemplate jpaNotificationTemplate = getJpaNotificationTemplateById(notificationTemplateDTO.getNotificationTemplateId());
            INSTANCE.editDTOToJpaNotificationTemplate(jpaNotificationTemplate, notificationTemplateDTO);
            jpaNotificationTemplate.setDateModified(Timestamp.from(Instant.now()));
            jpaNotificationTemplate.setModifiedBy(stringifyEmployee(employee, "Updated Notification template"));
            em.merge(jpaNotificationTemplate);
            isTransactionSuccessful.set(true);
            return true;
        });
        return isTransactionSuccessful.get();
    }

    /**
     * @param notificationTemplateId JpaNotificationTemplate id
     * @return boolean
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Deletes a notification template
     */
    @Override
    public boolean deleteNotificationTemplate(String notificationTemplateId) {
        return new JPAQueryFactory(jpaApi.em()).delete(qJpaNotificationTemplate)
                .where(qJpaNotificationTemplate.notificationTemplateId.eq(notificationTemplateId))
                .execute() == 1;
    }

    /**
     * @param templateName
     * @return
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Fetches a JpaNotificationTemplate via its id
     */
    @Override
    public boolean checkIfNotificationTemplateNameInUse(String templateName) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaNotificationTemplate)
                .where(qJpaNotificationTemplate.notificationTemplateName.eq(templateName))
                .fetchOne() != null;
    }

    /**
     * @param notificationTemplateId JpaNotificationTemplate id
     * @return JpaNotificationTemplate object
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Fetches a JpaNotificationTemplate via its id
     */
    private JpaNotificationTemplate getJpaNotificationTemplateById(String notificationTemplateId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaNotificationTemplate)
                .where(qJpaNotificationTemplate.notificationTemplateId.eq(notificationTemplateId))
                .fetchOne();
    }


}
