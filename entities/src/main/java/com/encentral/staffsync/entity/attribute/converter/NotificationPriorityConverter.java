package com.encentral.staffsync.entity.attribute.converter;

import com.encentral.staffsync.entity.enums.NotificationPriority;
import org.postgresql.util.PGobject;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.SQLException;


@Converter
public class NotificationPriorityConverter implements AttributeConverter<NotificationPriority, Object> {

    public NotificationPriorityConverter() {
    }

    public PGobject convertToDatabaseColumn(NotificationPriority notificationPriority) {
        try {
            PGobject po = new PGobject();
            po.setType("notification_priority");
            po.setValue(notificationPriority.name());
            return po;
        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
    }

    public NotificationPriority convertToEntityAttribute(Object obj) {
        if (obj instanceof  String){
            return NotificationPriority.valueOf((String )obj);
        }
        else if (obj instanceof  PGobject ){
            return NotificationPriority.valueOf(((PGobject) obj).getValue());
        }
        else {
            return null;
        }
    }
}