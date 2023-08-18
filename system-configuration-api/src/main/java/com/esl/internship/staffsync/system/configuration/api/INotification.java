package com.esl.internship.staffsync.system.configuration.api;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.entities.enums.NotificationPriority;
import com.esl.internship.staffsync.system.configuration.model.Notification;
import com.esl.internship.staffsync.system.configuration.dto.CreateNotificationDTO;
import java.util.List;
import java.util.Optional;

public interface INotification {
    Optional<Notification> getNotification(String notificationId);

    List<Notification> getEmployeeReceivedNotifications(String employeeId);

    List<Notification> getEmployeeSentNotifications(String employeeId);

    List<Notification> getEmployeeUnreadNotifications(String employeeId);

    List<Notification> getSystemNotifications();

    boolean markNotificationAsRead(String notificationId, Employee employee);

    Notification createNotification(CreateNotificationDTO createNotificationDTO, String employeeId);

    boolean sendNotification(String receiverId, String senderId, String subject, String object, String templateId);

    boolean markNotificationAsDeleted(String notificationId, Employee employee);

    boolean markAllEmployeeNotificationAsRead(String employeeId, Employee employee);

    boolean markAllEmployeeNotificationAsDeleted(String employeeId, Employee employee);

    boolean deleteAllEmployeeNotification(String employeeId);

    boolean deleteNotification(String notificationId);

    boolean verifyEmployee(String employeeId, String notificationId);
}
