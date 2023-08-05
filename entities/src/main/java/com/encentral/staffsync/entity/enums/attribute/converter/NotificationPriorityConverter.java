package com.encentral.staffsync.entity.enums.attribute.converter;

import com.encentral.staffsync.entity.enums.NotificationPriority;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class NotificationPriorityConverter implements AttributeConverter<NotificationPriority, String> {
    @Override
    public String convertToDatabaseColumn(NotificationPriority notificationPriority) {
        if (notificationPriority == null ) return null;
        return notificationPriority.getValue();
    }

    @Override
    public NotificationPriority convertToEntityAttribute(String s) {
        return NotificationPriority.valueOf(s);
    }
}