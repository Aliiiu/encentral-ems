package com.encentral.staffsync.entity.attribute.converter;

import com.encentral.staffsync.entity.enums.Action;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ActionConverter implements AttributeConverter<Action, String> {
    @Override
    public String convertToDatabaseColumn(Action action) {
        return action.name();
    }

    @Override
    public Action convertToEntityAttribute(String s) {
        return Action.valueOf(s);
    }
}
