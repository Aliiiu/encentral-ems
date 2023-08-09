package com.esl.internship.staffsync.entities.enums;

import java.util.Collection;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toUnmodifiableMap;

public enum OptionTypeEnum {

    COUNTRY,
    STATE,
    GENDER,
    MARITAL_STATUS,
    CERTIFICATION,
    LEAVE_TYPE,
    EVENT_TYPE,
    DOCUMENT_TYPE;

    private static final Map<String, OptionTypeEnum> VALUES = stream(OptionTypeEnum.values()).collect(toUnmodifiableMap(Enum::name, identity()));

    String getDBValue() {
        return name().toLowerCase();
    }

    public static OptionTypeEnum from(String value) {
        if (!VALUES.containsKey(value)) {
            throw new IllegalArgumentException(String.format("Cant find OptionType %s", value));
        }
        return VALUES.get(value);
    }

    boolean contains(String value) {
        return VALUES.containsKey(value);
    }

    public static Collection<OptionTypeEnum> collection() {
        return VALUES.values();
    }

}
