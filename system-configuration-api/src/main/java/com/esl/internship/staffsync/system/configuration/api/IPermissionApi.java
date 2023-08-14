package com.esl.internship.staffsync.system.configuration.api;


import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.system.configuration.dto.PermissionDTO;
import com.esl.internship.staffsync.system.configuration.model.Permission;

import java.util.List;
import java.util.Optional;


public interface IPermissionApi {

    Permission addPermission(PermissionDTO permissionDto, Employee employee);

    Optional<Permission> getPermissionById(String permissionId);

    Optional<Permission> getPermissionByName(String permissionName);

    List<Permission> getAllPermissions();

    boolean updatePermission(String permissionId, PermissionDTO permissionDto, Employee employee);

    boolean deletePermission(String permissionId);

}
