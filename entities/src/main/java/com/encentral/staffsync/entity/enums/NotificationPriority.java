package com.encentral.staffsync.entity.enums;

public enum NotificationPriority {
    LOW("LOW"), NORMAL("NORMAL"), HIGH("HIGH"), VERY_HIGH("VERY_HIGH");

    private final String value;

    NotificationPriority(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
