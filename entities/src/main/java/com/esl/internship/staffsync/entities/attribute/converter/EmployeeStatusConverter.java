package com.esl.internship.staffsync.entities.attribute.converter;

import com.esl.internship.staffsync.entities.enums.EmployeeStatus;
import org.postgresql.util.PGobject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.SQLException;

@Converter
public class EmployeeStatusConverter implements AttributeConverter<EmployeeStatus, Object> {
    @Override
    public Object convertToDatabaseColumn(EmployeeStatus leaveRequest) {
        try {
            PGobject pgObject = new PGobject();
            pgObject.setType("employee_status");
            pgObject.setValue(leaveRequest.name());
            return pgObject;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public EmployeeStatus convertToEntityAttribute(Object obj) {
        if (obj instanceof String)
            return EmployeeStatus.valueOf((String)obj);
        else if (obj instanceof PGobject)
            return EmployeeStatus.valueOf(((PGobject)obj).getValue());
        return null;
    }
}
