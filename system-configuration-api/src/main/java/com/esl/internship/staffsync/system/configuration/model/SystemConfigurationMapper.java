package com.esl.internship.staffsync.system.configuration.model;

import com.encentral.staffsync.entity.JpaAppConfig;
import com.encentral.staffsync.entity.JpaNotification;
import com.encentral.staffsync.entity.JpaNotificationTemplate;
import com.esl.internship.staffsync.system.configuration.model.dto.CreateNotificationDTO;
import com.esl.internship.staffsync.system.configuration.model.dto.CreateNotificationTemplateDTO;
import com.esl.internship.staffsync.system.configuration.model.dto.EditNotificationTemplateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface SystemConfigurationMapper {

    SystemConfigurationMapper INSTANCE = Mappers.getMapper(SystemConfigurationMapper.class);

    JpaAppConfig mapAppConfig(AppConfig model);

    AppConfig mapAppConfig(JpaAppConfig entity);


    @Mappings({

            @Mapping(target = "receiver.employeeId", source = "receiverId"),
            @Mapping(target = "sender.employeeId", source = "senderId"),
            @Mapping(target = "notificationTemplateBean.notificationTemplateId", source = "notificationTemplateBeanId")
    })
    JpaNotification notificationToJpaNotification(Notification notification);

    Notification createNotificationToNotification(CreateNotificationDTO createNotification);

    @Mappings({

            @Mapping(target = "receiverId", source = "receiver.employeeId"),
            @Mapping(target = "senderId", source = "sender.employeeId"),
            @Mapping(target = "notificationTemplateBeanId", source = "notificationTemplateBean.notificationTemplateId")
    })
    Notification jpaNotificationToNotification(JpaNotification jpaNotification);

    @Mapping(target = "notificationList", source = "notifications")
    NotificationTemplate jpaNotificationTemplateToNotificationTemplate(JpaNotificationTemplate jpaNotificationTemplate);

    @Mapping(target = "notifications", source = "notificationList")
    JpaNotificationTemplate notificationTemplateToJpaNotificationTemplate(NotificationTemplate notificationTemplate);

    NotificationTemplate dtoToNotificationTemplate(CreateNotificationTemplateDTO notificationTemplateDTO);

    /**
     * @author DEMILADE
     * @dateCreated 03/08/2023
     * @description A method to map new values of the JpaNotificationTemplate object to itself
     *
     * @param j JpaNotificationTemplate object to be updated
     * @param e NotificationTemplateDTO object containing the value of the fields to be updated
     */
    default void editDTOToJpaNotificationTemplate(JpaNotificationTemplate j, EditNotificationTemplateDTO e) {

        if (e.getNotificationTemplateName().isPresent()) {
            j.setNotificationTemplateName(e.getNotificationTemplateName().get());
        }

        if (e.getNotificationDescription().isPresent()) {
            j.setNotificationDescription(e.getNotificationDescription().get());
        }

        if (e.getNotificationTemplateContent().isPresent()) {
            j.setNotificationTemplateContent(e.getNotificationTemplateContent().get());
        }

    }

    /**
     * @author DEMILADE
     * @dateCreated 03/08/2023
     * @description Default method to map JpaNotification Objects to their ids
     *
     * @param notificationSet - a set of notification objects
     * @return - a set of strings representing the notificationIds of the notification objects
     *
     **/
    default Set<String> toNotificationIds(Set<JpaNotification> notificationSet) {
        if (notificationSet == null) {
            return null;
        }
        return notificationSet.stream().map(JpaNotification::getNotificationId).collect(Collectors.toSet());
    }

    /**
     * @author DEMILADE
     * @dateCreated 03/08/2023
     * @description Default method to map a collection of notificationIds to a collection of JpaNotification objects
     *
     * @param notificationIds - a set of notification Ids
     * @return - a set of  JpaNotification objects
     */
    default Set<JpaNotification> toJpaNotifications(Set<String> notificationIds) {
        if (notificationIds == null) {
            return new HashSet<>();
        }
        Set<JpaNotification> jpaNotificationSet = new HashSet<>();
        for (String id : notificationIds) {
            JpaNotification j = new JpaNotification();
            j.setNotificationId(id);
            jpaNotificationSet.add(j);
        }
        return jpaNotificationSet;
    }
}
