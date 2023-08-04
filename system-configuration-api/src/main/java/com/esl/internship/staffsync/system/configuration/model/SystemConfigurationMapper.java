package com.esl.internship.staffsync.system.configuration.model;

import com.encentral.staffsync.entity.JpaAppConfig;
import com.encentral.staffsync.entity.JpaPermission;
import com.encentral.staffsync.entity.JpaRole;
import com.encentral.staffsync.entity.JpaRoleHasPermission;
import com.esl.internship.staffsync.system.configuration.dto.PermissionDTO;
import com.esl.internship.staffsync.system.configuration.dto.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SystemConfigurationMapper {

    SystemConfigurationMapper INSTANCE = Mappers.getMapper(SystemConfigurationMapper.class);

    JpaAppConfig mapAppConfig(AppConfig model);

    AppConfig mapAppConfig(JpaAppConfig entity);

    JpaPermission mapPermission(Permission model);
    Permission mapPermission(JpaPermission entity);
    JpaPermission mapPermissionDto(PermissionDTO dto);
    PermissionDTO mapPermissionDto(JpaPermission entity);

    JpaRole mapRole(Role model);
    Role mapRole(JpaRole entity);
    JpaRole mapRoleDto(RoleDTO dto);
    RoleDTO mapRoleDto(JpaRole entity);

    JpaRoleHasPermission mapRoleHasPermission(RoleHasPermission model);
    RoleHasPermission mapRoleHasPermission(JpaRoleHasPermission entity);


}
