package com.esl.internship.staffsync.entities.attribute.converter;

import org.postgresql.util.PGobject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.SQLException;

@Converter
public class JsonStringConverter implements AttributeConverter<String, PGobject> {

    public JsonStringConverter() {
    }

    public PGobject convertToDatabaseColumn(String attribute) {
        try {
            PGobject po = new PGobject();
            po.setType("json");
            po.setValue(attribute);
            return po;
        } catch (SQLException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public String convertToEntityAttribute(PGobject dbData) {
        return dbData == null ? null : dbData.getValue();
    }

}
