package com.encentral.staffsync.entity.enums;

public enum NotificationStatus {
    UNREAD("UNREAD"), READ("READ"), DELETED("DELETED");

    private final String value;

    NotificationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
