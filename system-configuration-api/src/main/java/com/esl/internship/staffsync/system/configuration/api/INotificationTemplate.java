package com.esl.internship.staffsync.system.configuration.api;

import com.encentral.scaffold.commons.model.Employee;
import com.esl.internship.staffsync.system.configuration.model.Notification;
import com.esl.internship.staffsync.system.configuration.model.NotificationTemplate;
import com.esl.internship.staffsync.system.configuration.dto.CreateNotificationTemplateDTO;
import com.esl.internship.staffsync.system.configuration.dto.EditNotificationTemplateDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;
import java.util.Optional;

public interface INotificationTemplate {

    Optional<NotificationTemplate> getNotificationTemplate(String notificationTemplateId);

    List<NotificationTemplate> getAllNotificationTemplates();

    List<Notification> getNotifications(String notificationTemplateId);

    NotificationTemplate createNotificationTemplate(CreateNotificationTemplateDTO notificationTemplate, Employee employee );

    boolean editNotificationTemplate(EditNotificationTemplateDTO notificationTemplate, Employee employee );

    boolean deleteNotificationTemplate(String notificationTemplateId);

    boolean checkIfNotificationTemplateNameInUse(String templateName);

    boolean checkIfNotificationTemplateExists(String notificationTemplateId);
}
