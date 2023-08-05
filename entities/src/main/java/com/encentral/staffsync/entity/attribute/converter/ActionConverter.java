package com.encentral.staffsync.entity.attribute.converter;

import com.encentral.staffsync.entity.enums.Action;
import org.postgresql.util.PGobject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.SQLException;

@Converter
public class ActionConverter implements AttributeConverter<Action, Object> {
    @Override
    public Object convertToDatabaseColumn(Action action) {
        try {
            PGobject pgObject = new PGobject();
            pgObject.setType("action");
            pgObject.setValue(action.name());
            return pgObject;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Action convertToEntityAttribute(Object obj) {
        if (obj instanceof String)
            return Action.valueOf((String)obj);
        else if (obj instanceof PGobject)
            return Action.valueOf(((PGobject)obj).getValue());
        return null;
    }
//    @Override
//    public PGobject convertToDatabaseColumn(Action action) {
//        try {
//            PGobject pgObject = new PGobject();
//            pgObject.setType("action");
//            pgObject.setValue(action.name());
//            return pgObject;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @Override
//    public Action convertToEntityAttribute(PGobject dbData) {
//        return dbData == null ? null : Action.valueOf(dbData.getValue());
//    }
}
