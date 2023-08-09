package com.esl.internship.staffsync.entities.attribute.converter;

import com.esl.internship.staffsync.entities.enums.NotificationStatus;
import org.postgresql.util.PGobject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.SQLException;

@Converter
public class NotificationStatusConverter implements AttributeConverter<NotificationStatus, Object> {

    public NotificationStatusConverter() {
    }

    public PGobject convertToDatabaseColumn(NotificationStatus notificationStatus) {
        try {
            PGobject po = new PGobject();
            po.setType("notification_status");
            po.setValue(notificationStatus.name());
            return po;
        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
    }

    public NotificationStatus convertToEntityAttribute(Object obj) {
        if (obj instanceof  String){
            return NotificationStatus.valueOf((String )obj);
        }
        else if (obj instanceof  PGobject ){
            return NotificationStatus.valueOf(((PGobject) obj).getValue());
        }
        else {
            return null;
        }
    }
}