package com.esl.internship.staffsync.system.configuration.impl;

import com.encentral.scaffold.commons.model.Employee;
import com.encentral.staffsync.entity.JpaNotificationTemplate;
import com.encentral.staffsync.entity.QJpaNotification;
import com.encentral.staffsync.entity.QJpaNotificationTemplate;
import com.esl.internship.staffsync.system.configuration.api.INotificationTemplate;
import com.esl.internship.staffsync.system.configuration.model.Notification;
import com.esl.internship.staffsync.system.configuration.model.NotificationTemplate;
import com.esl.internship.staffsync.system.configuration.model.dto.CreateNotificationTemplateDTO;
import com.esl.internship.staffsync.system.configuration.model.dto.EditNotificationTemplateDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;
import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.esl.internship.staffsync.system.configuration.model.SystemConfigurationMapper.INSTANCE;
import static com.encentral.scaffold.commons.util.Utility.stringifyEmployee;


public class NotificationTemplateImpl implements INotificationTemplate {

    @Inject
    JPAApi jpaApi;

    private static final QJpaNotification qJpaNotification = QJpaNotification.jpaNotification;

    private static final QJpaNotificationTemplate qJpaNotificationTemplate = QJpaNotificationTemplate.jpaNotificationTemplate;


    @Override
    public Optional<NotificationTemplate> getNotificationTemplate(String notificationTemplateId) {
        return Optional.ofNullable(INSTANCE.jpaNotificationTemplateToNotificationTemplate(
                getJpaNotificationTemplateById(notificationTemplateId))
        );
    }

    /**
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Fetches a list of all notification templates
     *
     *
     * @return List containing Notification templates
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
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Fetches a list of all notifications created with a template
     *
     * @param notificationTemplateId Notification template id
     *
     * @return List containing Notifications
     */
    @Override
    public List<Notification> getNotifications(String notificationTemplateId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(jpaApi.em());

        return queryFactory.selectFrom(qJpaNotification)
                .where(qJpaNotification.notificationTemplateBean
                        .notificationTemplateId.eq(notificationTemplateId))
                .fetch()
                .stream()
                .map(INSTANCE::jpaNotificationToNotification)
                .collect(Collectors.toList());
    }

    /**
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Creates a new notification template
     *
     * @param notificationTemplateDTO DTO for creating notification template
     * @param employee Employee creating the template
     *
     * @return Notification template object
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

    @Override
    public boolean editNotificationTemplate(EditNotificationTemplateDTO notificationTemplateDTO, Employee employee) {
       try {
           JpaNotificationTemplate jpaNotificationTemplate = getJpaNotificationTemplateById(notificationTemplateDTO.getNotificationTemplateId());

           INSTANCE.editDTOToJpaNotificationTemplate(jpaNotificationTemplate, notificationTemplateDTO);
           jpaNotificationTemplate.setDateModified(Timestamp.from(Instant.now()));
           jpaNotificationTemplate.setModifiedBy(stringifyEmployee(employee, "Updated Notification template"));

           jpaApi.em().merge(jpaNotificationTemplate);
           return true;
       }
       catch (Exception e){
           return false;
       }
    }

    /**
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Deletes a notification template
     *
     * @param notificationTemplateId JpaNotificationTemplate id
     * @return boolean
     */
    @Override
    public boolean deleteNotificationTemplate(String notificationTemplateId) {
        return new JPAQueryFactory(jpaApi.em()).delete(qJpaNotificationTemplate)
                .where(qJpaNotificationTemplate.notificationTemplateId.eq(notificationTemplateId))
                .execute() == 1;
    }

    /**
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Fetches a JpaNotificationTemplate via its id
     *
     * @param templateName
     * @return
     */
    @Override
    public boolean checkIfNotificationNameInUse(String templateName){
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaNotificationTemplate)
                .where(qJpaNotificationTemplate.notificationTemplateName.eq(templateName))
                .fetchOne() != null;
    }

    /**
     * @author DEMILADE
     * @dateCreated 04/08/2023
     * @description Fetches a JpaNotificationTemplate via its id
     *
     * @param notificationTemplateId JpaNotificationTemplate id
     * @return JpaNotificationTemplate object
     */
    private JpaNotificationTemplate getJpaNotificationTemplateById(String notificationTemplateId){
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaNotificationTemplate)
                .where(qJpaNotificationTemplate.notificationTemplateId.eq(notificationTemplateId))
                .fetchOne();
    }


}
