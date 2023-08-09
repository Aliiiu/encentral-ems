package com.encentral.staffsync.entity.attribute.converter;


import com.encentral.staffsync.entity.enums.LeaveRequestStatus;
import org.postgresql.util.PGobject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.SQLException;

@Converter
public class LeaveRequestStatusConverter implements AttributeConverter<LeaveRequestStatus, Object> {
    @Override
    public Object convertToDatabaseColumn(LeaveRequestStatus leaveRequest) {
        try {
            PGobject pgObject = new PGobject();
            pgObject.setType("leave_request_status");
            pgObject.setValue(leaveRequest.name());
            return pgObject;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public LeaveRequestStatus convertToEntityAttribute(Object obj) {
        if (obj instanceof String)
            return LeaveRequestStatus.valueOf((String)obj);
        else if (obj instanceof PGobject)
            return LeaveRequestStatus.valueOf(((PGobject)obj).getValue());
        return null;
    }
}
