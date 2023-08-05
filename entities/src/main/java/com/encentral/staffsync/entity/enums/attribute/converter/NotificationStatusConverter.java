package com.encentral.staffsync.entity.enums.attribute.converter;

import com.encentral.staffsync.entity.enums.NotificationStatus;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class NotificationStatusConverter implements AttributeConverter<NotificationStatus, String> {
    @Override
    public String convertToDatabaseColumn(NotificationStatus notificationStatus) {
        return notificationStatus.getValue();
    }

    @Override
    public NotificationStatus convertToEntityAttribute(String s) {
        return NotificationStatus.valueOf(s);
    }
}