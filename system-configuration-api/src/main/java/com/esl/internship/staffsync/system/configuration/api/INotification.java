package com.esl.internship.staffsync.system.configuration.api;

import com.encentral.scaffold.commons.model.Employee;
import com.esl.internship.staffsync.system.configuration.model.Notification;
import com.esl.internship.staffsync.system.configuration.model.dto.CreateNotificationDTO;
import java.util.List;
import java.util.Optional;

public interface INotification {
    Optional<Notification> getNotification(String notificationId);

    List<Notification> getEmployeeReceivedNotifications(String employeeId);

    List<Notification> getEmployeeSentNotifications(String employeeId);

    List<Notification> getEmployeeUnreadNotifications(String employeeId);

    List<Notification> getSystemNotifications();

    boolean markNotificationAsRead(String notificationId, Employee employee);

    Notification createNotification(CreateNotificationDTO createNotificationDTO, Employee employee);

    boolean markNotificationAsDeleted(String notificationId, Employee employee);

    boolean markAllEmployeeNotificationAsRead(String employeeId, Employee employee);

    boolean markAllEmployeeNotificationAsDeleted(String employeeId, Employee employee);

    boolean deleteAllEmployeeNotification(String employeeId);

    boolean deleteNotification(String notificationId);

    boolean verifyEmployee(String employeeId, String notificationId);
}
