package com.encentral.staffsync.entity.attribute.converter;

import com.encentral.staffsync.entity.enums.EmployeeStatus;
import org.postgresql.util.PGobject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.SQLException;


@Converter
public class EmployeeStatusConverter implements AttributeConverter<EmployeeStatus, Object> {
    @Override
    public Object convertToDatabaseColumn(EmployeeStatus employeeStatus) {

        try {
            PGobject pGobject = new PGobject();
            pGobject.setType("employee_status");
            pGobject.setValue(employeeStatus.name());
            return pGobject;
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
