package com.esl.internship.staffsync.system.configuration.dto;


public class RoleDTO {

    private String roleDescription;
    private String roleName;

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
                "roleDescription='" + roleDescription + '\'' +
                ", roleName='" + roleName + '\'' +
                '}';
    }

}
