package com.esl.internship.staffsync.system.configuration.api;

import com.encentral.scaffold.commons.model.Employee;
import com.encentral.staffsync.entity.enums.Action;
import com.esl.internship.staffsync.system.configuration.dto.PermissionDTO;
import com.esl.internship.staffsync.system.configuration.model.Permission;
import com.esl.internship.staffsync.system.configuration.model.Role;

import java.util.List;
import java.util.Optional;

public interface IPermissionApi {
    Permission addPermission(Permission permission, Employee employee);
    Permission addPermission(PermissionDTO permissionDto, Employee employee);
    Optional<Permission> getPermissionById(String permissionId);
    Optional<Permission> getPermissionByName(String permissionName);
    List<Permission> getAllPermissions();
//    List<Permission> getPermissionsForRole(String roleId);
//    List<Role> getRoles(String permissionId);
    boolean updatePermission(String permissionId, Permission permission, Employee employee);
    boolean updatePermission(String permissionId, PermissionDTO permissionDto, Employee employee);
    boolean deletePermission(String permissionId);
}
