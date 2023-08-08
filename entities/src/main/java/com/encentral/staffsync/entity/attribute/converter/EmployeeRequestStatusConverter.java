package com.encentral.staffsync.entity.attribute.converter;


import com.encentral.staffsync.entity.enums.EmployeeRequestStatus;
import org.postgresql.util.PGobject;

import javax.persistence.AttributeConverter;
import java.sql.SQLException;

public class EmployeeRequestStatusConverter implements AttributeConverter<EmployeeRequestStatus, Object> {

    @Override
    public Object convertToDatabaseColumn(EmployeeRequestStatus employeeRequestStatus) {
        try {
            PGobject pgObject = new PGobject();
            pgObject.setType("employee_request_status");
            pgObject.setValue(employeeRequestStatus.name());
            return pgObject;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public EmployeeRequestStatus convertToEntityAttribute(Object obj) {
        if (obj instanceof String)
            return EmployeeRequestStatus.valueOf((String)obj);
        else if (obj instanceof PGobject)
            return EmployeeRequestStatus.valueOf(((PGobject)obj).getValue());
        return null;
    }

}
