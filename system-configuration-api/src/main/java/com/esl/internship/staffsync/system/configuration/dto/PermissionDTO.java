package com.esl.internship.staffsync.system.configuration.dto;


import com.esl.internship.staffsync.entities.enums.Action;


public class PermissionDTO {

    private Action permissionAction;
    private String permissionDescription;
    private String permissionName;

    public Action getPermissionAction() {
        return permissionAction;
    }

    public void setPermissionAction(Action permissionAction) {
        this.permissionAction = permissionAction;
    }

    public String getPermissionDescription() {
        return permissionDescription;
    }

    public void setPermissionDescription(String permissionDescription) {
        this.permissionDescription = permissionDescription;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    @Override
    public String toString() {
        return "PermissionDTO{" +
                "permissionAction=" + permissionAction +
                ", permissionDescription='" + permissionDescription + '\'' +
                ", permissionName='" + permissionName + '\'' +
                '}';
    }

}
