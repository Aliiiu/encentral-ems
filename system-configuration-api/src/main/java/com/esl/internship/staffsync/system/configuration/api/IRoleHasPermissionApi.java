package com.esl.internship.staffsync.system.configuration.api;

import com.encentral.scaffold.commons.model.Employee;
import com.esl.internship.staffsync.system.configuration.model.Permission;
import com.esl.internship.staffsync.system.configuration.model.Role;
import com.esl.internship.staffsync.system.configuration.model.RoleHasPermission;

import java.util.List;
import java.util.Optional;

public interface IRoleHasPermissionApi {
    boolean givePermissionToRole(String roleId, String permissionId, Employee employee);
    boolean removePermissionFromRole(String roleId, String permissionId);
    Optional<RoleHasPermission> getRoleHasPermission(String roleHasPermissionId);
    boolean removeRoleHasPermission(String roleHasPermissionId);
    List<Role> getRolesForPermissionApi(String permissionId);
    List<Permission> getPermissionForRole(String roleId);
    List<RoleHasPermission> getAllRoleHasPermission();
}
